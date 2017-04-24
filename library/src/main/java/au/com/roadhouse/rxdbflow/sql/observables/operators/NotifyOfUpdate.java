package au.com.roadhouse.rxdbflow.sql.observables.operators;

import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.structure.BaseModel;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowBaseCompletable;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.HasUpstreamCompletableSource;


public class NotifyOfUpdate<ModelType> extends DBFlowBaseCompletable<ModelType> implements HasUpstreamCompletableSource {

    private final Completable mSource;
    private final String mQuery;

    public NotifyOfUpdate(Completable source, String query, Class<ModelType> clazz) {
        super(clazz);
        mSource = source;
        mQuery = query;
    }

    @Override
    public CompletableSource source() {
        return mSource;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        mSource.subscribe(new CompletableObservable(observer, mQuery, getPrimaryModelClass()));
        super.subscribeActual(observer);
    }

    @Override
    public void run() {
        if(mQuery.toLowerCase().contains("delete ")){
            SqlUtils.notifyModelChanged(getPrimaryModelClass(), BaseModel.Action.DELETE, null);
        } else if(mQuery.toLowerCase().contains("update ")){
            SqlUtils.notifyModelChanged(getPrimaryModelClass(), BaseModel.Action.UPDATE, null);
        }
    }

    private class CompletableObservable implements CompletableObserver, Disposable {

        private final CompletableObserver mActual;
        private final String mQuery;
        private final Class mModelClazz;
        private boolean mIsDisposed = false;

        public CompletableObservable(CompletableObserver observer, String query, Class modelClazz) {
            mActual = observer;
            mQuery = query;
            mModelClazz = modelClazz;
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
            if(mQuery.toLowerCase().contains("delete ")){
                SqlUtils.notifyModelChanged(mModelClazz, BaseModel.Action.DELETE, null);
            } else if(mQuery.toLowerCase().contains("update ")){
                SqlUtils.notifyModelChanged(mModelClazz, BaseModel.Action.UPDATE, null);
            }

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
