package au.com.roadhouse.rxdbflow.rx1.sql.language;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.Set;
import com.raizlabs.android.dbflow.sql.language.Update;

/**
 * Creates an update SQL query
 */
public class RxUpdate<TModel> implements Query {

    private final Update<TModel> mRealUpdate;

    RxUpdate(Class<TModel> clazz){
        mRealUpdate = new Update<>(clazz);
    }

    @Override
    public String getQuery() {
        return mRealUpdate.getQuery();
    }

    /**
     * Adds a conflict action to the update statement
     * @param conflictAction The action to take on conflict
     * @return An RxUpdate instance
     */
    public RxUpdate conflictAction(ConflictAction conflictAction) {
        mRealUpdate.conflictAction(conflictAction);
        return this;
    }

    /**
     * Adds a rollback action on conflict
     * @return This instance.
     * @see ConflictAction#ROLLBACK
     */
    public RxUpdate orRollback() {
        return conflictAction(ConflictAction.ROLLBACK);
    }

    /**
     * Adds an abort action on conflict
     * @return This instance.
     * @see ConflictAction#ABORT
     */
    public RxUpdate orAbort() {
        return conflictAction(ConflictAction.ABORT);
    }

    /**
     * Adds a replace action on conflict
     * @return This instance.
     * @see ConflictAction#REPLACE
     */
    public RxUpdate orReplace() {
        return conflictAction(ConflictAction.REPLACE);
    }

    /**
     * Adds a fail action on conflict
     * @return This instance.
     * @see ConflictAction#FAIL
     */
    public RxUpdate orFail() {
        return conflictAction(ConflictAction.FAIL);
    }

    /**
     * Adds an ignore action on conflict
     * @return This instance.
     * @see ConflictAction#IGNORE
     */
    public RxUpdate orIgnore() {
        return conflictAction(ConflictAction.IGNORE);
    }

    /**
     * Begins a SET block for the SQL query
     *
     * @param conditions The array of conditions that define this SET statement
     * @return A SET query piece of this statement
     */
    public RxSet<TModel> set(SQLOperator... conditions) {
        Set<TModel> set = mRealUpdate.set(conditions);
        return new RxSet<>(set);
    }

}
