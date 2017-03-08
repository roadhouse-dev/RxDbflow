package au.com.roadhouse.rxdbflow.sql.observables.operators;

import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.structure.BaseModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;


public class NotifyOfUpdate<T> extends Observable<T> implements HasUpstreamObservableSource<T> {

    private final Observable<T> mSource;
    private final Class mModelClazz;
    private final String mQuery;

    public NotifyOfUpdate(Observable<T> source, String query, Class modelClass) {
        mSource = source;
        mQuery = query;
        mModelClazz = modelClass;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        mSource.subscribe(new NotifyOfUpdateObserver<T>(observer, mQuery, mModelClazz));
    }

    @Override
    public ObservableSource<T> source() {
        return null;
    }

    private class NotifyOfUpdateObserver<T> implements Observer<T>, Disposable {

        private final Observer<? super T> mActual;
        private final String mQuery;
        private final Class mModelClazz;
        private boolean mIsDisposed = false;

        public NotifyOfUpdateObserver(Observer<? super T> observer, String query, Class modelClazz) {
            mActual = observer;
            mQuery = query;
            mModelClazz = modelClazz;
        }

        @Override
        public void onSubscribe(Disposable d) {
            mActual.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            mActual.onNext(t);

            if(mQuery.toLowerCase().contains("delete ")){
                SqlUtils.notifyModelChanged(mModelClazz, BaseModel.Action.DELETE, null);
            } else if(mQuery.toLowerCase().contains("update ")){
                SqlUtils.notifyModelChanged(mModelClazz, BaseModel.Action.UPDATE, null);
            }
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
        public void dispose() {
            mIsDisposed = true;
        }

        @Override
        public boolean isDisposed() {
            return mIsDisposed;
        }
    }
}
