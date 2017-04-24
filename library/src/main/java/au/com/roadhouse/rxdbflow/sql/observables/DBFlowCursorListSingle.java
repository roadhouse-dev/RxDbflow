package au.com.roadhouse.rxdbflow.sql.observables;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;

import java.util.ArrayList;

/**
 * Given a RxSQLite query, emits the results from the query as a FlowCursorList.
 */
public class DBFlowCursorListSingle<TModel> extends DBFlowBaseSingle<TModel, FlowCursorList<TModel>> {
    private final ModelQueriable<TModel> mQueriable;
    private ArrayList<ObserverDisposable> mObserverDisposables = new ArrayList<>();

    /**
     * Creates a new observable which runs a query and emits the result as a Cursor
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param queriable The query to run
     */
    public DBFlowCursorListSingle(Class<TModel> clazz, ModelQueriable<TModel> queriable) {
        super(clazz);
        mQueriable = queriable;
    }

    @Override
    public FlowCursorList<TModel> run() {
        return mQueriable.cursorList();
    }
}
