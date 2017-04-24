package au.com.roadhouse.rxdbflow.sql.observables;

import io.reactivex.SingleObserver;

public abstract class DBFlowBaseSingle<ModelType, Container> extends DBFlowSingle<Container> {

    private final Class<ModelType> mModelClazz;

    public DBFlowBaseSingle(Class<ModelType> clazz) {
        mModelClazz = clazz;
    }

    @Override
    protected Class getPrimaryModelClass() {
        return mModelClazz;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super Container> observer) {
        observer.onSuccess(run());
    }
}
