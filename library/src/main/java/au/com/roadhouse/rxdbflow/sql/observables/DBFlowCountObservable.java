package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;

import au.com.roadhouse.rxdbflow.sql.observables.functions.ValueAction;
import au.com.roadhouse.rxdbflow.sql.observables.operators.CompleteOnResult;
import au.com.roadhouse.rxdbflow.sql.observables.operators.DBFlowRestartOnChange;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Given a RxDBFlow query, creates an observable which emits a single count of results in the database table/view.
 * In most cases this observable should be paired with
 * {@link au.com.roadhouse.rxdbflow.sql.language.RxSQLite#selectCountOf(IProperty[])}
 * @param <T> The table/view model in which the count is performed.
 */
public class DBFlowCountObservable extends DBFlowObservable<Long> {

    private final Class mModelClazz;
    private final BaseQueriable mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;
    private ArrayList<ObserverDisposable> mObserverDisposables = new ArrayList<>();

    /**
     * Creates a new DBFlowCountObservable. Generally this constructor is not used directly, but used
     * as part of a query statement. {@see au.com.roadhouse.rxdbflow.sql.language.RxSQLite}
     * @param clazz The class of the table/view the query effects.
     * @param baseModelQueriable The query to run when computing the count
     * @param databaseWrapper The database wrapper that the target table/view belongs too.
     */
    public DBFlowCountObservable(@NonNull Class clazz, final BaseQueriable baseModelQueriable,
                                 @Nullable DatabaseWrapper databaseWrapper) {
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }

    @Override
    protected void subscribeActual(Observer<? super Long> observer) {
        ObserverDisposable<? super Long> disposable = new ObserverDisposable<>(this, observer);
        mObserverDisposables.add(disposable);
        disposable.onNext(run());
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        mObserverDisposables.remove(connectionDisposable);
    }

    @Override
    public Long run() {
        if(mDatabaseWrapper != null){
            return mBaseModelQueriable.count(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.count();
        }
    }


    @Override
    protected Class getPrimaryModelClass() {
        return mModelClazz;
    }
}
