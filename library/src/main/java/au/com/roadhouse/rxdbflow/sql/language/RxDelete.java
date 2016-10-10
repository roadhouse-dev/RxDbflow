package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.structure.Model;


public class RxDelete {
    private Delete mRealDelete;

    public RxDelete(){
        mRealDelete = new Delete();
    }

    public <TModel extends Model> RxFrom from(Class<TModel> table){
        //noinspection unchecked
        return new RxFrom(mRealDelete.from(table));
    }
}
