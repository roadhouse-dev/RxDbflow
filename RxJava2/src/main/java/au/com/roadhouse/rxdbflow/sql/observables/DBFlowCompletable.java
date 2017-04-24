package au.com.roadhouse.rxdbflow.sql.observables;

import io.reactivex.Completable;

public abstract class DBFlowCompletable extends Completable {
    public abstract void run();
}
