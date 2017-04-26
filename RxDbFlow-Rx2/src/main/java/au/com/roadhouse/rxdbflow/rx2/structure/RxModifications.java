package au.com.roadhouse.rxdbflow.rx2.structure;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import io.reactivex.Single;


/**
 * Provides a common interface for any
 * @param <TModel>
 */
public interface RxModifications<TModel extends RxBaseModel> {

    Single<TModel> saveAsSingle();

    Single<TModel> saveAsSingle(@Nullable DatabaseWrapper databaseWrapper);

    Single<TModel> insertAsSingle();

    Single<TModel> insertAsSingle(@Nullable DatabaseWrapper databaseWrapper);

    Single<TModel> deleteAsSingle();

    Single<TModel> deleteAsSingle(@Nullable DatabaseWrapper databaseWrapper);

    Single<TModel> updateAsSingle();

    Single<TModel> updateAsSingle(@Nullable DatabaseWrapper databaseWrapper);
}
