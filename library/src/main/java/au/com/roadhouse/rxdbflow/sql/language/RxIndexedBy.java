package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.IndexedBy;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.WhereBase;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public class RxIndexedBy <TModel extends Model> implements WhereBase<TModel>, RxTransformable<TModel> {

    IndexedBy<TModel> mRealIndexedBy;

    public RxIndexedBy(IndexedBy<TModel> realIndexedBy) {
        mRealIndexedBy = realIndexedBy;
    }

    @Override
    public RxWhere<TModel> groupBy(NameAlias... nameAliases) {
        return new RxWhere<>(mRealIndexedBy.groupBy(nameAliases));
    }

    @Override
    public RxWhere<TModel> groupBy(IProperty... properties) {
        return new RxWhere<>(mRealIndexedBy.groupBy(properties));
    }

    @Override
    public RxWhere<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        return new RxWhere<>(mRealIndexedBy.orderBy(nameAlias, ascending));
    }

    @Override
    public RxWhere<TModel> orderBy(IProperty property, boolean ascending) {
        return new RxWhere<>(mRealIndexedBy.orderBy(property, ascending));
    }

    @Override
    public RxWhere<TModel> orderBy(OrderBy orderBy) {
        return new RxWhere<>(mRealIndexedBy.orderBy(orderBy));
    }

    @Override
    public RxWhere<TModel> limit(int count) {
        return new RxWhere<>(mRealIndexedBy.limit(count));
    }

    @Override
    public RxWhere<TModel> offset(int offset) {
        return new RxWhere<>(mRealIndexedBy.offset(offset));
    }

    @Override
    public RxWhere<TModel> having(SQLCondition... conditions) {
        return new RxWhere<>(mRealIndexedBy.having(conditions));
    }

    @Override
    public Class<TModel> getTable() {
        return mRealIndexedBy.getTable();
    }

    @Override
    public Query getQueryBuilderBase() {
        return mRealIndexedBy.getQueryBuilderBase();
    }

    @Override
    public String getQuery() {
        return mRealIndexedBy.getQuery();
    }
}
