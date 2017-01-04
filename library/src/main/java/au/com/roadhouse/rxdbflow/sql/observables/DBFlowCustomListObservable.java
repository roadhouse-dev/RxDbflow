package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxSQLite query, emits the results from the query as a List of custom models.
 */
public class DBFlowCustomListObservable<TQueryModel extends BaseQueryModel, TModel> extends Observable<List<TQueryModel>> {

    private final Class<TQueryModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private List<Class<? extends Model>> mSubscribedClasses;

    /**
     * Creates a new observable which runs a query and emits the result as a CustomList
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowCustomListObservable(Class<TQueryModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable));
        mSubscribedClasses = new ArrayList<>();
            mModelClazz = clazz;
            mBaseModelQueriable = baseModelQueriable;
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query results to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<List<TQueryModel>> restartOnChange(){
        mSubscribedClasses.add(mModelClazz);
        return lift(new DBFlowOnChangeOperator());
    }


    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange()} or {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public Observable<List<TQueryModel>> completeOnResult(){
        return lift(new CompleteOnResultOperator<List<TQueryModel>>());
    }

    /**
     * Observes changes on the current table, restarts the query on change, and emits the updated
     * query results to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    @SafeVarargs
    public final Observable<List<TQueryModel>> restartOnChange(Class<? extends Model>... tableToListen){
        Collections.addAll(mSubscribedClasses, tableToListen);
        return lift(new DBFlowOnChangeOperator());
    }

    private static class OnDBFlowSubscribeWithChanges<AQueryModel extends BaseQueryModel, TModel> implements OnSubscribe<List<AQueryModel>> {

        private final ModelQueriable<TModel> mBaseModelQueriable;
        private final Class<AQueryModel> mClazz;

        OnDBFlowSubscribeWithChanges(Class<AQueryModel> clazz, ModelQueriable<TModel> baseModelQueriable){
            mBaseModelQueriable = baseModelQueriable;
            mClazz = clazz;
        }

        @Override
        public void call(final Subscriber<? super List<AQueryModel>> subscriber) {
            subscriber.onNext(mBaseModelQueriable.queryCustomList(mClazz));
        }

    }

    private class DBFlowOnChangeOperator implements Observable.Operator<List<TQueryModel>, List<TQueryModel>> {

        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super List<TQueryModel>> call(final Subscriber<? super List<TQueryModel>> subscriber) {
            return new Subscriber<List<TQueryModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<TQueryModel> tModels) {
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
                                        subscriber.onNext(mBaseModelQueriable.queryCustomList(mModelClazz));
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
