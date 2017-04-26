package au.com.roadhouse.rxdbflow.rx1.structure;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.rx1.RxDbFlow;
import rx.Observable;

/**
 * A DBFlow BaseModel implementation which provides observables for saving, updating, inserting, and
 * deleting operations.
 */
@SuppressWarnings("unchecked")
public class RxBaseModel<M extends RxBaseModel> extends BaseModel implements RxModifications<M> {

    private RxModelAdapter<? extends RxBaseModel> mModelAdapter;

    /**
     * Returns an observable for saving the object in the database
     * @return An observable
     */
    public Observable<M> saveAsObservable(){
        return getRxModelAdapter().saveAsObservable(this);
    }

    /**
     * Returns an observable for saving the object in the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    @SuppressWarnings("unchecked")
    public Observable<M> saveAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().saveAsObservable(this, databaseWrapper);
    }

    /**
     * Returns an observable for inserting the object in the database
     * @return An observable
     */
    public Observable<M> insertAsObservable(){
        return getRxModelAdapter().insertAsObservable(this);
    }

    /**
     * Returns an observable for inserting the object in the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> insertAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().insertAsObservable(this, databaseWrapper);
    }

    /**
     * Returns an observable for deleting the object from the database
     * @return An observable
     */
    public Observable<M> deleteAsObservable(){
        return getRxModelAdapter().deleteAsObservable(this);
    }

    /**
     * Returns an observable for deleting the object from the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> deleteAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().deleteAsObservable(this, databaseWrapper);
    }

    /**
     * Returns an observable for update the object in the database
     * @return An observable
     */
    public Observable<M> updateAsObservable(){
        return getRxModelAdapter().updateAsObservable(this);
    }

    /**
     * Returns an observable for update in object from the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> updateAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().updateAsObservable(this, databaseWrapper);
    }

    /**
     * Returns an observable which will refresh the model's data based on the primary key when subscribed.
     * @return An observable
     */
    public Observable<M> loadAsObservable(){
        return getRxModelAdapter().loadAsObservable(this);
    }

    /**
     * Returns an observable which will refresh the model's data based on the primary key when subscribed.
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> loadAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().loadAsObservable(this, databaseWrapper);
    }

    /**
     * @return The associated {@link RxModelAdapter}. The {@link RxDbFlow}
     * may throw a {@link InvalidDBConfiguration} for this call if this class
     * is not associated with a table, so be careful when using this method.
     */
    public RxModelAdapter getRxModelAdapter() {
        if (mModelAdapter == null) {
            mModelAdapter = RxDbFlow.getModelAdapter(getClass());
        }
        return mModelAdapter;
    }
}


