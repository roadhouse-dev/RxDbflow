package au.com.roadhouse.rxdbflow.sql.observables;

import io.reactivex.CompletableObserver;


public abstract class DBFlowBaseCompletable<ModelType> extends DBFlowCompletable  {

    private final Class<ModelType> mModelClazz;

    public DBFlowBaseCompletable(Class<ModelType> clazz) {
        mModelClazz = clazz;
    }


    protected Class<ModelType> getPrimaryModelClass() {
        return mModelClazz;
    }

    protected void subscribeActual(CompletableObserver observer) {
        run();
        observer.onComplete();
    }
}
