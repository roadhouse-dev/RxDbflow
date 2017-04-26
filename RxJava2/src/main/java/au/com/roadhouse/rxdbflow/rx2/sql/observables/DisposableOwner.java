package au.com.roadhouse.rxdbflow.rx2.sql.observables;

public interface DisposableOwner {
    void remove(ObserverDisposable connectionDisposable);
}
