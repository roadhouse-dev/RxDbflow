package au.com.roadhouse.rxdbflow.rx2.sql.observables;


import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.rx2.sql.observables.operators.NotifyOfUpdate;
import io.reactivex.Completable;

/**
 * Given an RxSQLite query, executes a statement without a result.
 */
public class DBFlowExecuteCompletable<TModel> extends DBFlowBaseCompletable<TModel>  {

    private final BaseQueriable mBaseQueriable;
    private final DatabaseWrapper mDatabaseWrapper;

    /**
     * Creates a new Observable which executes a sql statement against a table
     * @param clazz The model class representing the table to execute against
     * @param modelQueriable The query to execute
     * @param databaseWrapper The database in which the target table resides
     */
    public DBFlowExecuteCompletable(Class<TModel> clazz, BaseQueriable modelQueriable, @Nullable DatabaseWrapper databaseWrapper) {
        super(clazz);
        mBaseQueriable = modelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }


    public Completable notifyOfUpdates(){
        return new NotifyOfUpdate<>(this, mBaseQueriable.getQuery(), getPrimaryModelClass());
    }

    @Override
    public void run() {
        if(mDatabaseWrapper != null) {
            mBaseQueriable.execute(mDatabaseWrapper);
        } else {
            mBaseQueriable.execute();
        }
    }
}
