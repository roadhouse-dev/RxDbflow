package au.com.roadhouse.rxdbflow.sql.observables;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;

import java.util.ArrayList;

import io.reactivex.Observer;

/**
 * Given a RxSQLite query, emits the results from the query as a FlowCursorList.
 */
public class DBFlowCursorListObservable<TModel> extends DBFlowObservable<FlowCursorList<TModel>> {
    private final Class mModelClazz;
    private final ModelQueriable<TModel> mQueriable;
    private ArrayList<ObserverDisposable> mObserverDisposables = new ArrayList<>();

    /**
     * Creates a new observable which runs a query and emits the result as a Cursor
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param queriable The query to run
     */
    public DBFlowCursorListObservable(Class clazz, ModelQueriable queriable) {
        mModelClazz = clazz;
        mQueriable = queriable;
    }

    @Override
    protected void subscribeActual(Observer<? super FlowCursorList<TModel>> observer) {
        ObserverDisposable<? super FlowCursorList<TModel>> disposable = new ObserverDisposable<>(this, observer);
        mObserverDisposables.add(disposable);
        disposable.onNext(run());
    }

    @Override
    protected Class getPrimaryModelClass() {
        return mModelClazz;
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        mObserverDisposables.remove(connectionDisposable);
    }

    @Override
    public FlowCursorList<TModel> run() {
        return mQueriable.cursorList();
    }
}
