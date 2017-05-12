package au.com.roadhouse.rxdbflow.rx2.sql.observables;

import io.reactivex.SingleObserver;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;

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

        observer.onSubscribe(EmptyDisposable.INSTANCE);
        try {
            Container v =  run();
            if (v != null) {
                observer.onSuccess(v);
            } else {
                observer.onError(new NullPointerException("The callable returned a null value"));
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            observer.onError(e);
        }
    }
}
