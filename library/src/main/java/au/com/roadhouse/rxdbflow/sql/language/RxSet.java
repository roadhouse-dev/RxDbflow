package au.com.roadhouse.rxdbflow.sql.language;

import android.content.ContentValues;
import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Set;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.WhereBase;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import au.com.roadhouse.rxdbflow.sql.observables.DBFlowExecuteObservable;
import rx.Observable;

/**
 * Provides a set block for a select statement
 */
public class RxSet<TModel extends Model> implements WhereBase<TModel>, QueriableObservable, RxTransformable<TModel>{

    Set<TModel> mRealSet;

    public RxSet(Set<TModel> realSet) {
        mRealSet = realSet;
    }

    /**
     * Specifies a varg of conditions to append to this SET
     *
     * @param conditions The varg of conditions
     * @return This instance.
     */
    public RxSet<TModel> conditions(SQLCondition... conditions) {
        mRealSet.conditions(conditions);
        return this;
    }

    /**
     * Specifies a set of content values to append to this SET as Conditions
     *
     * @param contentValues The set of values to append.
     * @return This instance.
     */
    public RxSet<TModel> conditionValues(ContentValues contentValues) {
        mRealSet.conditionValues(contentValues);
        return this;
    }

    /**
     * Begins completing the rest of this SET statement.
     *
     * @param conditions The conditions to fill the WHERE with.
     * @return The where piece of this query.
     */
    public RxWhere<TModel> where(SQLCondition... conditions) {
        Where<TModel> where = mRealSet.where(conditions);
        return new RxWhere<>(where);
    }

    @Override
    public RxWhere<TModel> groupBy(NameAlias... nameAliases) {
        Where<TModel> where = mRealSet.groupBy(nameAliases);
        return new RxWhere<>(where);
    }

    @Override
    public RxWhere<TModel> groupBy(IProperty... properties) {
        Where<TModel> where = mRealSet.groupBy(properties);
        return new RxWhere<>(where);
    }

    @Override
    public RxWhere<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        Where<TModel> where = mRealSet.orderBy(nameAlias, ascending);
        return new RxWhere<>(where);
    }

    @Override
    public RxWhere<TModel> orderBy(IProperty property, boolean ascending) {
        Where<TModel> where = mRealSet.orderBy(property, ascending);
        return new RxWhere<>(where);
    }

    @Override
    public RxWhere<TModel> orderBy(OrderBy orderBy) {
        Where<TModel> where = mRealSet.orderBy(orderBy);
        return new RxWhere<>(where);
    }

    @Override
    public RxWhere<TModel> limit(int count) {
        Where<TModel> where = mRealSet.limit(count);
        return new RxWhere<>(where);
    }

    @Override
    public RxWhere<TModel> offset(int offset) {
        Where<TModel> where = mRealSet.offset(offset);
        return new RxWhere<>(where);
    }


    @Override
    public RxWhere<TModel> having(SQLCondition... conditions) {
        Where<TModel> where = mRealSet.having(conditions);
        return new RxWhere<>(where);
    }


    @Override
    public String getQuery() {
        return mRealSet.getQuery();
    }

    @Override
    public Class<TModel> getTable() {
        return mRealSet.getTable();
    }

    @Override
    public Query getQueryBuilderBase() {
        return mRealSet.getQueryBuilderBase();
    }


    @Override
    public Observable<Long> asCountObservable() {
        return where().asCountObservable();
    }

    @Override
    public Observable<Long> asCountObservable(DatabaseWrapper databaseWrapper) {
        return where().asCountObservable(databaseWrapper);
    }

    @Override
    public DBFlowExecuteObservable asExecuteObservable() {
        return where().asExecuteObservable();
    }

    @Override
    public Observable<Void> asExecuteObservable(DatabaseWrapper databaseWrapper) {
        return where().asExecuteObservable(databaseWrapper);
    }

    @Override
    public Observable<Cursor> asQueryObservable() {
        return where().asQueryObservable();
    }
}
