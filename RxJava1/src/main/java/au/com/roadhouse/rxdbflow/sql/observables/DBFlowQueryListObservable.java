package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxSQLite query, emits the results from the query as a FlowQueryList of models.
 */
public class DBFlowQueryListObservable<TModel> extends Observable<FlowQueryList<TModel>> {

    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final Class<TModel> mModelClazz;
    private List<Class> mSubscribedClasses;

    /**
     * Creates a new observable which runs a query and emits the result as a FlowQueryList
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowQueryListObservable(Class<TModel> clazz, final ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mSubscribedClasses = new ArrayList<>();
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query results to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<FlowQueryList<TModel>> restartOnChange(){
        mSubscribedClasses.add(mModelClazz);
        return lift(new DBFlowOnChangeOperator());
    }

    /**
     * Observes changes on the current table, restarts the query on change, and emits the updated
     * query results to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    @SafeVarargs
    public final Observable<FlowQueryList<TModel>> restartOnChange(Class<? extends Model>... tableToListen){
        Collections.addAll(mSubscribedClasses, tableToListen);
        return lift(new DBFlowOnChangeOperator());
    }

    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange()} or {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public Observable<FlowQueryList<TModel>> completeOnResult(){
        return lift(new CompleteOnResultOperator<FlowQueryList<TModel>>());
    }

    private static class OnDBFlowSubscribeWithChanges<AModel> implements OnSubscribe<FlowQueryList<AModel>> {

        private final ModelQueriable<AModel> mBaseModelQueriable;

        OnDBFlowSubscribeWithChanges(Class<AModel> clazz, ModelQueriable<AModel> baseModelQueriable){
            mBaseModelQueriable = baseModelQueriable;
        }


        @Override
        public void call(final Subscriber<? super FlowQueryList<AModel>> subscriber) {
            subscriber.onNext(mBaseModelQueriable.flowQueryList());
        }
    }


    private class DBFlowOnChangeOperator implements Observable.Operator<FlowQueryList<TModel>, FlowQueryList<TModel>> {

        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super FlowQueryList<TModel>> call(final Subscriber<? super FlowQueryList<TModel>> subscriber) {
            return new Subscriber<FlowQueryList<TModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(FlowQueryList<TModel> tModels) {
                    for (int i = 0; i < mSubscribedClasses.size(); i++) {
                        mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mSubscribedClasses.get(i));
                    }
                    mFlowContentObserver.addOnTableChangedListener(
                            new FlowContentObserver.OnTableChangedListener() {
                                @Override
                                public void onTableChanged(@Nullable Class<?> tableChanged, BaseModel.Action action) {

                                    if (subscriber.isUnsubscribed()) {
                                        mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                                    } else {
                                        subscriber.onNext(mBaseModelQueriable.flowQueryList());
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
