package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import rx.Observable;

public interface ModelQueriableObservable<TModel extends Model> extends QueriableObservable {
    Observable<TModel> asSingleObservable(boolean subscribeToChanges);
    Observable<TModel> asSingleObservable(boolean subscribeToChanges, DatabaseWrapper databaseWrapper);
    Observable<List<TModel>> asListObservable();
    Observable<List<TModel>> asListObservable(DatabaseWrapper databaseWrapper);
    Observable<CursorResult<TModel>> asResultsObservable();
    Observable<FlowQueryList<TModel>> asQueryListObservable();
    Observable<FlowCursorList<TModel>> asCursorListObservable();
    <AQueryModel extends BaseQueryModel>Observable<AQueryModel> asCustomSingleObservable(Class<AQueryModel> customClazz);
    <AQueryModel extends BaseQueryModel>Observable<List<AQueryModel>> asCustomListObservable(Class<AQueryModel> customClazz);
}
