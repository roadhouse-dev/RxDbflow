package au.com.roadhouse.rxdbflow.sql.language;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.Case;
import com.raizlabs.android.dbflow.sql.language.CaseCondition;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Index;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.sql.trigger.Trigger;
import com.raizlabs.android.dbflow.structure.Model;

public class RxSQLite {
    /**
     * @param properties The properties/columns to SELECT.
     * @return A beginning of the SELECT statement.
     */
    public static RxSelect select(IProperty... properties) {
        return new RxSelect(properties);
    }

    /**
     * Starts a new SELECT COUNT(property1, property2, propertyn) (if properties specified) or
     * SELECT COUNT(*).
     *
     * @param properties Optional, if specified returns the count of non-null ROWs from a specific single/group of columns.
     * @return A new select statement SELECT COUNT(expression)
     */
    public static RxSelect selectCountOf(IProperty... properties) {
        return new RxSelect(Method.count(properties));
    }

    /**
     * @param table    The tablet to update.
     * @param <TModel> The class that implements {@link Model}.
     * @return A new UPDATE statement.
     */
    public static <TModel extends Model> RxUpdate<TModel> update(Class<TModel> table) {
        return new RxUpdate<>(table);
    }

    /**
     * @param table    The table to insert.
     * @param <TModel> The class that implements {@link Model}.
     * @return A new INSERT statement.
     */
    public static <TModel extends Model> RxInsert<TModel> insert(Class<TModel> table) {
        return new RxInsert<>(table);
    }

    /**
     * @return Begins a DELETE statement.
     */
    public static RxDelete delete() {
        return new RxDelete();
    }

    /**
     * Starts a DELETE statement on the specified table.
     *
     * @param table    The table to delete from.
     * @param <TModel> The class that implements {@link Model}.
     * @return A {@link From} with specified DELETE on table.
     */
    public static <TModel extends Model> RxFrom<TModel> delete(Class<TModel> table) {
        return delete().from(table);
    }

    /**
     * Starts an INDEX statement on specified table.
     *
     * @param name     The name of the index.
     * @param <TModel> The class that implements {@link Model}.
     * @return A new INDEX statement.
     */
    public static <TModel extends Model> Index<TModel> index(String name) {
        return new Index<>(name);
    }

    /**
     * Starts a TRIGGER statement.
     *
     * @param name The name of the trigger.
     * @return A new TRIGGER statement.
     */
    public static Trigger createTrigger(String name) {
        return Trigger.create(name);
    }

    /**
     * Starts a CASE statement.
     *
     * @param condition The condition to check for in the WHEN.
     * @return A new {@link CaseCondition}.
     */
    //TODO: Wrap Case
    public static <TReturn> CaseCondition<TReturn> caseWhen(@NonNull SQLCondition condition) {
        return SQLite.caseWhen(condition);
    }

    /**
     * Starts an efficient CASE statement. The value passed here is only evaulated once. A non-efficient
     * case statement will evaluate all of its {@link SQLCondition}.
     *
     * @param caseColumn The value
     * @param <TReturn>
     * @return
     */
    public static <TReturn> Case<TReturn> _case(Property<TReturn> caseColumn) {
        return SQLite._case(caseColumn);
    }

    /**
     * Starts an efficient CASE statement. The value passed here is only evaulated once. A non-efficient
     * case statement will evaluate all of its {@link SQLCondition}.
     *
     * @param caseColumn The value
     * @param <TReturn>
     * @return
     */
    public static <TReturn> Case<TReturn> _case(IProperty caseColumn) {
        return SQLite._case(caseColumn);
    }
}
