package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import rx.Observable;

public interface ModelQueriableObservable<TModel> extends QueriableObservable {

    /**
     * Creates an observable that emits a single model from a query. This will be the first record
     * returned by the query.
     * @return An observable that emits a single model
     */
    Observable<TModel> asSingleObservable();

    /**
     * Creates an observable that emits a single model from a query. This will be the first record
     * returned by the query.
     * @param databaseWrapper The database wrapper from which to run the query
     * @return An observable that emits a single model
     */
    Observable<TModel> asSingleObservable(DatabaseWrapper databaseWrapper);

    /**
     * Creates an observable that emits a list of models from a query.
     * @return An observable that emits a list of model
     */
    Observable<List<TModel>> asListObservable();

    /**
     * Creates an observable that emits a list of models from a query.
     *
     * @param databaseWrapper The database wrapper from which to run the query
     * @return An observable that emits a list of model
     */
    Observable<List<TModel>> asListObservable(DatabaseWrapper databaseWrapper);

    /**
     * Creates an observable that emits the results of a query as a CursorResult.
     *
     * @return An observable that emits a CursorResult
     */
    Observable<CursorResult<TModel>> asResultsObservable();

    /**
     * Creates an observable that emits the results of a query as a FlowQueryList.
     *
     * @return An observable that emits a FlowQueryList
     */
    Observable<FlowQueryList<TModel>> asQueryListObservable();

    /**
     * Creates an observable that emits the results of a query as a FlowCursorList.
     *
     * @return An observable that emits a FlowCursorList
     */
    Observable<FlowCursorList<TModel>> asCursorListObservable();

    /**
     * Creates an observable that emits a single custom QueryModel class. This will be the first record
     * returned by the query.
     *
     * @return An observable that emits a  single custom QueryModel class.
     */
    <AQueryModel extends BaseQueryModel>Observable<AQueryModel> asCustomSingleObservable(Class<AQueryModel> customClazz);

    /**
     * Creates an observable that emits the results of a query as a list of custom QueryModel objects.
     *
     * @return An observable that emits a  single custom QueryModel class.
     */
    <AQueryModel extends BaseQueryModel>Observable<List<AQueryModel>> asCustomListObservable(Class<AQueryModel> customClazz);
}
