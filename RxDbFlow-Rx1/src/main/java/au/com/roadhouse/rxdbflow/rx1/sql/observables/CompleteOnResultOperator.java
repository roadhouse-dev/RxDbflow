package au.com.roadhouse.rxdbflow.rx1.sql.observables;

import rx.Observable;
import rx.Subscriber;

/**
 * An observable operator which calls onComplete on it's subscribers
 */

public class CompleteOnResultOperator<T> implements Observable.Operator<T, T> {

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override
            public void onNext(T aLong) {
                subscriber.onNext(aLong);
                subscriber.onCompleted();
            }
        };
    }
}

