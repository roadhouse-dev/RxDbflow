package au.com.roadhouse.rxdbflow.sql.observables;


import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;

import io.reactivex.Observer;

public class DBFlowResultObservable<TModel> extends DBFlowObservable<CursorResult<TModel>>{
    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;

    public DBFlowResultObservable(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        //Nothing to do here
    }

    @Override
    public CursorResult<TModel> run() {
        return mBaseModelQueriable.queryResults();
    }

    @Override
    protected Class getPrimaryModelClass() {
        return null;
    }

    @Override
    protected void subscribeActual(Observer<? super CursorResult<TModel>> observer) {
        ObserverDisposable<? super CursorResult<TModel>> disposable = new ObserverDisposable<>(this, observer);
        disposable.onNext(run());
    }
}