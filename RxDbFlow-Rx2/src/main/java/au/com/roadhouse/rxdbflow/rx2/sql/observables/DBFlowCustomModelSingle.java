package au.com.roadhouse.rxdbflow.rx2.sql.observables;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

/**
 * Given a RxSQLite query, emits the the first element from the query as a custom model.
 */
public class DBFlowCustomModelSingle<TQueryModel extends BaseQueryModel, TModel> extends DBFlowBaseSingle<TModel, TQueryModel> {

    private final Class<TQueryModel> mQueryModelClass;
    private final ModelQueriable<TModel> mBaseModelQueriable;

    /**
     * Creates a new observable which runs a query and emits the first element in the result set as a CustomModel
     *
     * @param clazz              The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowCustomModelSingle(Class<TQueryModel> clazz, Class<TModel> queryClazz, ModelQueriable<TModel> baseModelQueriable) {
        super(queryClazz);
        mQueryModelClass = clazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    @Override
    public TQueryModel run() {
        return mBaseModelQueriable.queryCustomSingle(mQueryModelClass);
    }

}