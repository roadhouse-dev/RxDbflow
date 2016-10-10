package au.com.roadhouse.rxdbflow.example.model;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.Database;

import au.com.roadhouse.rxdbflow.example.BuildConfig;


@Database(name = ExampleDatabase.NAME, version = ExampleDatabase.VERSION, insertConflict = ConflictAction.REPLACE)
public class ExampleDatabase {
    public static final String NAME = "example";
    public static final int VERSION = 1;
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".sync.provider";
    public static final String BASE_CONTENT_URI = "content://";
}
