package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import rx.Observable;
import rx.Subscriber;

/**
 * Given an RxSQLite query, executes a statement without a result.
 * @param <TModel> The model representing the table to execute against
 */
public class DBFlowExecuteObservable<TModel> extends Observable<Void> {

    private final Class mModelClazz;
    private final BaseQueriable<TModel> mBaseQueriable;

    /**
     * Creates a new Observable which executes a sql statement against a table
     * @param clazz The model class representing the table to execute against
     * @param modelQueriable The query to execute
     * @param databaseWrapper The database in which the target table resides
     */
    public DBFlowExecuteObservable(Class clazz, BaseQueriable<TModel> modelQueriable, @Nullable DatabaseWrapper databaseWrapper) {
        super(new OnDBFlowSubscribe(modelQueriable, databaseWrapper));
        mModelClazz = clazz;
        mBaseQueriable = modelQueriable;
    }

    /**
     * Publishes the results to all onchange observers after the statement has been executued.
     * @return An observable which publishes the change to all onchange observers
     */
    public Observable<Void> publishTableUpdates(){
        return lift(new DBFlowNotifyOfUpdate());
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
            subscriber.onCompleted();
        }
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
