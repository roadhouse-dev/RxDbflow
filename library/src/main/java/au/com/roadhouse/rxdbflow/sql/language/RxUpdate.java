package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Set;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.structure.Model;


public class RxUpdate<TModel extends Model> implements Query {

    private final Update<TModel> mRealUpdate;

    RxUpdate(Class<TModel> clazz){
        mRealUpdate = new Update<>(clazz);
    }

    @Override
    public String getQuery() {
        return mRealUpdate.getQuery();
    }

    public RxUpdate conflictAction(ConflictAction conflictAction) {
        mRealUpdate.conflictAction(conflictAction);
        return this;
    }

    /**
     * @return This instance.
     * @see ConflictAction#ROLLBACK
     */
    public RxUpdate orRollback() {
        return conflictAction(ConflictAction.ROLLBACK);
    }

    /**
     * @return This instance.
     * @see ConflictAction#ABORT
     */
    public RxUpdate orAbort() {
        return conflictAction(ConflictAction.ABORT);
    }

    /**
     * @return This instance.
     * @see ConflictAction#REPLACE
     */
    public RxUpdate orReplace() {
        return conflictAction(ConflictAction.REPLACE);
    }

    /**
     * @return This instance.
     * @see ConflictAction#FAIL
     */
    public RxUpdate orFail() {
        return conflictAction(ConflictAction.FAIL);
    }

    /**
     * @return This instance.
     * @see ConflictAction#IGNORE
     */
    public RxUpdate orIgnore() {
        return conflictAction(ConflictAction.IGNORE);
    }

    /**
     * Begins a SET piece of the SQL query
     *
     * @param conditions The array of conditions that define this SET statement
     * @return A SET query piece of this statement
     */
    public RxSet<TModel> set(SQLCondition... conditions) {
        Set<TModel> set = mRealUpdate.set(conditions);
        return new RxSet<>(set);
    }

}
