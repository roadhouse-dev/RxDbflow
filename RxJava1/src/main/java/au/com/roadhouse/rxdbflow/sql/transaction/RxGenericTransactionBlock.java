package au.com.roadhouse.rxdbflow.sql.transaction;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * <p>Provides a transaction block that runs a series of database operations on a single database within
 * a single transaction. There is no limitation on what type of operations, that can run within a single transaction.</p>
 * <p> Creation of a ModelOperationTransaction is via the use of the inner builder. </p>
 */
public class RxGenericTransactionBlock extends Observable<Void> {

    private RxGenericTransactionBlock(Builder builder) {
        super(new OnTransactionSubscribe<Void>(builder.mTransactionProcessList, builder.mDatabaseWrapper));
    }

    private static class OnTransactionSubscribe<T> implements OnSubscribe<Void> {

        private final List<TransactionOperation> mTransactionProcessList;
        private final DatabaseWrapper mDatabaseWrapper;

        private OnTransactionSubscribe(List<TransactionOperation> transactionProcessList, DatabaseWrapper databaseWrapper) {
            mTransactionProcessList = transactionProcessList;
            mDatabaseWrapper = databaseWrapper;
        }

        @Override
        public void call(Subscriber<? super Void> subscriber) {

            try {
                mDatabaseWrapper.beginTransaction();

                for (int i = 0; i < mTransactionProcessList.size(); i++) {
                    if(!mTransactionProcessList.get(i).onProcess(mDatabaseWrapper)){
                        throw new SQLiteException("A transaction process item failed");
                    }
                }
                subscriber.onNext(null);
                subscriber.onCompleted();


                mDatabaseWrapper.setTransactionSuccessful();
            } catch (Exception e){
                subscriber.onError(e);
            } finally{
                mDatabaseWrapper.endTransaction();
            }
        }
    }

    /**
     * A build which creates a new generic transaction block.
     */
    public static class Builder {

        private final DatabaseWrapper mDatabaseWrapper;
        private List<TransactionOperation> mTransactionProcessList;

        /**
         * Creates a new builder with a specific database wrapper.
         * @param databaseWrapper The database wrapper to run the transaction against
         */
        public Builder(@NonNull DatabaseWrapper databaseWrapper){
            mTransactionProcessList = new ArrayList<>();
            mDatabaseWrapper = databaseWrapper;
        }

        /**
         * Creates a new builder, using a Model class to derive the target database. The class passed
         * to the parameter does not restrict the tables on which the database operations are performed.
         * As long as the operations run on a single database.
         * @param clazz The model class used to derive the target database.
         */
        public Builder(@NonNull Class<? extends Model> clazz){
            mTransactionProcessList = new ArrayList<>();
            mDatabaseWrapper = FlowManager.getDatabaseForTable(clazz).getWritableDatabase();
        }

        /**
         * Adds a new transaction operation to be performed within a transaction block.
         * @param operation The operation to perform
         * @return An instance of the current builder object.
         */
        public Builder addOperation(@NonNull TransactionOperation operation){
            mTransactionProcessList.add(operation);
            return this;
        }

        /**
         * Builds a new GenericTransactionBlock observable which will run all database operations within
         * a single transaction once it's subscribed to.
         * @return A new GenericTransactionBlock containing all database operations
         */
        public RxGenericTransactionBlock build(){
            return new RxGenericTransactionBlock(this);
        }
    }

    /**
     * Represents a single transaction operation to perform within a transaction block.
     */
    public interface TransactionOperation {
        boolean onProcess(DatabaseWrapper databaseWrapper);
    }
}
