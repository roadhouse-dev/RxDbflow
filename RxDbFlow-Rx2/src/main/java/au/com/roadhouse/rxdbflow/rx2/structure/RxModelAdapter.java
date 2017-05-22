package au.com.roadhouse.rxdbflow.rx2.structure;

import android.content.ContentValues;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.Collection;
import java.util.concurrent.Callable;

import io.reactivex.Single;


public class RxModelAdapter<TModel> implements InternalAdapter<TModel> {

    private ModelAdapter<TModel> mModelAdapter;

    /**
     * Creates a new RxModelAdapter from an standard ModelAdapter
     *
     * @param modelAdapter The model adapter to create the RxModelAdapter from
     */
    public RxModelAdapter(ModelAdapter<TModel> modelAdapter) {
        mModelAdapter = modelAdapter;
    }

    /**
     * Retrieves a RxModelAdapter instance for a model
     * @param clazz The class of the model
     * @param <TModel> The model type
     * @return A RxModelAdapter instane
     */
    public static <TModel> RxModelAdapter<TModel> getModelAdapter(Class<TModel> clazz) {
        return new RxModelAdapter<>(FlowManager.getModelAdapter(clazz));
    }

    /**
     * Retrieves the table name for the adapter
     * @return The table name
     */
    @Override
    public String getTableName() {
        return mModelAdapter.getTableName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(TModel tModel) {
        return mModelAdapter.save(tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(TModel tModel, DatabaseWrapper databaseWrapper) {
        return mModelAdapter.save(tModel, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Collection<TModel> tModels) {
         mModelAdapter.saveAll(tModels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.saveAll(tModels, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long insert(TModel tModel) {
        return mModelAdapter.insert(tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long insert(TModel tModel, DatabaseWrapper databaseWrapper) {
        return mModelAdapter.insert(tModel, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertAll(Collection<TModel> tModels) {
        mModelAdapter.insertAll(tModels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.insertAll(tModels, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(TModel tModel) {
        return mModelAdapter.update(tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(TModel tModel, DatabaseWrapper databaseWrapper) {
        return mModelAdapter.update(tModel, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAll(Collection<TModel> tModels) {
        mModelAdapter.updateAll(tModels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.updateAll(tModels, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(TModel tModel) {
        return mModelAdapter.delete(tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(TModel tModel, DatabaseWrapper databaseWrapper) {
        return mModelAdapter.delete(tModel, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(Collection<TModel> tModels) {
        mModelAdapter.deleteAll(tModels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.deleteAll(tModels, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToStatement(DatabaseStatement sqLiteStatement, TModel tModel) {
        mModelAdapter.bindToStatement(sqLiteStatement, tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToInsertStatement(DatabaseStatement sqLiteStatement, TModel tModel, @IntRange(from = 0L, to = 1L) int start) {
        mModelAdapter.bindToInsertStatement(sqLiteStatement, tModel, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToInsertStatement(DatabaseStatement sqLiteStatement, TModel tModel) {
        mModelAdapter.bindToInsertStatement(sqLiteStatement, tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToContentValues(ContentValues contentValues, TModel tModel) {
        mModelAdapter.bindToContentValues(contentValues, tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToInsertValues(ContentValues contentValues, TModel tModel) {
        mModelAdapter.bindToInsertValues(contentValues, tModel);
    }

    @Override
    public void bindToUpdateStatement(@NonNull DatabaseStatement updateStatement, @NonNull TModel tModel) {
        mModelAdapter.bindToUpdateStatement(updateStatement, tModel);
    }

    @Override
    public void bindToDeleteStatement(@NonNull DatabaseStatement deleteStatement, @NonNull TModel tModel) {
        mModelAdapter.bindToDeleteStatement(deleteStatement, tModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAutoIncrement(TModel tModel, Number id) {
        mModelAdapter.updateAutoIncrement(tModel, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getAutoIncrementingId(TModel tModel) {
        return mModelAdapter.getAutoIncrementingId(tModel);
    }

    /**
     * Loads or refreshes the data of a model based on the primary key
     * @param model The model to load or refresh
     */
    public void load(TModel model) {
        mModelAdapter.load(model);
    }

    /**
     * Loads or refreshes the data of a model based on the primary key
     * @param model The model to load or refresh
     * @param databaseWrapper The manually specified wrapper
     */
    public void load(TModel model, DatabaseWrapper databaseWrapper) {
        mModelAdapter.load(model, databaseWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cachingEnabled() {
        return mModelAdapter.cachingEnabled();
    }

    /**
     * Returns an Single for saving the object in the database
     * @return An Single
     */
    public Single<TModel> saveAsSingle(final TModel model) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        save(model);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for saving the object in the database
     * @return An Single
     */
    public Single<TModel> saveAsSingle(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        save(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for inserting the object in the database
     * @return An Single
     */
    public Single<TModel> insertAsSingle(final TModel model) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        insert(model);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for inserting the object in the database
     * @return An Single
     */
    public Single<TModel> insertAsSingle(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        insert(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for deleting the object from the database
     * @return An Single
     */
    public Single<TModel> deleteAsSingle(final TModel model) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        delete(model);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for deleting the object from the database
     * @return An Single
     */
    public Single<TModel> deleteAsSingle(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        delete(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for update the object in the database
     * @return An Single
     */
    public Single<TModel> updateAsSingle(final TModel model) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        update(model);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for update the object in the database
     * @return An Single
     */
    public Single<TModel> updateAsSingle(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        update(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single for update the object in the database
     * @return An Single
     */
    public Single<TModel> loadAsSingle(final TModel model) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        load(model);
                        return model;
                    }
                });
            }
        });
    }

    /**
     * Returns an Single which will refresh the model's data based on the primary key when subscribed.
     * @return An Single
     */
    public Single<TModel> loadAsSingle(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Single.defer(new Callable<Single<TModel>>() {
            @Override
            public Single<TModel> call() {
                return Single.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        load(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }
}

