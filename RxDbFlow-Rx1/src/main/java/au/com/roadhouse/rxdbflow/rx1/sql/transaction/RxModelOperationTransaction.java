package au.com.roadhouse.rxdbflow.rx1.sql.transaction;

import android.support.annotation.IntDef;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import au.com.roadhouse.rxdbflow.rx1.structure.RxBaseModel;
import rx.Observable;
import rx.Subscriber;

/**
 * <p>Provides a transaction block that runs a series of operations on a list of models. This class has no
 * restrictions on the type of model, or the operations being performed. However in the case that all
 * models are the same, and the same type of operation is being employed on all models optimisations are
 * used to ensure the fasest possible performance. Please see
 * <ahref>https://github.com/Raizlabs/DBFlow/blob/master/usage2/StoringData.md#faststoremodeltransaction</ahref> for
 * more information on how these optimisations work.</p>
 * <p> Creation of a ModelOperationTransaction is via the use of the inner builder. </p>
 */
public class RxModelOperationTransaction extends Observable<Void> {

    @IntDef({MODEL_OPERATION_UNSET, MODEL_OPERATION_SAVE, MODEL_OPERATION_UPDATE, MODEL_OPERATION_DELETE, MODEL_OPERATION_INSERT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ModelOperation {
    }

    public static final int MODEL_OPERATION_UNSET = -1;
    public static final int MODEL_OPERATION_SAVE = 0;
    public static final int MODEL_OPERATION_UPDATE = 1;
    public static final int MODEL_OPERATION_INSERT = 2;
    public static final int MODEL_OPERATION_DELETE = 3;

    private RxModelOperationTransaction(Builder builder) {
        //noinspection unchecked
        super(new OnTransactionSubscribe(builder.mModelList, builder.mOperationList, builder.mDatabaseWrapper, builder.mCanBatchTransaction, builder.mMustUseAdapters));
    }

    private static class OnTransactionSubscribe implements OnSubscribe<Void> {

        private final List<Object> mModelList;
        private final List<Integer> mOperationList;
        private final DatabaseWrapper mDatabaseWrapper;
        private final boolean mCanBatchTransaction;
        private boolean mMustUserAdapter;

        private OnTransactionSubscribe(List<Object> modelList, List<Integer> operationList, DatabaseWrapper databaseWrapper, boolean canBatchTransaction, boolean mustUseAdapter) {
            mModelList = modelList;
            mOperationList = operationList;
            mDatabaseWrapper = databaseWrapper;
            mCanBatchTransaction = canBatchTransaction;
            mMustUserAdapter = mustUseAdapter;
        }

        @Override
        public void call(Subscriber<? super Void> subscriber) {

            if (mOperationList.size() != mModelList.size()) {
                subscriber.onError(new IllegalStateException("model list and operation list must be of the same size"));
                return;
            }

            try {
                mDatabaseWrapper.beginTransaction();
                if (mModelList.size() == 0) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                    return;
                } else if (mCanBatchTransaction) { //All operations are the same, and all Model types are the same
                    @ModelOperation int batchOperation = mOperationList.get(0);
                    performBatchOperation(batchOperation);
                } else if (mMustUserAdapter) {
                    performIndividualAdapterOperations();
                } else {
                    performIndividualOperations();
                }
                mDatabaseWrapper.setTransactionSuccessful();

                subscriber.onNext(null);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                mDatabaseWrapper.endTransaction();
            }
        }

        private void performIndividualOperations() {
            for (int i = 0; i < mModelList.size(); i++) {
                @ModelOperation int operation = mOperationList.get(i);
                Model model = (Model) mModelList.get(i);

                if (operation == MODEL_OPERATION_SAVE) {
                    model.save();
                } else if (operation == MODEL_OPERATION_DELETE) {
                    model.delete();
                } else if (operation == MODEL_OPERATION_INSERT) {
                    model.insert();
                } else if (operation == MODEL_OPERATION_UPDATE) {
                    model.update();
                }
            }
        }

        @SuppressWarnings("unchecked")
        private void performIndividualAdapterOperations() {
            for (int i = 0; i < mModelList.size(); i++) {
                @ModelOperation int operation = mOperationList.get(i);
                Object model = mModelList.get(i);
                ModelAdapter modelAdapter = FlowManager.getModelAdapter(model.getClass());

                if (operation == MODEL_OPERATION_SAVE) {
                    modelAdapter.save(model);
                } else if (operation == MODEL_OPERATION_DELETE) {
                    modelAdapter.delete(model);
                } else if (operation == MODEL_OPERATION_INSERT) {
                    modelAdapter.insert(model);
                } else if (operation == MODEL_OPERATION_UPDATE) {
                    modelAdapter.update(model);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private void performBatchOperation(@ModelOperation int batchOperation) {
            ModelAdapter adapter = FlowManager.getModelAdapter(mModelList.get(0).getClass());
            if (batchOperation == MODEL_OPERATION_SAVE) {
                adapter.saveAll(mModelList);
            } else if (batchOperation == MODEL_OPERATION_INSERT) {
                adapter.insertAll(mModelList);
            } else if (batchOperation == MODEL_OPERATION_UPDATE) {
                adapter.updateAll(mModelList);
            }
        }
    }

    /**
     * A builder which creates a new model operation transaction block.
     */
    public static class Builder {
        private final DatabaseWrapper mDatabaseWrapper;
        private List<Object> mModelList;
        private List<Integer> mOperationList;
        @ModelOperation
        private int mDefaultOperation = MODEL_OPERATION_UNSET;
        //If all models and operations are the same we can optimise for speed
        private boolean mCanBatchTransaction;
        private boolean mMustUseAdapters;

        /**
         * Creates a new builder with a specific database wrapper.
         *
         * @param databaseWrapper The database wrapper to run the transaction against
         */
        public Builder(DatabaseWrapper databaseWrapper) {
            mModelList = new ArrayList<>();
            mOperationList = new ArrayList<>();
            mDatabaseWrapper = databaseWrapper;
        }

        /**
         * Creates a new builder, using a Model class to derive the target database. The class passed
         * to the parameter does not restrict the tables on which the database operations are performed.
         * As long as the operations run on a single database.
         *
         * @param clazz The model class used to derive the target database.
         */
        public Builder(Class<? extends Model> clazz) {
            this(FlowManager.getDatabaseForTable(clazz).getWritableDatabase());
        }

        /**
         * Sets a default operation, this is useful when adding a list of models to the builder.
         *
         * @param modelOperation The model operation to use when no model operation is specified
         * @return An instance of the current builder.
         */
        public Builder setDefaultOperation(@ModelOperation int modelOperation) {
            mDefaultOperation = modelOperation;

            return this;
        }

        /**
         * Adds a model and it's associated operation to be performed within the transaction
         * block
         *
         * @param model          The model to perform the transaction on
         * @param modelOperation The operation to be performed.
         * @return An instance of the current builder.
         */
        public Builder addModel(Object model, @ModelOperation int modelOperation) {
            if (mModelList.size() == 0) {
                mCanBatchTransaction = true;
                mMustUseAdapters = false;
            } else {

                if (!mMustUseAdapters) {
                    mMustUseAdapters = !(model instanceof RxBaseModel);
                }

                if (mCanBatchTransaction) {
                    //noinspection WrongConstant
                    mCanBatchTransaction =
                            (mModelList.get(0).getClass() == model.getClass()) &&
                                    mOperationList.get(0) == modelOperation &&
                                    modelOperation != MODEL_OPERATION_DELETE;
                }
            }

            mModelList.add(model);
            mOperationList.add(modelOperation);
            return this;
        }

        /**
         * Adds a model for which the default operation will be performed within the transaction
         * block.
         * <p><b>Please Note: </b> The default operation must be set before calling this</p>
         *
         * @param model The model to perform the default operation on.
         * @return An instance of the current builder.
         */
        public Builder addModel(Object model) {
            if (mDefaultOperation == MODEL_OPERATION_UNSET) {
                throw new IllegalStateException("No default operation set");
            }
            addModel(model, mDefaultOperation);

            return this;
        }

        /**
         * Adds a list of models, with a single operation to be performed on each model within the
         * transaction block
         *
         * @param modelList      A list of models
         * @param modelOperation The operation to be performed on each model
         * @return An instance of the current builder.
         */
        public Builder addAll(List<Object> modelList, @ModelOperation int modelOperation) {
            for (int i = 0; i < modelList.size(); i++) {
                addModel(modelList.get(i), modelOperation);
            }
            return this;
        }


        /**
         * Adds a list of models, with a paired list of operations. Each model within the list will have
         * have the operation at the same index applied to it, as such both lists must have the same number
         * of elements.
         *
         * @param modelList          A list of models
         * @param modelOperationList A list of paired operations
         * @return An instance of the current builder.
         */
        public Builder addAll(List<Object> modelList, List<Integer> modelOperationList) {
            if (modelList.size() != modelOperationList.size()) {
                throw new IllegalArgumentException("model list and operation list must be of the same size");
            }
            for (int i = 0; i < modelList.size(); i++) {
                //noinspection WrongConstant
                addModel(modelList.get(i), modelOperationList.get(i));
            }
            return this;
        }

        /**
         * Adds a list of models which will have the default operation performed on them.
         * * <p><b>Please Note: </b> The default operation must be set before calling this</p>
         *
         * @param modelList A list of models to be effected by the default operation
         * @return An instance of the current builder.
         */
        public Builder addAll(List<Object> modelList) {
            addAll(modelList, mDefaultOperation);

            return this;
        }


        /**
         * Builds a new ModelOperationTransaction observable which will run all database operations within
         * a single transaction once it's subscribed to.
         *
         * @return A new GenericTransactionBlock containing all database operations
         */
        public RxModelOperationTransaction build() {
            return new RxModelOperationTransaction(this);
        }
    }
}
