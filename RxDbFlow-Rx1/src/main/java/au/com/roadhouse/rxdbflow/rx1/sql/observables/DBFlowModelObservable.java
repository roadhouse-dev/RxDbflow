package au.com.roadhouse.rxdbflow.rx1.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxSQLite query, emits the the first element from the query results.
 */
public class DBFlowModelObservable<TModel> extends Observable<TModel> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;
    private List<Class> mSubscribedClasses;

    /**
     * Creates a new observable which runs a query and emits the first element in the result set as a CustomModel
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowModelObservable(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable,
                                 @Nullable DatabaseWrapper databaseWrapper) {
        super(new OnDBFlowSubscribeWithChanges<>(baseModelQueriable, databaseWrapper));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
        mSubscribedClasses = new ArrayList<>();
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query result to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<TModel> restartOnChange(){
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
    public Observable<TModel> completeOnResult(){
        return lift(new CompleteOnResultOperator<TModel>());
    }

    /**
     * Observes changes on the current table, restarts the query on change, and emits the updated
     * query result to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    @SafeVarargs
    public final Observable<TModel> restartOnChange(Class<? extends Model>... tableToListen){
        Collections.addAll(mSubscribedClasses, tableToListen);
        return lift(new DBFlowOnChangeOperator());
    }


    private TModel runQuery(){
        if(mDatabaseWrapper != null){
            return mBaseModelQueriable.querySingle(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.querySingle();
        }
    }

    private static class OnDBFlowSubscribeWithChanges<AModel> implements OnSubscribe<AModel> {
        private final ModelQueriable<AModel> mBaseModelQueriable;
        private final DatabaseWrapper mDatabaseWrapper;

        OnDBFlowSubscribeWithChanges(ModelQueriable<AModel> baseModelQueriable,
                                    DatabaseWrapper databaseWrapper){
            mBaseModelQueriable = baseModelQueriable;
            mDatabaseWrapper = databaseWrapper;
        }

        @Override
        public void call(final Subscriber<? super AModel> subscriber) {
            subscriber.onNext(runQuery());
        }

        private AModel runQuery(){
            if(mDatabaseWrapper != null){
                return mBaseModelQueriable.querySingle(mDatabaseWrapper);
            } else {
                return mBaseModelQueriable.querySingle();
            }
        }
    }

    private class DBFlowOnChangeOperator implements Operator<TModel, TModel> {
        private FlowContentObserver mFlowContentObserver;


        public DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super TModel> call(final Subscriber<? super TModel> subscriber) {
            return new Subscriber<TModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(TModel tModels) {
                    for (int i = 0; i < mSubscribedClasses.size(); i++) {
                        mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mSubscribedClasses.get(i));
                    }
                    mFlowContentObserver.addModelChangeListener(new FlowContentObserver.OnModelStateChangedListener() {
                        @Override
                        public void onModelStateChanged(@Nullable Class<?> table, BaseModel.Action action, @NonNull SQLOperator[] primaryKeyValues) {
                            if (subscriber.isUnsubscribed()) {
                                mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                            } else {
                                subscriber.onNext(runQuery());
                            }
                        }
                    });

                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
