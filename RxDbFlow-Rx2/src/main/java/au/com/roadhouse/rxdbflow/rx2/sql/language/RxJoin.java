package au.com.roadhouse.rxdbflow.rx2.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

/**
 * Constructs a join clause for a SQL statement
 * @param <TModel> The table model class to join to
 * @param <TFromModel> The table model class being joined to
 */
public class RxJoin<TModel, TFromModel> implements Query {

    private Join<TModel, TFromModel> mRealJoin;
    private RxFrom<TFromModel> mRxFrom;

    RxJoin(RxFrom<TFromModel> from, Join<TModel, TFromModel> join){
        mRxFrom = from;
        mRealJoin = join;
    }

    @Override
    public String getQuery() {
        return mRealJoin.getQuery();
    }

    /**
     * Creates a "on" clause when joining two tables
     * @param onConditions The conditions to use when joining the tables
     * @return A RxFrom clause
     */
    public RxFrom<TFromModel> on(SQLOperator... onConditions){
        mRealJoin.on(onConditions);
        return mRxFrom;
    }

    /**
     * Creates a using clause when joining two tables
     * @param columns The columns to use when joining
     * @return A RxFrom instance
     */
    public RxFrom<TFromModel> using(IProperty... columns) {
        mRealJoin.using(columns);
        return mRxFrom;
    }
}
