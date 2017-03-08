package au.com.roadhouse.rxdbflow.sql.language;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.WhereBase;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IndexProperty;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;

import java.util.List;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowObservable;
import io.reactivex.Observable;

/**
 * Constructs a From clause for a SQL query
 */
public class RxFrom<TModel> extends BaseModelQueriableObservable<TModel> implements Query,
        WhereBase<TModel>,  RxTransformable<TModel>, ModelQueriableObservable<TModel> {

    private From<TModel> mRealFrom;

    RxFrom(From<TModel> realFrom) {
        super(realFrom);
        mRealFrom = realFrom;
    }

    /**
     * The alias that this table name we use
     *
     * @param alias
     * @return This FROM statement
     */
    public RxFrom<TModel> as(String alias) {
        mRealFrom = mRealFrom.as(alias);
        return this;
    }

    /**
     * Adds a join on a specific table for this asQueryObservable
     *
     * @param table    The table this corresponds to
     * @param joinType The type of join to use
     * @return The join contained in this FROM statement
     */
    public <TJoin extends Model> RxJoin<TJoin, TModel> join(Class<TJoin> table, @NonNull Join.JoinType joinType) {
        return new RxJoin<>(this, mRealFrom.join(table, joinType));
    }

    public <TJoin extends Model> RxJoin<TJoin, TModel>
    join(ModelQueriable<TJoin> modelQueriable, @NonNull Join.JoinType joinType) {
        return new RxJoin<>(this, mRealFrom.join(modelQueriable, joinType));
    }

    /**
     * Adds a {@link Join.JoinType#CROSS} join on a specific table for this asQueryObservable.
     *
     * @param table   The table to join on.
     * @param <TJoin> The class of the join table.
     * @return The join contained in this FROM statement.
     */
    public <TJoin extends Model> RxJoin<TJoin, TModel> crossJoin(Class<TJoin> table) {
        return join(table, Join.JoinType.CROSS);
    }

    /**
     * Adds a {@link Join.JoinType#CROSS} join on a specific table for this asQueryObservable.
     *
     * @param modelQueriable The asQueryObservable to join on.
     * @param <TJoin>        The class of the join table.
     * @return The join contained in this FROM statement.
     */
    public <TJoin extends Model> RxJoin<TJoin, TModel> crossJoin(ModelQueriable<TJoin> modelQueriable) {
        return join(modelQueriable, Join.JoinType.CROSS);
    }

    /**
     * Adds a {@link Join.JoinType#INNER} join on a specific table for this asQueryObservable.
     *
     * @param table   The table to join on.
     * @param <TJoin> The class of the join table.
     * @return The join contained in this FROM statement.
     */
    public <TJoin extends Model> RxJoin<TJoin, TModel> innerJoin(Class<TJoin> table) {
        return join(table, Join.JoinType.INNER);
    }

    /**
     * Adds a {@link Join.JoinType#INNER} join on a specific table for this asQueryObservable.
     *
     * @param modelQueriable The asQueryObservable to join on.
     * @param <TJoin>        The class of the join table.
     * @return The join contained in this FROM statement.
     */
    public <TJoin extends Model> RxJoin<TJoin, TModel> innerJoin(ModelQueriable<TJoin> modelQueriable) {
        return join(modelQueriable, Join.JoinType.INNER);
    }

    /**
     * Adds a {@link Join.JoinType#LEFT_OUTER} join on a specific table for this asQueryObservable.
     *
     * @param table   The table to join on.
     * @param <TJoin> The class of the join table.
     * @return The join contained in this FROM statement.
     */
    public <TJoin extends Model> RxJoin<TJoin, TModel> leftOuterJoin(Class<TJoin> table) {
        return join(table, Join.JoinType.LEFT_OUTER);
    }

    /**
     * Adds a {@link Join.JoinType#LEFT_OUTER} join on a specific table for this asQueryObservable.
     *
     * @param modelQueriable The asQueryObservable to join on.
     * @param <TJoin>        The class of the join table.
     * @return The join contained in this FROM statement.
     */
    public <TJoin extends Model> RxJoin<TJoin, TModel> leftOuterJoin(ModelQueriable<TJoin> modelQueriable) {
        return join(modelQueriable, Join.JoinType.LEFT_OUTER);
    }

    /**
     * @return an empty {@link Where} statement
     */
    public RxWhere<TModel> where() {
        return new RxWhere<>(mRealFrom.where());
    }

    /**
     * @param conditions The array of conditions that define this WHERE statement
     * @return A {@link Where} statement with the specified array of {@link com.raizlabs.android.dbflow.sql.language.Condition}.
     */
    public RxWhere<TModel> where(SQLCondition... conditions) {
        return where().andAll(conditions);
    }

    /**
     * Begins an INDEXED BY piece of this asQueryObservable with the specified name.
     *
     * @param indexProperty The index property generated.
     * @return An INDEXED BY piece of this statement
     */
    public RxIndexedBy<TModel> indexedBy(IndexProperty<TModel> indexProperty) {
        return new RxIndexedBy<>(mRealFrom.indexedBy(indexProperty));
    }

    @Override
    public String getQuery() {
        return mRealFrom.getQuery();
    }

    @Override
    public Class<TModel> getTable() {
        return mRealFrom.getTable();
    }

    /**
     * @return The base asQueryObservable, usually a {@link com.raizlabs.android.dbflow.sql.language.Delete}, {@link
     * com.raizlabs.android.dbflow.sql.language.Select}, or {@link com.raizlabs.android.dbflow.sql.language.Update}
     */
    @Override
    public Query getQueryBuilderBase() {
        return mRealFrom.getQueryBuilderBase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> groupBy(NameAlias... nameAliases) {
        return where().groupBy(nameAliases);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> groupBy(IProperty... properties) {
        return where().groupBy(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        return where().orderBy(nameAlias, ascending);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(IProperty property, boolean ascending) {
        return where().orderBy(property, ascending);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> orderBy(OrderBy orderBy) {
        return where().orderBy(orderBy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> limit(int count) {
        return where().limit(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> offset(int offset) {
        return where().offset(offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RxWhere<TModel> having(SQLCondition... conditions) {
        return this.where().having(conditions);
    }

    @Override
    public DBFlowObservable<Cursor> asQueryObservable() {
        return where().asQueryObservable();
    }

    @Override
    public DBFlowObservable<Long> asCountObservable() {
        return where().asCountObservable();
    }

    @Override
    public <AQueryModel extends BaseQueryModel> Observable<AQueryModel> asCustomSingleObservable(Class<AQueryModel> customClazz) {
        return null;
    }

    @Override
    public <AQueryModel extends BaseQueryModel> Observable<List<AQueryModel>> asCustomListObservable(Class<AQueryModel> customClazz) {
        return null;
    }
}
