package au.com.roadhouse.rxdbflow.rx2.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

/**
 * Defines the where clause of a SQL query
 */
public class RxWhere<TModel> extends BaseModelQueriableObservable<TModel>
        implements Query, RxTransformable<TModel> {

    Where<TModel> mRealWhere;

    RxWhere(Where<TModel> where) {
        super(where);
        mRealWhere = where;
    }

    /**
     * Adds a param to the WHERE clause with the custom {@link SQLOperator}
     *
     * @param condition The {@link SQLOperator} to use
     * @return An instance of RxWhere
     */
    public RxWhere<TModel> and(SQLOperator condition) {
        mRealWhere.and(condition);

        return this;
    }

    /**
     * Adds a param to the WHERE clause with the custom {@link SQLOperator}
     *
     * @param condition The {@link SQLOperator} to use
     * @return An instance of RxWhere
     */
    public RxWhere<TModel> or(SQLOperator condition) {
        mRealWhere.or(condition);
        return this;
    }

    /**
     * Constructs a where clause which contains all of the passed in conditions
     *
     * @param conditions The conditions to add to the where clause
     * @return An instance of RxWhere
     */
    public RxWhere<TModel> andAll(SQLOperator[] conditions) {
        mRealWhere.andAll(conditions);
        return this;
    }

    @Override
    public String getQuery() {
        return mRealWhere.getQuery();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> groupBy(NameAlias... nameAliases) {
        mRealWhere.groupBy(nameAliases);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> groupBy(IProperty... properties) {
        mRealWhere.groupBy(properties);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        mRealWhere.orderBy(nameAlias, ascending);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(IProperty property, boolean ascending) {
        mRealWhere.orderBy(property, ascending);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(OrderBy orderBy) {
        mRealWhere.orderBy(orderBy);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> limit(int count) {
        mRealWhere.limit(count);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> offset(int offset) {
        mRealWhere.offset(offset);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> having(SQLOperator... conditions) {
        mRealWhere.having(conditions);
        return this;
    }
}
