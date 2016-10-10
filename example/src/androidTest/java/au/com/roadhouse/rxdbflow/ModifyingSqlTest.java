package au.com.roadhouse.rxdbflow;

import android.support.test.runner.AndroidJUnit4;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import au.com.roadhouse.rxdbflow.example.model.TestModel;
import au.com.roadhouse.rxdbflow.example.model.TestModel_Table;
import au.com.roadhouse.rxdbflow.sql.language.RxSQLite;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

@RunWith(AndroidJUnit4.class)
public class ModifyingSqlTest {
    private CompositeSubscription mDataSubscribers;

    private boolean mIsRunningASyncTask = false;
    private boolean mWasATaskSyncSuccessful = false;

    @Before
    public void setup() {

        mDataSubscribers = new CompositeSubscription();
        SQLite.delete(TestModel.class).execute();

        TestModel modelOne = new TestModel();
        modelOne.setFirstName("Bob");
        modelOne.setLastName("Marley");
        modelOne.save();

        TestModel modelTwo = new TestModel();
        modelTwo.setFirstName("John");
        modelTwo.setLastName("Doe");
        modelTwo.save();

        TestModel modelThree = new TestModel();
        modelThree.setFirstName("Bob");
        modelThree.setLastName("Don");
        modelThree.save();
    }

    @Test
    public void testUpdates() {
        RxSQLite.update(TestModel.class)
                .set(TestModel_Table.first_name.eq("UPDATED"))
                .where(TestModel_Table.first_name.eq("Bob"))
                .asExecuteObservable()
                .publishTableUpdates()
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        SQLite.select()
                                .from(TestModel.class)
                                .where(TestModel_Table.first_name.eq("UPDATED"))
                                .queryList();
                    }
                });
    }

    @Test
    public void testDeletes() {

    }

    @Test
    public void testInserts() {

    }
}
