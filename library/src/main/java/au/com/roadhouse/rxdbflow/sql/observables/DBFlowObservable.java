package au.com.roadhouse.rxdbflow.sql.observables;

import au.com.roadhouse.rxdbflow.sql.observables.functions.ValueAction;
import au.com.roadhouse.rxdbflow.sql.observables.operators.CompleteOnResult;
import au.com.roadhouse.rxdbflow.sql.observables.operators.DBFlowRestartOnChange;
import io.reactivex.Observable;

/**
 * A base observable which provides DBFlow specific operators
 * @param <T> The observable result type
 */
public abstract class DBFlowObservable<T> extends Observable<T> implements ValueAction<T>, DisposableOwner{

    /**
     * Observes changes on the current table, restarting the query on change and emits the new count
     * to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public final Observable<T> restartOnChange(){
        return new DBFlowRestartOnChange<>(this, new Class[]{getPrimaryModelClass()}, this);
    }

    /**
     * Observes changes for specific tables, restarting the query on change and emits the new count
     * to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    public final Observable<T> restartOnChange(Class... tableToListen){
        return new DBFlowRestartOnChange<>(this, tableToListen, this);
    }

    /**
     * Returns the class for the primary table model effected by this query
     * @return The class for the primary table model
     */
    protected abstract Class getPrimaryModelClass();

    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange()} or {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public final Observable<T> completeOnResult(){
        return new CompleteOnResult<>(this);
    }

}