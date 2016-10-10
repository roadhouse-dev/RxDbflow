package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

/**
 * Description: Defines the SQL WHERE statement of the query.
 */
public class RxWhere<TModel extends Model> extends BaseModelQueriableObservable<TModel>
        implements Query, RxTransformable<TModel> {

    Where<TModel> mRealWhere;

    RxWhere(Where<TModel> where) {
        super(where);
        mRealWhere = where;
    }

    RxWhere<TModel> andAll(SQLCondition[] conditions) {
        mRealWhere.andAll(conditions);
        return this;
    }

    @Override
    public String getQuery() {
        return mRealWhere.getQuery();
    }

    @Override
    public RxWhere<TModel> groupBy(NameAlias... nameAliases) {
        mRealWhere.groupBy(nameAliases);
        return this;
    }

    @Override
    public RxWhere<TModel> groupBy(IProperty... properties) {
        mRealWhere.groupBy(properties);
        return this;
    }

    @Override
    public RxWhere<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        mRealWhere.orderBy(nameAlias, ascending);
        return this;
    }

    @Override
    public RxWhere<TModel> orderBy(IProperty property, boolean ascending) {
        mRealWhere.orderBy(property, ascending);
        return this;
    }

    @Override
    public RxWhere<TModel> orderBy(OrderBy orderBy) {
        mRealWhere.orderBy(orderBy);
        return this;
    }

    @Override
    public RxWhere<TModel> limit(int count) {
        mRealWhere.limit(count);
        return this;
    }

    @Override
    public RxWhere<TModel> offset(int offset) {
        mRealWhere.offset(offset);
        return this;
    }

    @Override
    public RxWhere<TModel> having(SQLCondition... conditions) {
        mRealWhere.having(conditions);
        return this;
    }
}
