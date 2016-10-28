package au.com.roadhouse.rxdbflow.structure;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import rx.Observable;
import rx.functions.Func0;

/**
 * A DBFlow BaseModel implementation which provides observables for saving, updating, inserting, and
 * deleting operations.
 */
public class RxBaseModel<M extends RxBaseModel> extends BaseModel {

    /**
     * Returns an observable for saving the object in the database
     * @return An observable
     */
    public Observable<M> saveAsObservable(){
        return saveAsObservable(null);
    }

    /**
     * Returns an observable for saving the object in the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> saveAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return Observable.defer(new Func0<Observable<M>>() {
            @Override
            public Observable<M> call() {
                return Observable.just(deferredSave(databaseWrapper));
            }
        });
    }

    /**
     * Returns an observable for inserting the object in the database
     * @return An observable
     */
    public Observable<M> insertAsObservable(){
        return insertAsObservable(null);
    }

    /**
     * Returns an observable for inserting the object in the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> insertAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return Observable.defer(new Func0<Observable<M>>() {
            @Override
            public Observable<M> call() {
                return Observable.just(deferredInsert(databaseWrapper));
            }
        });
    }

    /**
     * Returns an observable for deleting the object from the database
     * @return An observable
     */
    public Observable<M> deleteAsObservable(){
        return deleteAsObservable(null);
    }

    /**
     * Returns an observable for deleting the object from the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> deleteAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return Observable.defer(new Func0<Observable<M>>() {
            @Override
            public Observable<M> call() {
                return Observable.just(deferredDelete(databaseWrapper));
            }
        });
    }

    /**
     * Returns an observable for update the object in the database
     * @return An observable
     */
    public Observable<M> updateAsObservable(){
        return updateAsObservable(null);
    }

    /**
     * Returns an observable for update in object from the database
     * @param databaseWrapper The database wrapper for the database holding the table
     * @return An observable
     */
    public Observable<M> updateAsObservable(@Nullable final DatabaseWrapper databaseWrapper){
        return Observable.defer(new Func0<Observable<M>>() {
            @Override
            public Observable<M> call() {
                return Observable.just(deferredUpdate(databaseWrapper));
            }
        });
    }

    private M deferredInsert(DatabaseWrapper databaseWrapper){
        if(databaseWrapper != null){
            insert(databaseWrapper);
        } else {
            insert();
        }

        return (M) this;
    }

    private M deferredSave(DatabaseWrapper databaseWrapper){
        if(databaseWrapper != null){
            insert(databaseWrapper);
        } else {
            insert();
        }

        return (M) this;
    }

    private M deferredDelete(DatabaseWrapper databaseWrapper){
        if(databaseWrapper != null){
            delete(databaseWrapper);
        } else {
            delete();
        }

        return  (M) this;
    }

    private M deferredUpdate(DatabaseWrapper databaseWrapper){
        if(databaseWrapper != null){
            delete(databaseWrapper);
        } else {
            delete();
        }

        return  (M) this;
    }
}


