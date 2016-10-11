package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.structure.Model;

/**
 * Creates a delete SQL statement
 */
 public class RxDelete {
    private Delete mRealDelete;

    RxDelete(){
        mRealDelete = new Delete();
    }

    /**
     * Constructs a from clause for the delete statement
     * @param table The table to delete from
     * @return A RxFrom instance
     */
    public <TModel extends Model> RxFrom from(Class<TModel> table){
        //noinspection unchecked
        return new RxFrom(mRealDelete.from(table));
    }
}
