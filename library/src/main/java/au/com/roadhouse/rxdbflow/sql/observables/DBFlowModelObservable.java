package au.com.roadhouse.rxdbflow.sql.observables;


import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import io.reactivex.Observer;

/**
 * Given a RxSQLite query, emits the the first element from the query results.
 */
public class DBFlowModelObservable<TModel> extends DBFlowObservable<TModel> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;
    private List<Class> mSubscribedClasses;

    /**
     * Creates a new observable which runs a query and emits the first element in the result set as a CustomModel
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowModelObservable(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable,
                                 @Nullable DatabaseWrapper databaseWrapper) {
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }

    @Override
    public void remove(ObserverDisposable connectionDisposable) {
        //Nothing to do here
    }

    @Override
    public TModel run() {
        if(mDatabaseWrapper == null) {
            return mBaseModelQueriable.querySingle();
        } else {
            return mBaseModelQueriable.querySingle(mDatabaseWrapper);
        }
    }

    @Override
    protected Class getPrimaryModelClass() {
        return mModelClazz;
    }

    @Override
    protected void subscribeActual(Observer<? super TModel> observer) {
        ObserverDisposable<? super TModel> disposable = new ObserverDisposable<>(this, observer);
        disposable.onNext(run());
    }
}

