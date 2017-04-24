package au.com.roadhouse.rxdbflow.sql.observables;

import au.com.roadhouse.rxdbflow.sql.observables.functions.ValueAction;
import au.com.roadhouse.rxdbflow.sql.observables.operators.DBFlowRestartOnChange;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * A base observable which provides DBFlow specific operators
 * @param <T> The observable result type
 */
public abstract class DBFlowSingle<T> extends Single<T> implements ValueAction<T> {

    /**
     * Observes changes on the current table, restarting the query on change and emits the new count
     * to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public final Observable<T> restartOnChange(){
        return new DBFlowRestartOnChange<>(this.toObservable(), new Class[]{getPrimaryModelClass()}, this);
    }

    /**
     * Observes changes for specific tables, restarting the query on change and emits the new count
     * to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    public final Observable<T> restartOnChange(Class... tableToListen){
        return new DBFlowRestartOnChange<>(this.toObservable(), tableToListen, this);
    }

    /**
     * Returns the class for the primary table model effected by this query
     * @return The class for the primary table model
     */
    protected abstract Class getPrimaryModelClass();

}