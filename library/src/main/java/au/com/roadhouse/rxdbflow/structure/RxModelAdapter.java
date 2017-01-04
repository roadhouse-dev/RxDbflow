package au.com.roadhouse.rxdbflow.structure;

import android.content.ContentValues;
import android.support.annotation.IntRange;

import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.Collection;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func0;

public class RxModelAdapter<TModel> implements InternalAdapter<TModel> {

    private ModelAdapter<TModel> mModelAdapter;

    /**
     * Creates a new RxModelAdapter from an standard ModelAdapter
     * @param modelAdapter The model adapter to create the RxModelAdapter from
     */
    public RxModelAdapter(ModelAdapter<TModel> modelAdapter) {
        mModelAdapter = modelAdapter;
    }

    @Override
    public String getTableName() {
        return mModelAdapter.getTableName();
    }

    @Override
    public void save(TModel tModel) {
        mModelAdapter.save(tModel);
    }

    @Override
    public void save(TModel tModel, DatabaseWrapper databaseWrapper) {
        mModelAdapter.save(tModel, databaseWrapper);
    }

    @Override
    public void saveAll(Collection<TModel> tModels) {
        mModelAdapter.saveAll(tModels);
    }

    @Override
    public void saveAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.saveAll(tModels, databaseWrapper);
    }

    @Override
    public long insert(TModel tModel) {
        return mModelAdapter.insert(tModel);
    }

    @Override
    public long insert(TModel tModel, DatabaseWrapper databaseWrapper) {
        return mModelAdapter.insert(tModel, databaseWrapper);
    }

    @Override
    public void insertAll(Collection<TModel> tModels) {
        mModelAdapter.insertAll(tModels);
    }

    @Override
    public void insertAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.insertAll(tModels, databaseWrapper);
    }

    @Override
    public void update(TModel tModel) {
        mModelAdapter.update(tModel);
    }

    @Override
    public void update(TModel tModel, DatabaseWrapper databaseWrapper) {
        mModelAdapter.update(tModel, databaseWrapper);
    }

    @Override
    public void updateAll(Collection<TModel> tModels) {
        mModelAdapter.updateAll(tModels);
    }

    @Override
    public void updateAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.updateAll(tModels, databaseWrapper);
    }

    @Override
    public void delete(TModel tModel) {
        mModelAdapter.delete(tModel);
    }

    @Override
    public void delete(TModel tModel, DatabaseWrapper databaseWrapper) {
        mModelAdapter.delete(tModel, databaseWrapper);
    }

    @Override
    public void deleteAll(Collection<TModel> tModels) {
        mModelAdapter.deleteAll(tModels);
    }

    @Override
    public void deleteAll(Collection<TModel> tModels, DatabaseWrapper databaseWrapper) {
        mModelAdapter.deleteAll(tModels, databaseWrapper);
    }

    @Override
    public void bindToStatement(DatabaseStatement sqLiteStatement, TModel tModel) {
        mModelAdapter.bindToStatement(sqLiteStatement, tModel);
    }

    @Override
    public void bindToInsertStatement(DatabaseStatement sqLiteStatement, TModel tModel, @IntRange(from = 0L, to = 1L) int start) {
        mModelAdapter.bindToInsertStatement(sqLiteStatement, tModel, start);
    }

    @Override
    public void bindToInsertStatement(DatabaseStatement sqLiteStatement, TModel tModel) {
        mModelAdapter.bindToInsertStatement(sqLiteStatement, tModel);
    }

    @Override
    public void bindToContentValues(ContentValues contentValues, TModel tModel) {
        mModelAdapter.bindToContentValues(contentValues, tModel);
    }

    @Override
    public void bindToInsertValues(ContentValues contentValues, TModel tModel) {
        mModelAdapter.bindToInsertValues(contentValues, tModel);
    }

    @Override
    public void updateAutoIncrement(TModel tModel, Number id) {
        mModelAdapter.updateAutoIncrement(tModel, id);
    }

    @Override
    public Number getAutoIncrementingId(TModel tModel) {
        return mModelAdapter.getAutoIncrementingId(tModel);
    }

    public void load(TModel model) {
        mModelAdapter.load(model);
    }

    public void load(TModel model, DatabaseWrapper databaseWrapper) {
        mModelAdapter.load(model, databaseWrapper);
    }

    @Override
    public boolean cachingEnabled() {
        return mModelAdapter.cachingEnabled();
    }

    public Observable<TModel> saveAsObservable(final TModel model) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        save(model);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> saveAsObservable(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        save(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> insertAsObservable(final TModel model) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        insert(model);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> insertAsObservable(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        insert(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> deleteAsObservable(final TModel model) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        delete(model);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> deleteAsObservable(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        delete(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> updateAsObservable(final TModel model) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        update(model);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> updateAsObservable(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        update(model, databaseWrapper);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> loadAsObservable(final TModel model) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
                    @Override
                    public TModel call() throws Exception {
                        load(model);
                        return model;
                    }
                });
            }
        });
    }

    public Observable<TModel> loadAsObservable(final TModel model, final DatabaseWrapper databaseWrapper) {
        return Observable.defer(new Func0<Observable<TModel>>() {
            @Override
            public Observable<TModel> call() {
                return Observable.fromCallable(new Callable<TModel>() {
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

