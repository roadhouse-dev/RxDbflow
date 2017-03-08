package au.com.roadhouse.rxdbflow.structure;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import io.reactivex.Observable;


/**
 * Provides a common interface for any
 * @param <TModel>
 */
public interface RxModifications<TModel extends RxBaseModel> {

    Observable<TModel> saveAsObservable();

    Observable<TModel> saveAsObservable(@Nullable DatabaseWrapper databaseWrapper);

    Observable<TModel> insertAsObservable();

    Observable<TModel> insertAsObservable(@Nullable DatabaseWrapper databaseWrapper);

    Observable<TModel> deleteAsObservable();

    Observable<TModel> deleteAsObservable(@Nullable DatabaseWrapper databaseWrapper);

    Observable<TModel> updateAsObservable();

    Observable<TModel> updateAsObservable(@Nullable DatabaseWrapper databaseWrapper);
}
