package au.com.roadhouse.rxdbflow.rx2.sql.language;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowCountSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowCursorSingle;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowExecuteCompletable;
import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowSingle;

public class BaseQueriableObservable<TModel> implements QueriableObservable {
    private BaseQueriable<TModel> mRealQueriable;

    public BaseQueriableObservable(BaseQueriable<TModel> queriable) {
        mRealQueriable = queriable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowSingle<Long> asCountSingle() {
        return new DBFlowCountSingle(mRealQueriable.getTable(), mRealQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowSingle<Long> asCountSingle(DatabaseWrapper databaseWrapper) {
        return new DBFlowCountSingle(mRealQueriable.getTable(), mRealQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowExecuteCompletable asExecuteCompletable() {
        return new DBFlowExecuteCompletable<>(mRealQueriable.getTable(), mRealQueriable, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowExecuteCompletable asExecuteCompletable(DatabaseWrapper databaseWrapper) {
        return new DBFlowExecuteCompletable<>(mRealQueriable.getTable(), mRealQueriable, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBFlowSingle<Cursor> asQuerySingle() {
        return new DBFlowCursorSingle<>(mRealQueriable.getTable(), mRealQueriable, null);
    }

    public Queriable getRealQueriable() {
        return mRealQueriable;
    }

}
