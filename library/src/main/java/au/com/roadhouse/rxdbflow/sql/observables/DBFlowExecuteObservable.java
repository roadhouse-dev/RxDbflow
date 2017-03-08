package au.com.roadhouse.rxdbflow.sql.observables;


import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.sql.language.NullValue;
import au.com.roadhouse.rxdbflow.sql.observables.functions.ValueAction;
import au.com.roadhouse.rxdbflow.sql.observables.operators.NotifyOfUpdate;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Given an RxSQLite query, executes a statement without a result.
 */
public class DBFlowExecuteObservable extends Observable<NullValue> implements ValueAction<NullValue>, DisposableOwner {

    private final BaseQueriable mBaseQueriable;
    private final DatabaseWrapper mDatabaseWrapper;
    private final Class mModelClazz;

    /**
     * Creates a new Observable which executes a sql statement against a table
     * @param clazz The model class representing the table to execute against
     * @param modelQueriable The query to execute
     * @param databaseWrapper The database in which the target table resides
     */
    public DBFlowExecuteObservable(Class clazz, BaseQueriable modelQueriable, @Nullable DatabaseWrapper databaseWrapper) {
        mBaseQueriable = modelQueriable;
        mDatabaseWrapper = databaseWrapper;
        mModelClazz = clazz;
    }

    @Override
    protected void subscribeActual(Observer<? super NullValue> observer) {
        ObserverDisposable<? super NullValue> disposable = new ObserverDisposable<>(this, observer);
        disposable.onNext(run());
    }

    public Observable<NullValue> notifyOfUpdates(){
        return new NotifyOfUpdate<>(this, mBaseQueriable.getQuery(), mModelClazz);
    }

    @Override
    public NullValue run() {
        if(mDatabaseWrapper != null) {
                mBaseQueriable.execute(mDatabaseWrapper);
            } else {
                mBaseQueriable.execute();
            }

            return new NullValue();
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {

    }
}
