package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxSQLite query, emits the results from the query as a CursorResult of models.
 */
public class DBFlowResultObservable<TModel extends Model> extends Observable<CursorResult<TModel>> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private List<Class<? extends Model>> mSubscribedClasses;

    /**
     * Creates a new observable which runs a query and emits the result as a CursorResult
     * @param clazz The table/view model in which the CursorResult will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowResultObservable(Class<TModel> clazz, final ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(baseModelQueriable));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mSubscribedClasses = new ArrayList<>();
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query results to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<CursorResult<TModel>> restartOnChange(){
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
    public final Observable<CursorResult<TModel>> restartOnChange(Class<? extends Model>... tableToListen){
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
    public Observable<CursorResult<TModel>> completeOnResult(){
        return lift(new CompleteOnResultOperator<CursorResult<TModel>>());
    }


    private static class OnDBFlowSubscribeWithChanges<AModel extends Model> implements OnSubscribe<CursorResult<AModel>> {

        private final ModelQueriable<AModel> mBaseModelQueriable;

        OnDBFlowSubscribeWithChanges(ModelQueriable<AModel> baseModelQueriable){
            mBaseModelQueriable = baseModelQueriable;
        }

        @Override
        public void call(final Subscriber<? super CursorResult<AModel>> subscriber) {
            subscriber.onNext(mBaseModelQueriable.queryResults());
        }
    }

    private class DBFlowOnChangeOperator implements Observable.Operator<CursorResult<TModel>, CursorResult<TModel>> {

        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super CursorResult<TModel>> call(final Subscriber<? super CursorResult<TModel>> subscriber) {
            return new Subscriber<CursorResult<TModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(CursorResult<TModel> tModels) {
                    for (int i = 0; i < mSubscribedClasses.size(); i++) {
                        mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mSubscribedClasses.get(i));
                    }
                    mFlowContentObserver.addOnTableChangedListener(
                            new FlowContentObserver.OnTableChangedListener() {
                                @Override
                                public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {

                                    if (subscriber.isUnsubscribed()) {
                                        mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                                    } else {
                                        subscriber.onNext(mBaseModelQueriable.queryResults());
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
