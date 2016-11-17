package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxSQLite query, emits the results from the query as a FlowCursorList
 * @param <TModel> The table/view model in which the FlowCursorList will contain
 */
public class DBFlowCursorListObservable<TModel extends Model> extends Observable<FlowCursorList<TModel>> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;

    /**
     * Creates a new observable which runs a query and emits the result as a FlowCursorList
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The RxSQLite query
     */
    public DBFlowCursorListObservable(Class<TModel> clazz, final ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query results to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<FlowCursorList<TModel>> restartOnChange(){
        return lift(new DBFlowOnChangeOperator(mModelClazz, mBaseModelQueriable));
    }

    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange()} or {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public Observable<FlowCursorList<TModel>> completeOnResult(){
        return lift(new CompleteOnResultOperator<FlowCursorList<TModel>>());
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query results to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    public Observable<FlowCursorList<TModel>> restartOnChange(Class<TModel>... tableToListen){
        return lift(new DBFlowOnChangeOperator(mModelClazz, mBaseModelQueriable, tableToListen));
    }


    private static class OnDBFlowSubscribeWithChanges<AModel extends Model> implements OnSubscribe<FlowCursorList<AModel>> {

        private final ModelQueriable<AModel> mBaseModelQueriable;
        private final Class<AModel> mClazz;
        private FlowContentObserver mFlowContentObserver = new FlowContentObserver();

        OnDBFlowSubscribeWithChanges(Class<AModel> clazz, ModelQueriable<AModel> baseModelQueriable){
            mBaseModelQueriable = baseModelQueriable;
            mClazz = clazz;
        }

        @Override
        public void call(final Subscriber<? super FlowCursorList<AModel>> subscriber) {
            subscriber.onNext(mBaseModelQueriable.cursorList());
        }
    }



    private class DBFlowOnChangeOperator implements Observable.Operator<FlowCursorList<TModel>, FlowCursorList<TModel>> {

        private final Class<TModel> mModelClazz;
        private final ModelQueriable<TModel> mBaseModelQueriable;
        private FlowContentObserver mFlowContentObserver;
        private List<Class<TModel>> mSubscribedClasses;

        public DBFlowOnChangeOperator(Class<TModel> modelClazz, ModelQueriable<TModel> baseModelQueriable) {
            mSubscribedClasses = new ArrayList<>();
            mModelClazz = modelClazz;
            mBaseModelQueriable = baseModelQueriable;
            mFlowContentObserver = new FlowContentObserver();
            mSubscribedClasses.add(mModelClazz);
        }

        private DBFlowOnChangeOperator(Class<TModel> modelClazz, ModelQueriable<TModel> baseModelQueriable, Class<TModel>[] tableToListen) {
            mSubscribedClasses = new ArrayList<>();
            mModelClazz = modelClazz;
            mBaseModelQueriable = baseModelQueriable;
            mFlowContentObserver = new FlowContentObserver();
            for (int i = 0; i < tableToListen.length; i++) {
                mSubscribedClasses.add(tableToListen[i]);
            }
        }



        @Override
        public Subscriber<? super FlowCursorList<TModel>> call(final Subscriber<? super FlowCursorList<TModel>> subscriber) {
            return new Subscriber<FlowCursorList<TModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(FlowCursorList<TModel> tModels) {
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
                                        subscriber.onNext(mBaseModelQueriable.cursorList());
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
