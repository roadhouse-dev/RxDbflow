package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public class RxJoin<TModel extends Model, TFromModel extends Model> implements Query {

    private Join<TModel, TFromModel> mRealJoin;
    private RxFrom<TFromModel> mRxFrom;

    public RxJoin(RxFrom<TFromModel> from, Join<TModel, TFromModel> join){
        mRxFrom = from;
        mRealJoin = join;
    }

    @Override
    public String getQuery() {
        return mRealJoin.getQuery();
    }

    public RxFrom<TFromModel> natural(){
        mRealJoin.natural();
        return mRxFrom;
    }

    public RxFrom<TFromModel> on(SQLCondition... onConditions){
        mRealJoin.on(onConditions);
        return mRxFrom;
    }

    public RxFrom<TFromModel> using(IProperty... columns) {
        mRealJoin.using(columns);
        return mRxFrom;
    }
}
