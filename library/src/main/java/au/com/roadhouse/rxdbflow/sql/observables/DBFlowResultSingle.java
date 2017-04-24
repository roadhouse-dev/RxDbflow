package au.com.roadhouse.rxdbflow.sql.observables;


import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;

public class DBFlowResultSingle<TModel> extends DBFlowBaseSingle<TModel, CursorResult<TModel>>{
    private final ModelQueriable<TModel> mBaseModelQueriable;

    public DBFlowResultSingle(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
        super(clazz);
        mBaseModelQueriable = baseModelQueriable;
    }

    @Override
    public CursorResult<TModel> run() {
        return mBaseModelQueriable.queryResults();
    }

}