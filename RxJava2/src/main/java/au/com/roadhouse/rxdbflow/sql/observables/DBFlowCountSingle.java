package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Given a RxDBFlow query, creates an observable which emits a single count of results in the database table/view.
 * In most cases this observable should be paired with
 * {@link au.com.roadhouse.rxdbflow.sql.language.RxSQLite#selectCountOf(IProperty[])}
 */
public class DBFlowCountSingle<TModel> extends DBFlowBaseSingle<TModel, Long>  {

    private final BaseQueriable mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;

    /**
     * Creates a new DBFlowCountSingle. Generally this constructor is not used directly, but used
     * as part of a query statement. {@see au.com.roadhouse.rxdbflow.sql.language.RxSQLite}
     * @param clazz The class of the table/view the query effects.
     * @param baseModelQueriable The query to run when computing the count
     * @param databaseWrapper The database wrapper that the target table/view belongs too.
     */
    public DBFlowCountSingle(@NonNull Class<TModel> clazz, final BaseQueriable baseModelQueriable,
                             @Nullable DatabaseWrapper databaseWrapper) {
        super(clazz);
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }


    @Override
    public Long run() {
        if(mDatabaseWrapper != null){
            return mBaseModelQueriable.count(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.count();
        }
    }
}
