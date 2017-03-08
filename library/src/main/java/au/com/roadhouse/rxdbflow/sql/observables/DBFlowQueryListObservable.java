package au.com.roadhouse.rxdbflow.sql.observables;


import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;

import io.reactivex.Observer;

public class DBFlowQueryListObservable<TModel> extends DBFlowObservable<FlowQueryList<TModel>>{
    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;

    public DBFlowQueryListObservable(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        //Nothing to do here
    }

    @Override
    public FlowQueryList<TModel> run() {
        return mBaseModelQueriable.flowQueryList();
    }

    @Override
    protected Class getPrimaryModelClass() {
        return null;
    }

    @Override
    protected void subscribeActual(Observer<? super FlowQueryList<TModel>> observer) {
        ObserverDisposable<? super FlowQueryList<TModel>> disposable = new ObserverDisposable<>(this, observer);
        disposable.onNext(run());
    }
}