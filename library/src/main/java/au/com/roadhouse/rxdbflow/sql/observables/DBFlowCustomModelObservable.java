package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;

import rx.Observable;
import rx.Subscriber;


public class DBFlowCustomModelObservable<TQueryModel extends BaseQueryModel, TModel extends Model> extends Observable<TQueryModel> {

    private final Class<TQueryModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;

    public DBFlowCustomModelObservable(Class<TQueryModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    public Observable<TQueryModel> subscribeToChanges(){
        return lift(new DBFlowOnChangeOperator(mModelClazz, mBaseModelQueriable));
    }

    private static class OnDBFlowSubscribeWithChanges<AQueryModel extends BaseQueryModel, TModel extends Model> implements OnSubscribe<AQueryModel> {

        private final Class<AQueryModel> mClazz;
        private final ModelQueriable<TModel> mBaseModelQueriable;

        OnDBFlowSubscribeWithChanges(Class<AQueryModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
            mBaseModelQueriable = baseModelQueriable;
            mClazz = clazz;
        }


        @Override
        public void call(final Subscriber<? super AQueryModel> subscriber) {
            subscriber.onNext(mBaseModelQueriable.queryCustomSingle(mClazz));
        }
    }

    private class DBFlowOnChangeOperator implements Observable.Operator<TQueryModel, TQueryModel> {

        private final Class<TQueryModel> mModelClazz;
        private final ModelQueriable<TModel> mBaseModelQueriable;
        private FlowContentObserver mFlowContentObserver;

        public DBFlowOnChangeOperator(Class<TQueryModel> modelClazz, ModelQueriable<TModel> baseModelQueriable) {
            mModelClazz = modelClazz;
            mBaseModelQueriable = baseModelQueriable;
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super TQueryModel> call(final Subscriber<? super TQueryModel> subscriber) {
            return new Subscriber<TQueryModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(TQueryModel tModels) {
                    mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mModelClazz);
                    mFlowContentObserver.addModelChangeListener(new FlowContentObserver.OnModelStateChangedListener() {
                        @Override
                        public void onModelStateChanged(@Nullable Class<? extends Model> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {
                            if (subscriber.isUnsubscribed()) {
                                mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                            } else {
                                subscriber.onNext(mBaseModelQueriable.queryCustomSingle(mModelClazz));
                            }
                        }
                    });
                }
            };

        }
    }
}
