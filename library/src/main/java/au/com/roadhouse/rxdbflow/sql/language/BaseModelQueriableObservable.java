package au.com.roadhouse.rxdbflow.sql.language;

import android.database.Cursor;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.BaseModelQueriable;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCursorListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCursorObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCustomListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCustomModelObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowModelObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowQueryListObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowResultObservable;
import io.reactivex.Observable;


public class BaseModelQueriableObservable<TModel> extends BaseQueriableObservable<TModel> implements ModelQueriableObservable<TModel> {

    private ModelQueriable<TModel> mRealModelQueriable;

    protected BaseModelQueriableObservable(BaseModelQueriable<TModel> realModelQueriable) {
        super(realModelQueriable);
        mRealModelQueriable = realModelQueriable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<TModel> asSingleObservable() {
        return new DBFlowModelObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<TModel> asSingleObservable(DatabaseWrapper databaseWrapper) {
        return new DBFlowModelObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<List<TModel>> asListObservable() {
        return new DBFlowListObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<List<TModel>> asListObservable(DatabaseWrapper databaseWrapper) {
        return new DBFlowListObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<CursorResult<TModel>> asResultsObservable() {
        return new DBFlowResultObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<FlowQueryList<TModel>> asQueryListObservable() {
        return new DBFlowQueryListObservable<>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<FlowCursorList<TModel>> asCursorListObservable() {
        return new DBFlowCursorListObservable<TModel>(mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<Cursor> asQueryObservable() {
        return new DBFlowCursorObservable(mRealModelQueriable.getTable(), mRealModelQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <AQueryModel extends BaseQueryModel> Observable<AQueryModel> asCustomSingleObservable(Class<AQueryModel> customClazz) {
        return new DBFlowCustomModelObservable<>(customClazz, mRealModelQueriable.getTable(), mRealModelQueriable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <AQueryModel extends BaseQueryModel> Observable<List<AQueryModel>> asCustomListObservable(Class<AQueryModel> customClazz) {
        return new DBFlowCustomListObservable<>(customClazz,  mRealModelQueriable.getTable(), mRealModelQueriable);
    }

}
