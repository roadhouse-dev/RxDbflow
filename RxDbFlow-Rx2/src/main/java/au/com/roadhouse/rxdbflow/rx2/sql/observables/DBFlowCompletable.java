package au.com.roadhouse.rxdbflow.rx2.sql.observables;

import io.reactivex.Completable;

public abstract class DBFlowCompletable extends Completable {
    public abstract void run();
}
