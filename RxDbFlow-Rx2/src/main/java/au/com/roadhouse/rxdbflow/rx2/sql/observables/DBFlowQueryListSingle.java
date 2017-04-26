package au.com.roadhouse.rxdbflow.rx2.sql.observables;


import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;

public class DBFlowQueryListSingle<TModel> extends DBFlowBaseSingle<TModel, FlowQueryList<TModel>>{

    private final ModelQueriable<TModel> mBaseModelQueriable;

    public DBFlowQueryListSingle(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
        super(clazz);
        mBaseModelQueriable = baseModelQueriable;
    }

    @Override
    public FlowQueryList<TModel> run() {
        return mBaseModelQueriable.flowQueryList();
    }

}