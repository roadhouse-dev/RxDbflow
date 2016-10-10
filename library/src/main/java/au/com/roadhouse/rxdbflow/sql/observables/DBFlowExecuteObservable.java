package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import rx.Observable;
import rx.Subscriber;


public class DBFlowExecuteObservable<TModel extends Model> extends Observable<Void> {

    private final Class<? extends Model> mModelClazz;
    private final BaseQueriable<TModel> mBaseQueriable;

    public DBFlowExecuteObservable(Class<? extends Model> clazz, BaseQueriable<TModel> modelQueriable, @Nullable DatabaseWrapper databaseWrapper) {
        super(new OnDBFlowSubscribe(modelQueriable, databaseWrapper));
        mModelClazz = clazz;
        mBaseQueriable = modelQueriable;
    }

    private static class OnDBFlowSubscribe implements OnSubscribe<Void> {

        private final Queriable mBaseQueriable;;
        private final DatabaseWrapper mDatabaseWrapper;

        OnDBFlowSubscribe(Queriable modelQueriable, DatabaseWrapper databaseWrapper){
            mBaseQueriable = modelQueriable;
            mDatabaseWrapper = databaseWrapper;
        }

        @Override
        public void call(final Subscriber<? super Void> subscriber) {
            if(mDatabaseWrapper != null) {
                mBaseQueriable.execute(mDatabaseWrapper);
            } else {
                mBaseQueriable.execute();
            }

            subscriber.onNext(null);
        }
    }

    public Observable<Void> publishTableUpdates(){
        return lift(new DBFlowNotifyOfUpdate());
    }

    private class DBFlowNotifyOfUpdate implements Observable.Operator<Void, Void> {
        @Override
        public Subscriber<? super Void> call(final Subscriber<? super Void> subscriber) {
            return new Subscriber<Void>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Void tModels) {
                    subscriber.onNext(tModels);

                    if(mBaseQueriable.getQuery().toLowerCase().contains("delete")){
                        SqlUtils.notifyModelChanged(mModelClazz, BaseModel.Action.DELETE, null);
                    } else if(mBaseQueriable.getQuery().toLowerCase().contains("update")){
                        SqlUtils.notifyModelChanged(mModelClazz, BaseModel.Action.UPDATE, null);
                    }
                }
            };

        }
    }
}
