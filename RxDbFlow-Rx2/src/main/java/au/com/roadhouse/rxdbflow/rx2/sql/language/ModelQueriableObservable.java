package au.com.roadhouse.rxdbflow.rx2.sql.language;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowSingle;


public interface ModelQueriableObservable<TModel> extends QueriableObservable {

    /**
     * Creates an Single that emits a single model from a query. This will be the first record
     * returned by the query.
     * @return An Single that emits a single model
     */
    DBFlowSingle<TModel> asSingle();

    /**
     * Creates an Single that emits a single model from a query. This will be the first record
     * returned by the query.
     * @param databaseWrapper The database wrapper from which to run the query
     * @return An Single that emits a single model
     */
    DBFlowSingle<TModel> asSingle(DatabaseWrapper databaseWrapper);

    /**
     * Creates an Single that emits a list of models from a query.
     * @return An Single that emits a list of model
     */
    DBFlowSingle<List<TModel>> asListSingle();

    /**
     * Creates an Single that emits a list of models from a query.
     *
     * @param databaseWrapper The database wrapper from which to run the query
     * @return An Single that emits a list of model
     */
    DBFlowSingle<List<TModel>> asListSingle(DatabaseWrapper databaseWrapper);

    /**
     * Creates an Single that emits the results of a query as a CursorResult.
     *
     * @return An Single that emits a CursorResult
     */
    DBFlowSingle<CursorResult<TModel>> asResultsSingle();

    /**
     * Creates an Single that emits the results of a query as a FlowQueryList.
     *
     * @return An Single that emits a FlowQueryList
     */
    DBFlowSingle<FlowQueryList<TModel>> asQueryListSingle();

    /**
     * Creates an Single that emits the results of a query as a FlowCursorList.
     *
     * @return An Single that emits a FlowCursorList
     */
    DBFlowSingle<FlowCursorList<TModel>> asCursorListSingle();

    /**
     * Creates an Single that emits a single custom QueryModel class. This will be the first record
     * returned by the query.
     *
     * @return An Single that emits a  single custom QueryModel class.
     */
    <AQueryModel extends BaseQueryModel>DBFlowSingle<AQueryModel> asCustomSingle(Class<AQueryModel> customClazz);

    /**
     * Creates an observable that emits the results of a query as a list of custom QueryModel objects.
     *
     * @return An observable that emits a  single custom QueryModel class.
     */
    <AQueryModel extends BaseQueryModel>DBFlowSingle<List<AQueryModel>> asCustomListObservable(Class<AQueryModel> customClazz);
}
