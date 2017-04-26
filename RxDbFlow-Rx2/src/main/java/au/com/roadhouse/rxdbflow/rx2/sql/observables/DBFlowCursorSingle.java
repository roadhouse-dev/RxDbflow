package au.com.roadhouse.rxdbflow.rx2.sql.observables;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Given a RxSQLite query, emits the results from the query as a FlowCursorList.
 */
public class DBFlowCursorSingle<TModel> extends DBFlowBaseSingle<TModel, Cursor> {

    private final Queriable mQueriable;
    private final DatabaseWrapper mDatabaseWrapper;

    /**
     * Creates a new observable which runs a query and emits the result as a Cursor
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param queriable The query to run
     */
    public DBFlowCursorSingle( Class<TModel> clazz, Queriable queriable, DatabaseWrapper databaseWrapper) {
        super(clazz);
        mQueriable = queriable;
        mDatabaseWrapper = databaseWrapper;
    }

    @Override
    public Cursor run() {
        if(mDatabaseWrapper != null){
            return mQueriable.query(mDatabaseWrapper);
        } else {
            return mQueriable.query();
        }
    }
}
