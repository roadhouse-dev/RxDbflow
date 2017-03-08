package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import io.reactivex.Observer;

public class DBFlowListObservable<TModel> extends DBFlowObservable<List<TModel>> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final DatabaseWrapper mDatabaseWrapper;

    public DBFlowListObservable(Class<TModel> clazz, ModelQueriable<TModel> baseModelQueriable,
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
    public List<TModel> run() {
        if (mDatabaseWrapper != null) {
            return mBaseModelQueriable.queryList(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.queryList();
        }
    }

    @Override
    protected Class getPrimaryModelClass() {
        return null;
    }

    @Override
    protected void subscribeActual(Observer<? super List<TModel>> observer) {
        ObserverDisposable<? super List<TModel>> disposable = new ObserverDisposable<>(this, observer);
        disposable.onNext(run());
    }
}