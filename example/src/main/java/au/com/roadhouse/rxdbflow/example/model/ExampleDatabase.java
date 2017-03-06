package au.com.roadhouse.rxdbflow.example.model;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.Database;


@Database(name = ExampleDatabase.NAME, version = ExampleDatabase.VERSION, insertConflict = ConflictAction.REPLACE)
public class ExampleDatabase {
    public static final String NAME = "example";
    public static final int VERSION = 2;
}
