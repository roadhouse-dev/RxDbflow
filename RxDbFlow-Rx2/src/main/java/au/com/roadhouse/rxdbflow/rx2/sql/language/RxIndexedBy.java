package au.com.roadhouse.rxdbflow.rx2.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.IndexedBy;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.WhereBase;
import com.raizlabs.android.dbflow.structure.BaseModel;
/**
 * Constructs an Index By clause for a SQL query
 */
public class RxIndexedBy <TModel> implements WhereBase<TModel>, RxTransformable<TModel> {

    private IndexedBy<TModel> mRealIndexedBy;

    RxIndexedBy(IndexedBy<TModel> realIndexedBy) {
        mRealIndexedBy = realIndexedBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> groupBy(NameAlias... nameAliases) {
        return new RxWhere<>(mRealIndexedBy.groupBy(nameAliases));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> groupBy(IProperty... properties) {
        return new RxWhere<>(mRealIndexedBy.groupBy(properties));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        return new RxWhere<>(mRealIndexedBy.orderBy(nameAlias, ascending));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(IProperty property, boolean ascending) {
        return new RxWhere<>(mRealIndexedBy.orderBy(property, ascending));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(OrderBy orderBy) {
        return new RxWhere<>(mRealIndexedBy.orderBy(orderBy));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> limit(int count) {
        return new RxWhere<>(mRealIndexedBy.limit(count));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> offset(int offset) {
        return new RxWhere<>(mRealIndexedBy.offset(offset));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> having(SQLOperator... conditions) {
        return new RxWhere<>(mRealIndexedBy.having(conditions));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<TModel> getTable() {
        return mRealIndexedBy.getTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query getQueryBuilderBase() {
        return mRealIndexedBy.getQueryBuilderBase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQuery() {
        return mRealIndexedBy.getQuery();
    }

    @Override
    public BaseModel.Action getPrimaryAction() {
        return mRealIndexedBy.getPrimaryAction();
    }

}
