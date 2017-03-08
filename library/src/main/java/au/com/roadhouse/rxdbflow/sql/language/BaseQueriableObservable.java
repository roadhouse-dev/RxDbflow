package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCursorObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowExecuteObservable;

public class BaseQueriableObservable<TModel> implements QueriableObservable {
    private BaseQueriable<TModel> mRealQueriable;

    public BaseQueriableObservable(BaseQueriable<TModel> queriable) {
        mRealQueriable = queriable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowCountObservable<TModel> asCountObservable() {
        return new DBFlowCountObservable<>(mRealQueriable.getTable(), mRealQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowCountObservable<TModel> asCountObservable(DatabaseWrapper databaseWrapper) {
        return new DBFlowCountObservable<>(mRealQueriable.getTable(), mRealQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowExecuteObservable<TModel> asExecuteObservable() {
        return new DBFlowExecuteObservable<>(mRealQueriable.getTable(), mRealQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowExecuteObservable<TModel> asExecuteObservable(DatabaseWrapper databaseWrapper) {
        return new DBFlowExecuteObservable<>(mRealQueriable.getTable(), mRealQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowCursorObservable asQueryObservable() {
        return new DBFlowCursorObservable(mRealQueriable.getTable(), mRealQueriable, null);
    }

    public Queriable getRealQueriable() {
        return mRealQueriable;
    }

}
