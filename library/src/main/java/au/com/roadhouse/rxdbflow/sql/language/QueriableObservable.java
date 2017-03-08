package au.com.roadhouse.rxdbflow.sql.language;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowExecuteObservable;
import io.reactivex.Observable;

public interface QueriableObservable {

    /**
     * Creates an observable that emits a long value. This should be paired with {@link RxSQLite#selectCountOf(IProperty[])}
     * or a query that returns a single long/integer value. Calling this on any other query could result in unexpected return values
     *
     * @return An observable that emits a long result
     */
    Observable<Long> asCountObservable();

    /**
     * Creates an observable that emits a long value. This should be paired with {@link RxSQLite#selectCountOf(IProperty[])}
     * or a query that returns a single long/integer value. Calling this on any other query could result in an unexpected emitted
     * value
     *
     * @param databaseWrapper The database wrapper to use for the query
     * @return An observable that emits a long result
     */
    Observable<Long> asCountObservable(DatabaseWrapper databaseWrapper);

    /**
     * Creates an observable that executes a SQL statement. This is usually used with CRUD statements as
     * it only emits a Void value
     *
     * @return An observable that executes a SQL statement
     */
    DBFlowExecuteObservable asExecuteObservable();

    /**
     * Creates an observable that executes a SQL statement. This is usually used with CRUD statements as
     * it only emits a Void value
     *
     * @param databaseWrapper The database wrapper to use for the query
     * @return An observable that executes a SQL statement
     */
    Observable<Void> asExecuteObservable(DatabaseWrapper databaseWrapper);

    /**
     * Creates an observable that emits a cursor containing the results of the query
     * @return An observable that emits a cursor
     */
    Observable<Cursor> asQueryObservable();
}
