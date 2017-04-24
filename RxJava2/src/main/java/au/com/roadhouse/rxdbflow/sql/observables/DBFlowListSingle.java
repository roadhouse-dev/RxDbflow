package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

public class DBFlowListSingle<TModel> extends DBFlowBaseSingle<TModel, List<TModel>> {

    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;

    public DBFlowListSingle(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable,
                            @Nullable DatabaseWrapper databaseWrapper) {
        super(clazz);
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }


    @Override
    public List<TModel> run() {
        if (mDatabaseWrapper != null) {
            return mBaseModelQueriable.queryList(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.queryList();
        }
    }
}