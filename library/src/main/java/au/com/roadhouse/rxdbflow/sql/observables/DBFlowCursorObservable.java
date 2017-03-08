package au.com.roadhouse.rxdbflow.sql.observables;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;

import io.reactivex.Observer;

/**
 * Given a RxSQLite query, emits the results from the query as a FlowCursorList.
 */
public class DBFlowCursorObservable extends DBFlowObservable<Cursor> {
    private final Class mModelClazz;
    private final Queriable mQueriable;
    private final DatabaseWrapper mDatabaseWrapper;
    private ArrayList<ObserverDisposable> mObserverDisposables = new ArrayList<>();

    /**
     * Creates a new observable which runs a query and emits the result as a Cursor
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param queriable The query to run
     */
    public DBFlowCursorObservable( Class clazz, Queriable queriable, DatabaseWrapper databaseWrapper) {
        mModelClazz = clazz;
        mQueriable = queriable;
        mDatabaseWrapper = databaseWrapper;
    }

    @Override
    protected void subscribeActual(Observer<? super Cursor> observer) {
        ObserverDisposable<? super Cursor> disposable = new ObserverDisposable<>(this, observer);
        mObserverDisposables.add(disposable);
        disposable.onNext(run());
    }


    @Override
    protected Class getPrimaryModelClass() {
        return mModelClazz;
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        mObserverDisposables.remove(connectionDisposable);
    }

    @Override
    public Cursor run() {
        if(mDatabaseWrapper != null){
            return mQueriable.query(mDatabaseWrapper);
        } else {
            return mQueriable.query();
        }
    }
}
