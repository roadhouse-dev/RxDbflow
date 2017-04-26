package au.com.roadhouse.rxdbflow.rx2.structure;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.rx2.RxDbFlow;
import io.reactivex.Single;

/**
 * A DBFlow BaseModel implementation which provides Singles for saving, updating, inserting, and
 * deleting operations.
 */
@SuppressWarnings("unchecked")
public class RxBaseModel<M extends RxBaseModel> extends BaseModel implements RxModifications<M> {

    private transient RxModelAdapter<? extends RxBaseModel> mModelAdapter;

    /**
     * Returns an Single for saving the object in the database
     * @return An Single
     */
    public Single<M> saveAsSingle(){
        return getRxModelAdapter().saveAsSingle(this);
    }

    /**
     * Returns an Single for saving the object in the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An Single
     */
    @SuppressWarnings("unchecked")
    public Single<M> saveAsSingle(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().saveAsSingle(this, databaseWrapper);
    }

    /**
     * Returns an Single for inserting the object in the database
     * @return An Single
     */
    public Single<M> insertAsSingle(){
        return getRxModelAdapter().insertAsSingle(this);
    }

    /**
     * Returns an Single for inserting the object in the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An Single
     */
    public Single<M> insertAsSingle(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().insertAsSingle(this, databaseWrapper);
    }

    /**
     * Returns an Single for deleting the object from the database
     * @return An Single
     */
    public Single<M> deleteAsSingle(){
        return getRxModelAdapter().deleteAsSingle(this);
    }

    /**
     * Returns an Single for deleting the object from the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An Single
     */
    public Single<M> deleteAsSingle(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().deleteAsSingle(this, databaseWrapper);
    }

    /**
     * Returns an Single for update the object in the database
     * @return An Single
     */
    public Single<M> updateAsSingle(){
        return getRxModelAdapter().updateAsSingle(this);
    }

    /**
     * Returns an Single for update in object from the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An Single
     */
    public Single<M> updateAsSingle(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().updateAsSingle(this, databaseWrapper);
    }

    /**
     * Returns an Single which will refresh the model's data based on the primary key when subscribed.
     * @return An Single
     */
    public Single<M> loadAsSingle(){
        return getRxModelAdapter().loadAsSingle(this);
    }

    /**
     * Returns an Single which will refresh the model's data based on the primary key when subscribed.
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An Single
     */
    public Single<M> loadAsSingle(@Nullable final DatabaseWrapper databaseWrapper){
        return getRxModelAdapter().loadAsSingle(this, databaseWrapper);
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


