package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public class RxSelect implements Query{

    private Select mRealSelect;

    RxSelect(IProperty... properties){
        mRealSelect = new Select(properties);
    }

    public <TModel extends Model> RxFrom<TModel> from(Class<TModel> table) {
        return new RxFrom<>(mRealSelect.from(table));
    }

    public RxSelect distinct() {
        mRealSelect = mRealSelect.distinct();
        return this;
    }

    @Override
    public String getQuery() {
        return mRealSelect.getQuery();
    }

    @Override
    public String toString() {
        return getQuery();
    }
}
