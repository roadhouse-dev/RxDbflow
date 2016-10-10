package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

import rx.Observable;
import rx.Subscriber;


public class DBFlowQueryListObservable<TModel extends Model> extends Observable<FlowQueryList<TModel>> {

    private final ModelQueriable<TModel> mBaseModelQueriable;
    private final Class<TModel> mModelClazz;

    public DBFlowQueryListObservable(Class<TModel> clazz, final ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(clazz, baseModelQueriable));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    public Observable<FlowQueryList<TModel>> restartOnChange(){
        return lift(new DBFlowOnChangeOperator());
    }

    private static class OnDBFlowSubscribeWithChanges<AModel extends Model> implements OnSubscribe<FlowQueryList<AModel>> {

        private final ModelQueriable<AModel> mBaseModelQueriable;

        OnDBFlowSubscribeWithChanges(Class<AModel> clazz, ModelQueriable<AModel> baseModelQueriable){
            mBaseModelQueriable = baseModelQueriable;
        }

        @Override
        public void call(final Subscriber<? super FlowQueryList<AModel>> subscriber) {
            subscriber.onNext(mBaseModelQueriable.flowQueryList());
        }
    }


    private class DBFlowOnChangeOperator implements Observable.Operator<FlowQueryList<TModel>, FlowQueryList<TModel>> {

        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super FlowQueryList<TModel>> call(final Subscriber<? super FlowQueryList<TModel>> subscriber) {
            return new Subscriber<FlowQueryList<TModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(FlowQueryList<TModel> tModels) {
                    mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mModelClazz);
                    mFlowContentObserver.addOnTableChangedListener(
                            new FlowContentObserver.OnTableChangedListener() {
                                @Override
                                public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {

                                    if (subscriber.isUnsubscribed()) {
                                        mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                                    } else {
                                        subscriber.onNext(mBaseModelQueriable.flowQueryList());
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
