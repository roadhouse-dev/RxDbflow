package au.com.roadhouse.rxdbflow.rx2.sql.observables;


import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.rx2.sql.observables.operators.DBFlowBaseMaybe;

/**
 * Given a RxSQLite query, emits the the first element from the query results.
 */
public class DBFlowModelMaybe<TModel> extends DBFlowBaseMaybe<TModel, TModel> {
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;

    /**
     * Creates a new observable which runs a query and emits the first element in the result set as a CustomModel
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowModelMaybe(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable,
                            @Nullable DatabaseWrapper databaseWrapper) {
        super(clazz);
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }


    @Override
    public TModel run() {
        if(mDatabaseWrapper == null) {
            return mBaseModelQueriable.querySingle();
        } else {
            return mBaseModelQueriable.querySingle(mDatabaseWrapper);
        }
    }
}

