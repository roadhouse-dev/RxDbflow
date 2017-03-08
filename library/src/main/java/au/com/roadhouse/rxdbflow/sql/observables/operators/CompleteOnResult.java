package au.com.roadhouse.rxdbflow.sql.observables.operators;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * An observable operator which calls onComplete on it's subscribers
 */
public class CompleteOnResult<T> extends Observable<T> {
    private final ObservableSource<T> mSource;

    public CompleteOnResult(ObservableSource<T> source){
        mSource = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        mSource.subscribe(new CompleOnResultObserver<T>(observer));
    }

    private class CompleOnResultObserver<T> implements Observer<T>, Disposable{
        private final Observer<? super T> mActual;
        private boolean mIsDisposed = false;

        public CompleOnResultObserver(Observer<? super T> observer) {
            mActual = observer;
        }

        @Override
        public void onSubscribe(Disposable d) {
            mActual.onSubscribe(this);
        }

        @Override
        public void onError(Throwable e) {
            mActual.onError(e);
        }

        @Override
        public void onComplete() {
            mActual.onComplete();
        }

        @Override
        public void onNext(T o) {
            mActual.onNext(o);
            onComplete();
        }

        @Override
        public void dispose() {
            mIsDisposed = true;
        }

        @Override
        public boolean isDisposed() {
            return mIsDisposed;
        }
    }
}
