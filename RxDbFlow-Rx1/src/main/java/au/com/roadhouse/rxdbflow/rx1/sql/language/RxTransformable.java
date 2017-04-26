package au.com.roadhouse.rxdbflow.rx1.sql.language;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

/**
 * An interface for all SQL query classes that transform RxSQLite query results
 *
 * @param <T> The table model class that the sqlite query will emit
 */
public interface RxTransformable<T> {

    /**
     * Constructs a group by clause
     * @param nameAliases The fields to group the query by
     * @return An instance of RxWhere
     */
    RxWhere<T> groupBy(NameAlias... nameAliases);

    /**
     * Constructs a group by clause
     * @param properties The fields to group the query by
     * @return An instance of RxWhere
     */
    RxWhere<T> groupBy(IProperty... properties);

    /**
     * Constructs an order by clause
     * @param nameAlias The field to use for ordering
     * @param ascending True to sort ascending, false otherwise
     * @return An instance of RxWhere
     */
    RxWhere<T> orderBy(NameAlias nameAlias, boolean ascending);

    /**
     * Constructs an order by clause
     * @param property The field to use for ordering
     * @param ascending True to sort ascending, false otherwise
     * @return An instance of RxWhere
     */
    RxWhere<T> orderBy(IProperty property, boolean ascending);

    /**
     * Adds an order by clause to the current query
     * @param orderBy The order by clause to use
     * @return An instance of RxWhere
     */
    RxWhere<T> orderBy(OrderBy orderBy);

    /**
     * Constructs a limit clause, limiting the number or returned results
     * @param count The number of records to limit to query to
     * @return An instance of RxWhere
     */
    RxWhere<T> limit(int count);

    /**
     * Constructs an offset clause
     * @param offset The number of records to offset the results by
     * @return An instance of RxWhere
     */
    RxWhere<T> offset(int offset);

    /**
     * Constructs a having clause
     * @param conditions The conditions contained in the having clause
     * @return An instance of RxWhere
     */
    RxWhere<T> having(SQLOperator... conditions);
}
