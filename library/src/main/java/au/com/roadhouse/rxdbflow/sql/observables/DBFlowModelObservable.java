package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import rx.Observable;
import rx.Subscriber;


public class DBFlowModelObservable<TModel extends Model> extends Observable<TModel> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;

    public DBFlowModelObservable(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable,
                                 boolean subscribeToChanges, @Nullable DatabaseWrapper databaseWrapper) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable, subscribeToChanges, databaseWrapper));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }

    public Observable<TModel> restartOnChange(){
        return lift(new DBFlowOnChangeOperator());
    }

    private TModel runQuery(){
        if(mDatabaseWrapper != null){
            return mBaseModelQueriable.querySingle(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.querySingle();
        }
    }

    private static class OnDBFlowSubscribeWithChanges<AModel extends Model> implements OnSubscribe<AModel> {

        private final boolean mSubscribeToModelChanges;
        private final ModelQueriable<AModel> mBaseModelQueriable;
        private final Class<AModel> mClazz;
        private final DatabaseWrapper mDatabaseWrapper;
        private FlowContentObserver mFlowContentObserver = new FlowContentObserver();

        OnDBFlowSubscribeWithChanges(Class<AModel> clazz, ModelQueriable<AModel> baseModelQueriable,
                                     boolean subscribeToModelChanges, DatabaseWrapper databaseWrapper){
           mSubscribeToModelChanges = subscribeToModelChanges;
            mBaseModelQueriable = baseModelQueriable;
            mDatabaseWrapper = databaseWrapper;
            mClazz = clazz;
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

    private class DBFlowOnChangeOperator implements Observable.Operator<TModel, TModel> {
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
                    mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mModelClazz);
                    mFlowContentObserver.addModelChangeListener(new FlowContentObserver.OnModelStateChangedListener() {
                        @Override
                        public void onModelStateChanged(@Nullable Class<? extends Model> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {
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
