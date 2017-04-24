package au.com.roadhouse.rxdbflow.sql.observables;

public interface DisposableOwner {
    void remove(ObserverDisposable connectionDisposable);
}
