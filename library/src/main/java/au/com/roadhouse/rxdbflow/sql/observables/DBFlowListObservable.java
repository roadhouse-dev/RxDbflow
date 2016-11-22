package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class DBFlowListObservable<TModel extends Model> extends Observable<List<TModel>> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private DatabaseWrapper mDatabaseWrapper;

    public DBFlowListObservable(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable,
                                 @Nullable DatabaseWrapper databaseWrapper) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable, databaseWrapper));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }

    public Observable<List<TModel>> restartOnChange(){
        return lift(new DBFlowOnChangeOperator(mModelClazz, mBaseModelQueriable));
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query results to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    @SafeVarargs
    public final Observable<List<TModel>> restartOnChange(Class<? extends Model>... tableToListen){
        return lift(new DBFlowOnChangeOperator(mModelClazz, mBaseModelQueriable, tableToListen));
    }

    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange()} or {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public Observable<List<TModel>> completeOnResult(){
        return lift(new CompleteOnResultOperator<List<TModel>>());
    }

    private static class OnDBFlowSubscribeWithChanges<AModel extends Model> implements OnSubscribe<List<AModel>> {

        private final ModelQueriable<AModel> mBaseModelQueriable;
        private final Class<AModel> mClazz;
        private final DatabaseWrapper mDatabaseWrapper;

        OnDBFlowSubscribeWithChanges(Class<AModel> clazz, ModelQueriable<AModel> baseModelQueriable,
                                     DatabaseWrapper databaseWrapper){
            mBaseModelQueriable = baseModelQueriable;
            mDatabaseWrapper = databaseWrapper;
            mClazz = clazz;
        }

        @Override
        public void call(final Subscriber<? super List<AModel>> subscriber) {
            subscriber.onNext(runQuery());
        }

        private List<AModel> runQuery(){
            if(mDatabaseWrapper != null){
                return mBaseModelQueriable.queryList(mDatabaseWrapper);
            } else {
                return mBaseModelQueriable.queryList();
            }
        }
    }

    private List<TModel> runQuery(){
        if(mDatabaseWrapper != null){
            return mBaseModelQueriable.queryList(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.queryList();
        }
    }

    private class DBFlowOnChangeOperator implements Observable.Operator<List<TModel>, List<TModel>> {

        private final Class<TModel> mModelClazz;
        private final ModelQueriable<TModel> mBaseModelQueriable;
        private FlowContentObserver mFlowContentObserver;
        private List<Class<? extends Model>> mSubscribedClasses;

        public DBFlowOnChangeOperator(Class<TModel> modelClazz, ModelQueriable<TModel> baseModelQueriable) {
            mSubscribedClasses = new ArrayList<>();
            mModelClazz = modelClazz;
            mBaseModelQueriable = baseModelQueriable;
            mFlowContentObserver = new FlowContentObserver();
            mSubscribedClasses.add(mModelClazz);
        }

        public DBFlowOnChangeOperator(Class<TModel> modelClazz, ModelQueriable<TModel> baseModelQueriable, Class<? extends Model>[] tableToListen) {
            mSubscribedClasses = new ArrayList<>();
            mModelClazz = modelClazz;
            mBaseModelQueriable = baseModelQueriable;
            mFlowContentObserver = new FlowContentObserver();
            for (int i = 0; i < tableToListen.length; i++) {
                mSubscribedClasses.add(tableToListen[i]);
            }
        }

        @Override
        public Subscriber<? super List<TModel>> call(final Subscriber<? super List<TModel>> subscriber) {
            return new Subscriber<List<TModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<TModel> tModels) {
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
                                        subscriber.onNext(mBaseModelQueriable.queryList());
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
