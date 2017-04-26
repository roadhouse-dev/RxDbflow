package au.com.roadhouse.rxdbflow.rx2.sql.language;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.BaseModelQueriable;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowCursorListSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowCustomListSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowCustomModelSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowListSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowModelSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowQueryListSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowResultSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowSingle;


public class BaseModelQueriableObservable<TModel> extends BaseQueriableObservable<TModel> implements ModelQueriableObservable<TModel> {

    private ModelQueriable<TModel> mRealModelQueriable;

    protected BaseModelQueriableObservable(BaseModelQueriable<TModel> realModelQueriable) {
        super(realModelQueriable);
        mRealModelQueriable = realModelQueriable;
    }

    @Override
    public DBFlowSingle<TModel> asSingle() {
        return new DBFlowModelSingle<TModel>(mRealModelQueriable.getTable(), mRealModelQueriable, null);
    }

    @Override
    public DBFlowSingle<TModel> asSingle(DatabaseWrapper databaseWrapper) {
        return new DBFlowModelSingle<>(mRealModelQueriable.getTable(), mRealModelQueriable, databaseWrapper);
    }

    @Override
    public DBFlowSingle<List<TModel>> asListSingle() {
        return new DBFlowListSingle<>(mRealModelQueriable.getTable(), mRealModelQueriable, null);
    }

    @Override
    public DBFlowSingle<List<TModel>> asListSingle(DatabaseWrapper databaseWrapper) {
        return new DBFlowListSingle<>(mRealModelQueriable.getTable(), mRealModelQueriable, databaseWrapper);
    }

    @Override
    public DBFlowSingle<CursorResult<TModel>> asResultsSingle() {
        return new DBFlowResultSingle<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    @Override
    public DBFlowSingle<FlowQueryList<TModel>> asQueryListSingle() {
        return new DBFlowQueryListSingle<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    @Override
    public DBFlowSingle<FlowCursorList<TModel>> asCursorListSingle() {
        return new DBFlowCursorListSingle<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    @Override
    public <AQueryModel extends BaseQueryModel> DBFlowSingle<AQueryModel> asCustomSingle(Class<AQueryModel> customClazz) {
        return new DBFlowCustomModelSingle<>(customClazz, mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    @Override
    public <AQueryModel extends BaseQueryModel> DBFlowSingle<List<AQueryModel>> asCustomListObservable(Class<AQueryModel> customClazz) {
        return new DBFlowCustomListSingle<>(customClazz, mRealModelQueriable.getTable(), mRealModelQueriable);
    }
}
