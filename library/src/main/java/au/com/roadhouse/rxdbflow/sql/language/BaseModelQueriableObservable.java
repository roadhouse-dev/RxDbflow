package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.BaseModelQueriable;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCursorListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCursorObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCustomListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCustomModelObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowModelObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowQueryListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowResultObservable;
import rx.Observable;


public class BaseModelQueriableObservable<TModel extends Model> extends BaseQueriableObservable<TModel> implements ModelQueriableObservable<TModel> {

    private ModelQueriable<TModel> mRealModelQueriable;

    protected BaseModelQueriableObservable(BaseModelQueriable<TModel> realModelQueriable) {
        super(realModelQueriable);
        mRealModelQueriable = realModelQueriable;
    }

    @Override
    public Observable<TModel> asSingleObservable(boolean subscribeToChanges) {
        return new DBFlowModelObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, subscribeToChanges, null);
    }

    @Override
    public Observable<TModel> asSingleObservable(boolean subscribeToChanges, DatabaseWrapper databaseWrapper) {
        return new DBFlowModelObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, subscribeToChanges, databaseWrapper);
    }

    @Override
    public DBFlowListObservable<TModel> asListObservable() {
        return new DBFlowListObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, null);
    }

    @Override
    public DBFlowListObservable<TModel> asListObservable(DatabaseWrapper databaseWrapper) {
        return new DBFlowListObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, databaseWrapper);
    }

    @Override
    public Observable<CursorResult<TModel>> asResultsObservable() {
        return new DBFlowResultObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    @Override
    public Observable<FlowQueryList<TModel>> asQueryListObservable() {
        return new DBFlowQueryListObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    @Override
    public Observable<FlowCursorList<TModel>> asCursorListObservable() {
        return new DBFlowCursorListObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }


    @Override
    public DBFlowCursorObservable asQueryObservable() {
        return new DBFlowCursorObservable(mRealModelQueriable.getTable(), mRealModelQueriable, null);
    }

    @Override
    public <AQueryModel extends BaseQueryModel>Observable<AQueryModel> asCustomSingleObservable(Class<AQueryModel> customClazz) {
        return new DBFlowCustomModelObservable<>(customClazz, mRealModelQueriable);
    }

    @Override
    public <AQueryModel extends BaseQueryModel>Observable<List<AQueryModel>> asCustomListObservable(Class<AQueryModel> customClazz) {
        return new DBFlowCustomListObservable<>(customClazz, mRealModelQueriable);
    }

}
