package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

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
        return lift(new DBFlowOnChangeOperator());
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

        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
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
                    mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mModelClazz);
                    mFlowContentObserver.addOnTableChangedListener(
                            new FlowContentObserver.OnTableChangedListener() {
                                @Override
                                public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {

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
