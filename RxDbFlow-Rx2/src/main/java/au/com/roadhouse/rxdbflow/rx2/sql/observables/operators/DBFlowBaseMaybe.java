package au.com.roadhouse.rxdbflow.rx2.sql.observables.operators;

import au.com.roadhouse.rxdbflow.rx2.sql.observables.DBFlowMaybe;
import io.reactivex.MaybeObserver;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;

public abstract class DBFlowBaseMaybe<ModelType, Container> extends DBFlowMaybe<Container> {

    private final Class<ModelType> mModelClazz;

    public DBFlowBaseMaybe(Class<ModelType> clazz) {
        mModelClazz = clazz;
    }

    @Override
    protected Class getPrimaryModelClass() {
        return mModelClazz;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super Container> observer) {

        observer.onSubscribe(EmptyDisposable.INSTANCE);
        try {
            Container v = run();
            observer.onSuccess(v);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            observer.onError(e);
        }
    }
}
