package au.com.roadhouse.rxdbflow.sql.observables;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

import rx.Observable;
import rx.Subscriber;


public class DBFlowResultObservable<TModel extends Model> extends Observable<CursorResult<TModel>> {

    private final Class<TModel> mModelClazz;
    private final ModelQueriable<TModel> mBaseModelQueriable;


    public DBFlowResultObservable(Class<TModel> clazz, final ModelQueriable<TModel> baseModelQueriable) {
        super(new OnDBFlowSubscribeWithChanges<>(baseModelQueriable));
        mModelClazz = clazz;
        mBaseModelQueriable = baseModelQueriable;
    }

    public Observable<CursorResult<TModel>> restartOnChange(){
        return lift(new DBFlowOnChangeOperator());
    }

    private static class OnDBFlowSubscribeWithChanges<AModel extends Model> implements OnSubscribe<CursorResult<AModel>> {

        private final ModelQueriable<AModel> mBaseModelQueriable;

        OnDBFlowSubscribeWithChanges(ModelQueriable<AModel> baseModelQueriable){
            mBaseModelQueriable = baseModelQueriable;
        }

        @Override
        public void call(final Subscriber<? super CursorResult<AModel>> subscriber) {
            subscriber.onNext(mBaseModelQueriable.queryResults());
        }
    }

    private class DBFlowOnChangeOperator implements Observable.Operator<CursorResult<TModel>, CursorResult<TModel>> {

        private FlowContentObserver mFlowContentObserver;

        private DBFlowOnChangeOperator() {
            mFlowContentObserver = new FlowContentObserver();
        }

        @Override
        public Subscriber<? super CursorResult<TModel>> call(final Subscriber<? super CursorResult<TModel>> subscriber) {
            return new Subscriber<CursorResult<TModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(CursorResult<TModel> tModels) {
                    mFlowContentObserver.registerForContentChanges(FlowManager.getContext(), mModelClazz);
                    mFlowContentObserver.addOnTableChangedListener(
                            new FlowContentObserver.OnTableChangedListener() {
                                @Override
                                public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {

                                    if (subscriber.isUnsubscribed()) {
                                        mFlowContentObserver.unregisterForContentChanges(FlowManager.getContext());
                                    } else {
                                        subscriber.onNext(mBaseModelQueriable.queryResults());
                                    }
                                }
                            });
                    subscriber.onNext(tModels);
                }
            };

        }
    }
}
