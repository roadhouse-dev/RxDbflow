package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.BaseQueriable;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxDBFlow query, creates an observable which emits a single count of results in the database table/view.
 * In most cases this observable should be paired with
 * {@link au.com.roadhouse.rxdbflow.sql.language.RxSQLite#selectCountOf(IProperty[])}
 * @param <TModel> The table/view model in which the count is performed.
 */
public class DBFlowCountObservable<TModel extends Model> extends Observable<Long> {

    private final BaseQueriable<TModel> mBaseModelQueriable;
    private final Class<TModel> mModelClazz;
    private final DatabaseWrapper mDatabaseWrapper;

    /**
     * Creates a new DBFlowCountObservable. Generally this constructor is not used directly, but used
     * as part of a query statement. {@see au.com.roadhouse.rxdbflow.sql.language.RxSQLite}
     * @param clazz The class of the table/view the query effects.
     * @param baseModelQueriable The query to run when computing the count
     * @param databaseWrapper The database wrapper that the target table/view belongs too.
     */
    public DBFlowCountObservable(@NonNull Class<TModel> clazz, final BaseQueriable<TModel> baseModelQueriable,
                                 @Nullable DatabaseWrapper databaseWrapper) {
        super(new OnDBFlowSubscribeWithChanges<>(baseModelQueriable, databaseWrapper));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mDatabaseWrapper = databaseWrapper;
    }

    /**
     * Observes changes on the current table, restarting the query on change and emits the new count
     * to any subscribers
     * @return An observable which observes any changes in the current table
     */
    public Observable<Long> restartOnChange(){
        return lift(new DBFlowOnChangeOperator());
    }

    /**
     * Observes changes for specific tables, restarting the query on change and emits the new count
     * to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    public Observable<Long> restartOnChange(Class<TModel>... tableToListen){
        return lift(new DBFlowOnChangeOperator(tableToListen));
    }

    private static class OnDBFlowSubscribeWithChanges<AModel extends Model> implements OnSubscribe<Long> {

        private final BaseQueriable<AModel> mBaseModelQueriable;
        private final DatabaseWrapper mDatabaseWrapper;

        OnDBFlowSubscribeWithChanges(BaseQueriable<AModel> baseModelQueriable, DatabaseWrapper databaseWrapper){
            mBaseModelQueriable = baseModelQueriable;
            mDatabaseWrapper = databaseWrapper;
        }

        @Override
        public void call(final Subscriber<? super Long> subscriber) {
            subscriber.onNext(triggerCount());
        }

        private Long triggerCount(){
            if(mDatabaseWrapper != null){
                return mBaseModelQueriable.count(mDatabaseWrapper);
            } else {
                return mBaseModelQueriable.count();
            }
        }
    }

    private Long triggerCount(){
        if(mDatabaseWrapper != null){
            return mBaseModelQueriable.count(mDatabaseWrapper);
        } else {
            return mBaseModelQueriable.count();
        }
    }

    private class DBFlowOnChangeOperator implements Observable.Operator<Long, Long> {

        private List<Class<TModel>> mSubscribedClasses;
        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mSubscribedClasses = new ArrayList<>();
            mFlowContentObserver = new FlowContentObserver();
            mSubscribedClasses.add(mModelClazz);
        }

        private DBFlowOnChangeOperator(Class<TModel>[] tableToListen) {
            mSubscribedClasses = new ArrayList<>();
            mFlowContentObserver = new FlowContentObserver();
            for (int i = 0; i < tableToListen.length; i++) {
                mSubscribedClasses.add(tableToListen[i]);
            }
            mSubscribedClasses.addAll(Arrays.asList(tableToListen));
        }

        @Override
        public Subscriber<? super Long> call(final Subscriber<? super Long> subscriber) {
            return new Subscriber<Long>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Long tModels) {
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
                                        subscriber.onNext(triggerCount());
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
