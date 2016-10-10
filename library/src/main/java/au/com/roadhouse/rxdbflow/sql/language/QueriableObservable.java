package au.com.roadhouse.rxdbflow.sql.language;

import android.database.Cursor;

import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowExecuteObservable;
import rx.Observable;

public interface QueriableObservable {
    Observable<Long> asCountObservable();
    Observable<Long> asCountObservable(DatabaseWrapper databaseWrapper);
    DBFlowExecuteObservable asExecuteObservable();
    Observable<Void> asExecuteObservable(DatabaseWrapper databaseWrapper);
    Observable<Cursor> asQueryObservable();
}
