package au.com.roadhouse.rxdbflow.sql.observables;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

import java.util.List;

import io.reactivex.Observer;

/**
 * Given a RxSQLite query, emits the results from the query as a List of custom models.
 */

public class DBFlowCustomListObservable<TQueryModel extends BaseQueryModel, TModel> extends DBFlowObservable<List<TQueryModel>> {

    private final Class<TModel> mModelClazz;
    private final Class<TQueryModel> mQueryModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;

    /**
     * Creates a new observable which runs a query and emits the result as a CustomList
     *
     * @param modelClass              The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowCustomListObservable(Class<TQueryModel> queryModelClazz, Class<TModel> modelClass, ModelQueriable<TModel> baseModelQueriable) {
        mModelClazz = modelClass;
        mBaseModelQueriable = baseModelQueriable;
        mQueryModelClazz = queryModelClazz;
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        //Nothing to do here
    }

    @Override
    public List<TQueryModel> run() {
        return mBaseModelQueriable.queryCustomList(mQueryModelClazz);
    }

    @Override
    protected Class getPrimaryModelClass() {
        return mModelClazz;
    }

    @Override
    protected void subscribeActual(Observer<? super List<TQueryModel>> observer) {
        ObserverDisposable<? super List<TQueryModel>> disposable = new ObserverDisposable<>(this, observer);
        disposable.onNext(run());
    }
}
