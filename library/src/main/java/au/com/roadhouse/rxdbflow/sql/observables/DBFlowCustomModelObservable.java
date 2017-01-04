package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Given a RxSQLite query, emits the the first element from the query as a custom model.
 */
public class DBFlowCustomModelObservable<TQueryModel extends BaseQueryModel, TModel> extends Observable<TQueryModel> {

    private final Class<TQueryModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;
    private List<Class<? extends Model>> mSubscribedClasses;

    /**
     * Creates a new observable which runs a query and emits the first element in the result set as a CustomModel
     * @param clazz The table/view model in which the FlowCursorList will contain
     * @param baseModelQueriable The query to run
     */
    public DBFlowCustomModelObservable(Class<TQueryModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
        mSubscribedClasses = new ArrayList<>();
    }

    /**
     * Observes changes on the current table, restarts the query on change, and emits the updated
     * query result to any subscribers
     * @param tableToListen The tables to observe for changes
     * @return An observable which observes any changes in the specified tables
     */
    @SafeVarargs
    public final Observable<TQueryModel> restartOnChange(Class<? extends Model>... tableToListen){
        Collections.addAll(mSubscribedClasses, tableToListen);
        return lift(new DBFlowOnChangeOperator());
    }

    /**
     * Forces onComplete to be called upon returning with a result, therefore automatically
     * unsubscribing the subscription. This should be used when you're only interested in a
     * single result i.e. not using {@link #restartOnChange(Class[])}.
     * If this is not used, the subscriber will be responsible for unsubscribing
     * @return An observable which will call onComplete once the result has returned.
     */
    public Observable<TQueryModel> completeOnResult(){
        return lift(new CompleteOnResultOperator<TQueryModel>());
    }

    private static class OnDBFlowSubscribeWithChanges<AQueryModel extends BaseQueryModel, TModel> implements OnSubscribe<AQueryModel> {

        private final Class<AQueryModel> mClazz;
        private final ModelQueriable<TModel> mBaseModelQueriable;

        OnDBFlowSubscribeWithChanges(Class<AQueryModel> clazz, ModelQueriable<TModel> baseModelQueriable) {
            mBaseModelQueriable = baseModelQueriable;
            mClazz = clazz;
        }


        @Override
        public void call(final Subscriber<? super AQueryModel> subscriber) {
            subscriber.onNext(mBaseModelQueriable.queryCustomSingle(mClazz));
        }
    }

    private class DBFlowOnChangeOperator implements Observable.Operator<TQueryModel, TQueryModel> {

        private FlowContentObserver mFlowContentObserver;

        public DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super TQueryModel> call(final Subscriber<? super TQueryModel> subscriber) {
            return new Subscriber<TQueryModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(TQueryModel tModels) {
                    for (int i = 0; i < mSubscribedClasses.size(); i++) {
                        mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mSubscribedClasses.get(i));
                    }
                    mFlowContentObserver.addModelChangeListener(new FlowContentObserver.OnModelStateChangedListener() {
                        @Override
                        public void onModelStateChanged(@Nullable Class<?> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {
                            if (subscriber.isUnsubscribed()) {
                                mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                            } else {
                                subscriber.onNext(mBaseModelQueriable.queryCustomSingle(mModelClazz));
                            }
                        }
                    });
                }
            };

        }
    }
}
