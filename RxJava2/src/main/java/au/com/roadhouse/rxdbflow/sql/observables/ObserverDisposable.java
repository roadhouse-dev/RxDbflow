package au.com.roadhouse.rxdbflow.sql.observables;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class ObserverDisposable<T> implements Disposable {

    private final Observer<T> mObserver;
    private final DisposableOwner mParent;

    public ObserverDisposable(DisposableOwner parent, Observer<T> observer) {
        mParent = parent;
        mObserver = observer;
    }

    private boolean mIsSelfDisposed = false;

    public boolean isDisposed() {
        return mIsSelfDisposed;
    }

    public void dispose() {
        mIsSelfDisposed = true;
        mParent.remove(this);
    }

    public void onError(Throwable e) {
        mObserver.onError(e);
    }

    public void onNext(T data) {
        mObserver.onNext(data);
    }

    public void onComplete() {
        mObserver.onComplete();
        dispose();
    }
}
