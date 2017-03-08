package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
public class DBFlowCountObservable extends Observable<Long> implements DisposableOwner, ValueAction<Long> {

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
        super(new OnDBFlowSubscribeWithChanges<>(baseModelQueriable, databaseWrapper));
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

    /**
     * Observes changes on the current table, restarting the query on change and emits the new count
     * to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<Long> restartOnChange(){
        return new DBFlowRestartOnChange<>(this, new Class[]{mModelClazz}, this);
    }

    /**
     * Observes changes for specific tables, restarting the query on change and emits the new count
     * to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    public final Observable<Long> restartOnChange(Class... tableToListen){
        return new DBFlowRestartOnChange<>(this, tableToListen, this);
    }

    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange()} or {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public Observable<Long> completeOnResult(){
        return new CompleteOnResult<>(this);
    }
}
