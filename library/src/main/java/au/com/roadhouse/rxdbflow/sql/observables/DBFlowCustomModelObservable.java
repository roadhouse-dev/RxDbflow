package au.com.roadhouse.rxdbflow.sql.observables;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

import io.reactivex.Observer;

/**
 * Given a RxSQLite query, emits the the first element from the query as a custom model.
 */
public class DBFlowCustomModelObservable<TQueryModel extends BaseQueryModel, TModel> extends DBFlowObservable<TQueryModel> {

    private final Class<TQueryModel> mQueryModelClass;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final Class<TModel> mMainClazz;

    /**
     * Creates a new observable which runs a query and emits the first element in the result set as a CustomModel
     *
     * @param clazz              The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowCustomModelObservable(Class<TQueryModel> clazz, Class<TModel> queryClazz, ModelQueriable<TModel> baseModelQueriable) {
        mQueryModelClass = clazz;
        mMainClazz = queryClazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        //Nothing to do here
    }

    @Override
    public TQueryModel run() {
        return mBaseModelQueriable.queryCustomSingle(mQueryModelClass);
    }

    @Override
    protected Class getPrimaryModelClass() {
        return mMainClazz;
    }

    @Override
    protected void subscribeActual(Observer<? super TQueryModel> observer) {
        ObserverDisposable<? super TQueryModel> disposable = new ObserverDisposable<>(this, observer);
        disposable.onNext(run());
    }
}