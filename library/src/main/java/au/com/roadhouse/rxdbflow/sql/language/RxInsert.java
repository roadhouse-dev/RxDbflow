package au.com.roadhouse.rxdbflow.sql.language;

import android.content.ContentValues;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Insert;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

import java.util.List;

public class RxInsert<TModel extends Model> extends BaseQueriableObservable implements Query {

    private final Insert<TModel> mRealInsert;

    RxInsert(Class<TModel> table) {
        super(createInsert(table));
        //noinspection unchecked
        mRealInsert = (Insert<TModel>) getRealQueriable();
    }


    private static <TModel extends Model> Insert<TModel> createInsert(Class<TModel> table) {
        return new Insert<>(table);
    }

    /**
     * The optional columns to specify. If specified, the values length must correspond to these columns, and
     * each column has a 1-1 relationship to the values.
     *
     * @param columns The columns to use
     */
    public RxInsert<TModel> columns(String... columns) {
        mRealInsert.columns(columns);
        return this;
    }

    public RxInsert<TModel> columns(IProperty... properties) {
        mRealInsert.columns(properties);
        return this;
    }

    public RxInsert<TModel> columns(List<IProperty> properties) {
        mRealInsert.columns(properties);
        return this;
    }

    /**
     * @return Appends a list of columns to this INSERT statement from the associated {@link TModel}.
     */
    public RxInsert<TModel> asColumns() {
        mRealInsert.asColumns();
        return this;
    }

    /**
     * The required values to specify. It must be non-empty and match the length of the columns when
     * a set of columns are specified.
     *
     * @param values The non type-converted values
     */
    public RxInsert<TModel> values(Object... values) {
        mRealInsert.values(values);
        return this;
    }

    /**
     * Uses the {@link Condition} pairs to fill this insert query.
     *
     * @param conditions The conditions that we use to fill the columns and values of this INSERT
     */
    public RxInsert<TModel> columnValues(SQLCondition... conditions) {
        mRealInsert.columnValues(conditions);
        return this;
    }

    /**
     * Uses the {@link Condition} pairs to fill this insert query.
     *
     * @param conditionGroup The ConditionGroup to use
     */
    public RxInsert<TModel> columnValues(ConditionGroup conditionGroup) {
        mRealInsert.columnValues(conditionGroup);
        return this;
    }

    public RxInsert<TModel> columnValues(ContentValues contentValues) {
        mRealInsert.columnValues(contentValues);
        return this;
    }

    /**
     * Appends the specified {@link From}, which comes from a {@link Select} statement.
     *
     * @param selectFrom The from that is continuation of {@link Select}.
     */
    public RxInsert<TModel> select(From<? extends Model> selectFrom) {
        mRealInsert.select(selectFrom);
        return this;
    }


    /**
     * Specifies the optional OR method to use for this insert query
     *
     * @param action The conflict action to use
     * @return
     */
    public RxInsert<TModel> or(ConflictAction action) {
        mRealInsert.or(action);
        return this;
    }

    /**
     * Specifies OR REPLACE, which will either insert if row does not exist, or replace the value if it does.
     *
     * @return
     */
    public RxInsert<TModel> orReplace() {
        return or(ConflictAction.REPLACE);
    }

    /**
     * Specifies OR ROLLBACK, which will cancel the current transaction or ABORT the current statement.
     *
     * @return
     */
    public RxInsert<TModel> orRollback() {
        return or(ConflictAction.ROLLBACK);
    }

    /**
     * Specifies OR ABORT, which will cancel the current INSERT, but all other operations will be preserved in
     * the current transaction.
     *
     * @return
     */
    public RxInsert<TModel> orAbort() {
        return or(ConflictAction.ABORT);
    }

    /**
     * Specifies OR FAIL, which does not back out of the previous statements. Anything else in the current
     * transaction will fail.
     *
     * @return
     */
    public RxInsert<TModel> orFail() {
        return or(ConflictAction.FAIL);
    }

    /**
     * Specifies OR IGNORE, which ignores any kind of error and proceeds as normal.
     *
     * @return
     */
    public RxInsert<TModel> orIgnore() {
        return or(ConflictAction.IGNORE);
    }

    @Override
    public String getQuery() {
        return mRealInsert.getQuery();
    }
}
