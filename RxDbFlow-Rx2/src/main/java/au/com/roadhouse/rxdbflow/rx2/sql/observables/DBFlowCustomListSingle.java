package au.com.roadhouse.rxdbflow.rx2.sql.observables;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

import java.util.List;

/**
 * Given a RxSQLite query, emits the results from the query as a List of custom models.
 */

public class DBFlowCustomListSingle<TQueryModel extends BaseQueryModel, TModel> extends DBFlowBaseSingle<TModel, List<TQueryModel>> {

    private final Class<TQueryModel> mQueryModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;

    /**
     * Creates a new observable which runs a query and emits the result as a CustomList

     * @param baseModelQueriable The query to run
     */
    public DBFlowCustomListSingle(Class<TQueryModel> queryModelClazz, Class<TModel> modelClass, ModelQueriable<TModel> baseModelQueriable) {
        super(modelClass);
        mBaseModelQueriable = baseModelQueriable;
        mQueryModelClazz = queryModelClazz;
    }

    @Override
    public List<TQueryModel> run() {
        return mBaseModelQueriable.queryCustomList(mQueryModelClazz);
    }
}
