package au.com.roadhouse.rxdbflow.sql.observables.operators;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Arrays;
import java.util.List;

import au.com.roadhouse.rxdbflow.sql.observables.functions.ValueAction;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;


public class DBFlowRestartOnChange<T> extends Observable<T> implements HasUpstreamObservableSource<T> {
    private final ObservableSource<T> mSource;
    private final Class[] mSubscribedClasses;
    private final ValueAction<T> mRestartAction;

    public DBFlowRestartOnChange(Observable<T> source, Class[] subscribedClasses, ValueAction<T> restartAction) {
        mSource = source;
        mSubscribedClasses = subscribedClasses;
        mRestartAction = restartAction;
    }


    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        final RestartOnChangeObserver<T> disposable = new RestartOnChangeObserver<T>(observer, mSubscribedClasses, mRestartAction);
        mSource.subscribe(disposable);
        observer.onSubscribe(disposable);
    }

    @Override
    public ObservableSource<T> source() {
        return mSource;
    }

    private class RestartOnChangeObserver<T> implements Observer<T>, Disposable {
        private final Observer<? super T> mActual;
        private final ValueAction<T> mRestartAction;
        private List<Class> mSubscribedClasses;
        private boolean mIsDisposed = false;
        private FlowContentObserver mFlowContentObserver = new FlowContentObserver();

        public RestartOnChangeObserver(Observer<? super T> observer, Class[] subscribedClasses, ValueAction<T> action) {
            mActual = observer;
            mSubscribedClasses = Arrays.asList(subscribedClasses);
            mRestartAction = action;
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

            for (int i = 0; i < mSubscribedClasses.size(); i++) {
                mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mSubscribedClasses.get(i));
            }

            mFlowContentObserver.addOnTableChangedListener(
                    new FlowContentObserver.OnTableChangedListener() {
                        @Override
                        public void onTableChanged(@Nullable Class<?> tableChanged, BaseModel.Action action) {
                            if (!isDisposed()) {
                                mActual.onNext(mRestartAction.run());
                            }
                        }
                    });
        }

        @Override
        public void dispose() {
            mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
            mIsDisposed = true;
        }

        @Override
        public boolean isDisposed() {
            return mIsDisposed;
        }
    }
}
