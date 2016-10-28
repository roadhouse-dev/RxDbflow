package au.com.roadhouse.rxdbflow.example.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import au.com.roadhouse.rxdbflow.structure.RxBaseModel;

@ModelContainer
@Table(database = ExampleDatabase.class, name = TestModel.NAME, insertConflict = ConflictAction.REPLACE)
public class TestModel extends RxBaseModel<TestModel> {

    public static final String NAME = "test";

    @PrimaryKey(autoincrement = true)
    @Column(name = "id", getterName = "getId", setterName = "setId")
    private long mId;

    @Column(name = "first_name", getterName = "getFirstName", setterName = "setFirstName")
    private String mFirstName;

    @Column(name = "last_name", getterName = "getLastName", setterName = "setLastName")
    private String mLastName;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }
}