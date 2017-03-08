package au.com.roadhouse.rxdbflow.sql.language;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCountObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowCursorObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowExecuteObservable;
import au.com.roadhouse.rxdbflow.sql.observables.DBFlowObservable;

public class BaseQueriableObservable<TModel> implements QueriableObservable {
    private BaseQueriable<TModel> mRealQueriable;

    public BaseQueriableObservable(BaseQueriable<TModel> queriable) {
        mRealQueriable = queriable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<Long> asCountObservable() {
        return new DBFlowCountObservable(mRealQueriable.getTable(), mRealQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<Long> asCountObservable(DatabaseWrapper databaseWrapper) {
        return new DBFlowCountObservable(mRealQueriable.getTable(), mRealQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowExecuteObservable asExecuteObservable() {
        return new DBFlowExecuteObservable(mRealQueriable.getTable(), mRealQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowExecuteObservable asExecuteObservable(DatabaseWrapper databaseWrapper) {
        return new DBFlowExecuteObservable(mRealQueriable.getTable(), mRealQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowObservable<Cursor> asQueryObservable() {
        return new DBFlowCursorObservable(mRealQueriable.getTable(), mRealQueriable, null);
    }

    public Queriable getRealQueriable() {
        return mRealQueriable;
    }

}
