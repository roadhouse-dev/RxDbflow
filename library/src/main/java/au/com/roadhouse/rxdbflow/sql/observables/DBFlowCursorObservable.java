package au.com.roadhouse.rxdbflow.sql.observables;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxSQLite query, emits the results from the query as a FlowCursorList.
 */
public class DBFlowCursorObservable extends Observable<Cursor> {
    private final Class<? extends Model> mModelClazz;
    private final Queriable mQueriable;
    private final DatabaseWrapper mDatabaseWrapper;
    private List<Class<? extends Model>> mSubscribedClasses;

    /**
     * Creates a new observable which runs a query and emits the result as a Cursor
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param queriable The query to run
     */
    public DBFlowCursorObservable(Class<? extends  Model> clazz, Queriable queriable, DatabaseWrapper databaseWrapper) {
        super(new OnDBFlowSubscribe(queriable, databaseWrapper));
        mModelClazz = clazz;
        mQueriable = queriable;
        mDatabaseWrapper = databaseWrapper;
        mSubscribedClasses = new ArrayList<>();
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the updated
     * query results to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<Cursor> restartOnChange(){
        mSubscribedClasses.add(mModelClazz);
        return lift(new DBFlowOnChangeOperator<>());
    }


    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange()} or {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public Observable<Cursor> completeOnResult(){
        return lift(new CompleteOnResultOperator<Cursor>());
    }


    /**
     * Observes changes on the current table, restarts the query on change, and emits the updated
     * query results to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    @SafeVarargs
    public final <TModel extends Model> Observable<Cursor> restartOnChange(Class<TModel>... tableToListen){
        Collections.addAll(mSubscribedClasses, tableToListen);
        return lift(new DBFlowOnChangeOperator<>());
    }

    private static class OnDBFlowSubscribe implements OnSubscribe<Cursor> {

        private final Queriable mQueriable;
        private final DatabaseWrapper mDatabaseWrapper;

        OnDBFlowSubscribe(Queriable queriable,  DatabaseWrapper databaseWrapper){
            mQueriable = queriable;
            mDatabaseWrapper = databaseWrapper;
        }

        @Override
        public void call(final Subscriber<? super Cursor> subscriber) {
            subscriber.onNext(runQuery());
        }

        private Cursor runQuery(){
            if(mDatabaseWrapper != null){
                return mQueriable.query(mDatabaseWrapper);
            } else {
                return mQueriable.query();
            }
        }
    }

    private Cursor runQuery(){
        if(mDatabaseWrapper != null){
            return mQueriable.query(mDatabaseWrapper);
        } else {
            return mQueriable.query();
        }
    }


    private class DBFlowOnChangeOperator<T extends Cursor> implements Observable.Operator<Cursor, Cursor> {

        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super Cursor> call(final Subscriber<? super Cursor> subscriber) {
            return new Subscriber<Cursor>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Cursor cursor) {
                    for (int i = 0; i < mSubscribedClasses.size(); i++) {
                        mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mSubscribedClasses.get(i));
                    }
                    mFlowContentObserver.addOnTableChangedListener(
                            new FlowContentObserver.OnTableChangedListener() {
                                @Override
                                public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {

                                    if (subscriber.isUnsubscribed()) {
                                        mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                                    } else {
                                        subscriber.onNext(runQuery());
                                    }
                                }
                            });
                    subscriber.onNext(cursor);
                }
            };
        }
    }
}
