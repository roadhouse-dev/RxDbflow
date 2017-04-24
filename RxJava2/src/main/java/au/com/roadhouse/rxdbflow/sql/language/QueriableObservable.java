package au.com.roadhouse.rxdbflow.sql.language;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowExecuteCompletable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowSingle;

public interface QueriableObservable {

    /**
     * Creates an Single that emits a long value. This should be paired with {@link RxSQLite#selectCountOf(IProperty[])}
     * or a query that returns a single long/integer value. Calling this on any other query could result in unexpected return values
     *
     * @return An Single that emits a long result
     */
    DBFlowSingle<Long> asCountSingle();

    /**
     * Creates an Single that emits a long value. This should be paired with {@link RxSQLite#selectCountOf(IProperty[])}
     * or a query that returns a single long/integer value. Calling this on any other query could result in an unexpected emitted
     * value
     *
     * @param databaseWrapper The database wrapper to use for the query
     * @return An Single that emits a long result
     */
    DBFlowSingle<Long> asCountSingle(DatabaseWrapper databaseWrapper);

    /**
     * Creates an Single that executes a SQL statement. This is usually used with CRUD statements as
     * it only emits a Void value
     *
     * @return An Single that executes a SQL statement
     */
    DBFlowExecuteCompletable asExecuteCompletable();

    /**
     * Creates an Single that executes a SQL statement. This is usually used with CRUD statements as
     * it only emits a Void value
     *
     * @param databaseWrapper The database wrapper to use for the query
     * @return An Single that executes a SQL statement
     */
    DBFlowExecuteCompletable asExecuteCompletable(DatabaseWrapper databaseWrapper);

    /**
     * Creates an Single that emits a cursor containing the results of the query
     * @return An Single that emits a cursor
     */
    DBFlowSingle<Cursor> asQuerySingle();
}
