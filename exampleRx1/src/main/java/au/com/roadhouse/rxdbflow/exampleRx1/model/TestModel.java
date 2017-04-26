package au.com.roadhouse.rxdbflow.exampleRx1.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

@Table(database = au.com.roadhouse.rxdbflow.example.model.ExampleDatabase.class, name = TestModel.NAME, insertConflict = ConflictAction.REPLACE)
public class TestModel {

    public static final String NAME = "test";

    @PrimaryKey(autoincrement = true)
    @Column(name = "id", getterName = "getId", setterName = "setId")
    private long id;

    @Column(name = "first_name", getterName = "getFirstName", setterName = "setFirstName")
    private String firstName;

    @Column(name = "last_name", getterName = "getLastName", setterName = "setLastName")
    private String lastName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}