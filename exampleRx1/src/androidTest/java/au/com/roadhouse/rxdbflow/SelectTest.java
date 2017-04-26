package au.com.roadhouse.rxdbflow;

import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import rx.subscriptions.CompositeSubscription;

@RunWith(AndroidJUnit4.class)
public class SelectTest {

    private CompositeSubscription mDataSubscribers;

    private boolean mIsRunningASyncTask = false;
    private boolean mWasATaskSyncSuccessful = false;

//    @Before
//    public void setup() {
//        mDataSubscribers = new CompositeSubscription();
//        SQLite.delete(TestModel.class).execute();
//
//        TestModel modelOne = new TestModel();
//        modelOne.setFirstName("Bob");
//        modelOne.setLastName("Marley");
//        modelOne.save();
//
//        TestModel modelTwo = new TestModel();
//        modelTwo.setFirstName("John");
//        modelTwo.setLastName("Doe");
//        modelTwo.save();
//
//        TestModel modelThree = new TestModel();
//        modelThree.setFirstName("Bob");
//        modelThree.setLastName("Don");
//        modelThree.save();
//    }
//
//    @Test
//    public void testSimpleListSelect() throws Exception {
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .asListObservable()
//                .subscribe(new Action1<List<TestModel>>() {
//                    @Override
//                    public void call(List<TestModel> testModels) {
//                        assertTrue(testModels.size() == 3);
//                    }
//                }));
//    }

//Test is disabled due to the actual DBFlow library failing in this situation
//    @Test
//    public void testSimpleResultSelect() throws Exception{
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .asQueryListObservable(false)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<TestModel>>() {
//                    @Override
//                    public void call(List<TestModel> testModels) {
//                        assertTrue(testModels.size() == 3);
//                    }
//                }));
//    }

//    @Test
//    public void testSimpleQuerySelect() throws Exception {
//
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .asQueryObservable()
//                .subscribe(new Action1<Cursor>() {
//                    @Override
//                    public void call(Cursor testModels) {
//                        assertTrue(testModels.getCount() == 3);
//                    }
//                }));
//    }
//
//    @Test
//    public void testSimpleCursorListSelect() throws Exception {
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .asCursorListObservable()
//                .subscribe(new Action1<FlowCursorList<TestModel>>() {
//                    @Override
//                    public void call(FlowCursorList<TestModel> testModels) {
//                        assertTrue(testModels.getCount() == 3);
//                    }
//                }));
//    }
//
//    @Test
//    public void testWhereSelectResults() throws Exception {
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .where(TestModel_Table.first_name.eq("Bob"))
//                .asCursorListObservable()
//                .subscribe(new Action1<FlowCursorList<TestModel>>() {
//                    @Override
//                    public void call(FlowCursorList<TestModel> testModels) {
//                        assertTrue(testModels.getCount() == 2);
//                    }
//                }));
//    }
//
//    @Test
//    public void testWhereSelectNoResults() throws Exception {
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .where(TestModel_Table.first_name.eq("Rocky"))
//                .asListObservable()
//                .subscribe(new Action1<List<TestModel>>() {
//                    @Override
//                    public void call(List<TestModel> testModels) {
//                        assertTrue(testModels.size() == 0);
//                    }
//                }));
//    }
//
//    @Test
//    public void testWhereSelectFirst() throws Exception {
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .limit(1)
//                .asListObservable()
//                .subscribe(new Action1<List<TestModel>>() {
//                    @Override
//                    public void call(List<TestModel> testModels) {
//                        assertTrue(testModels.size() == 1);
//                    }
//                }));
//    }
//
//    @Test
//    public void testWhereSelectFirstSingle() throws Exception {
//        mDataSubscribers.add(RxSQLite.select()
//                .from(TestModel.class)
//                .limit(1)
//                .asSingleObservable()
//                .subscribe(new Action1<TestModel>() {
//                    @Override
//                    public void call(TestModel testModels) {
//                        assertNotNull(testModels);
//                        assertEquals(testModels.getFirstName(), "Bob");
//                        assertEquals(testModels.getLastName(), "Marley");
//                    }
//                }));
//    }
//
//    @Test
//    public void testCaseSelect() throws Exception {
//        mDataSubscribers.add(RxSQLite.select(
//                TestModel_Table.id,
//                RxSQLite
//                        .caseWhen(TestModel_Table.first_name.notEq("Bob")).then("Not Bob")
//                        ._else("Bob")
//                        .end().as("first_name"),
//                TestModel_Table.last_name)
//                .from(TestModel.class)
//                .limit(1)
//                .asListObservable()
//                .subscribe(new Action1<List<TestModel>>() {
//                    @Override
//                    public void call(List<TestModel> testModels) {
//                        for (int i = 0; i < testModels.size(); i++) {
//                            TestModel model = testModels.get(i);
//                            if (!model.getFirstName().equals("Bob")) {
//                                assertEquals(model.getFirstName(), "Not Bob");
//                            }
//                        }
//                    }
//                }));
//    }
//
//    @Test
//    public void testCount() throws Exception {
//        mDataSubscribers.add(RxSQLite.selectCountOf()
//                .from(TestModel.class)
//                .asCountObservable()
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long count) {
//                        assertTrue(count == 3);
//                    }
//                }));
//    }
//
//    @Test
//    public void testGroupByCount() throws Exception {
//        mDataSubscribers.add(RxSQLite.selectCountOf()
//                .from(TestModel.class)
//                .groupBy(TestModel_Table.first_name)
//                .asCountObservable()
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long count) {
//                        assertTrue(count == 2);
//                    }
//                }));
//    }
//
//    @Test
//    public void testSelectMethod() throws Exception {
//        mDataSubscribers.add(
//                RxSQLite.select(
//                        TestModel_Table.first_name,
//                        Method.group_concat(TestModel_Table.last_name).as("last_name"),
//                        Method.sum(TestModel_Table.id).as("id"))
//                        .from(TestModel.class)
//                        .groupBy(TestModel_Table.first_name)
//                        .asListObservable()
//                        .subscribe(new Action1<List<TestModel>>() {
//                            @Override
//                            public void call(List<TestModel> testModels) {
//                                assertTrue(testModels.size() == 2);
//                                for (int i = 0; i < testModels.size(); i++) {
//                                    TestModel model = testModels.get(i);
//                                    if (!model.getFirstName().equals("Bob")) {
//                                        assertEquals(model.getLastName(), "Doe");
//                                    } else {
//                                        assertEquals(model.getLastName(), "Marley,Don");
//                                    }
//                                }
//                            }
//                        }));
//    }
//
//    @Test
//    public void testSelectHaving() throws Exception {
//        mDataSubscribers.add(
//                RxSQLite.select(
//                        TestModel_Table.first_name,
//                        Method.group_concat(TestModel_Table.last_name).as("last_name"),
//                        Method.sum(TestModel_Table.id).as("id"))
//                        .from(TestModel.class)
//                        .groupBy(TestModel_Table.first_name)
//                        .having(TestModel_Table.last_name.eq("Doe"))
//                        .asListObservable()
//                        .subscribe(new Action1<List<TestModel>>() {
//                            @Override
//                            public void call(List<TestModel> testModels) {
//                                assertTrue(testModels.size() == 1);
//                                for (int i = 0; i < testModels.size(); i++) {
//                                    TestModel model = testModels.get(i);
//                                    assertEquals(model.getFirstName(), "John");
//                                    assertEquals(model.getLastName(), "Doe");
//                                }
//                            }
//                        }));
//    }
//
//    @Test
//    public void testSelectWithSubscriptionSave() throws Exception {
//
//        mIsRunningASyncTask = true;
//        mWasATaskSyncSuccessful = true;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if (mIsRunningASyncTask) {
//                    mWasATaskSyncSuccessful = false;
//                }
//                mIsRunningASyncTask = false;
//            }
//        }).start();
//
//        mDataSubscribers.add(
//                RxSQLite.select()
//                        .from(TestModel.class)
//                        .asListObservable()
//                        .restartOnChange()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<List<TestModel>>() {
//                            @Override
//                            public void call(List<TestModel> testModels) {
//                                if (testModels.size() == 3) {
//                                    TestModel modelThree = new TestModel();
//                                    modelThree.setFirstName("New");
//                                    modelThree.setLastName("Insertion");
//                                    modelThree.save();
//                                } else if (testModels.size() == 4) {
//                                    assertEquals(testModels.get(3).getFirstName(), "New");
//                                    assertEquals(testModels.get(3).getLastName(), "Insertion");
//                                    mIsRunningASyncTask = false;
//                                }
//                            }
//                        }));
//
//        while (mIsRunningASyncTask) ;
//
//        if (!mWasATaskSyncSuccessful) {
//            assertFalse("Callback failed to occur within 1 second", true);
//        }
//    }
//
//
//    @Test
//    public void testSelectWithSubscriptionInsert() throws Exception {
//
//        mIsRunningASyncTask = true;
//        mWasATaskSyncSuccessful = true;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if (mIsRunningASyncTask) {
//                    mWasATaskSyncSuccessful = false;
//                }
//                mIsRunningASyncTask = false;
//            }
//        }).start();
//
//        mDataSubscribers.add(
//                RxSQLite.select()
//                        .from(TestModel.class)
//                        .asListObservable()
//                        .restartOnChange()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<List<TestModel>>() {
//                            @Override
//                            public void call(List<TestModel> testModels) {
//                                if (testModels.size() == 3) {
//                                    TestModel modelThree = new TestModel();
//                                    modelThree.setFirstName("New");
//                                    modelThree.setLastName("Insertion");
//                                    modelThree.insert();
//                                } else if (testModels.size() == 4) {
//                                    assertEquals(testModels.get(3).getFirstName(), "New");
//                                    assertEquals(testModels.get(3).getLastName(), "Insertion");
//                                    mIsRunningASyncTask = false;
//                                }
//                            }
//                        }));
//
//        while (mIsRunningASyncTask) ;
//
//        if (!mWasATaskSyncSuccessful) {
//            assertFalse("Callback failed to occur within 1 second", true);
//        }
//    }
//
//
//    @Test
//    public void testSelectWithSubscriptionRemove() throws Exception {
//
//        mIsRunningASyncTask = true;
//        mWasATaskSyncSuccessful = true;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if(mIsRunningASyncTask) {
//                    mWasATaskSyncSuccessful = false;
//                }
//                mIsRunningASyncTask = false;
//            }
//        }).start();
//
//        mDataSubscribers.add(
//                RxSQLite.select()
//                        .from(TestModel.class)
//                        .asListObservable()
//                        .restartOnChange()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<List<TestModel>>() {
//                            @Override
//                            public void call(List<TestModel> testModels) {
//                                if (testModels.size() == 3) {
//                                    //noinspection ConstantConditions
//                                    SQLite.select().from(TestModel.class).limit(1).querySingle().delete();
//                                } else if (testModels.size() == 2) {
//                                    mIsRunningASyncTask = false;
//                                }
//                            }
//                        }));
//
//        while (mIsRunningASyncTask) ;
//
//        if (!mWasATaskSyncSuccessful) {
//            assertFalse("Callback failed to occur within 1 second", true);
//        }
//    }
//
//    @Test
//    public void testSelectWithSubscriptionUpdate() throws Exception {
//
//        mIsRunningASyncTask = true;
//        mWasATaskSyncSuccessful = true;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if(mIsRunningASyncTask) {
//                    mWasATaskSyncSuccessful = false;
//                }
//                mIsRunningASyncTask = false;
//            }
//        }).start();
//        mDataSubscribers.add(
//                RxSQLite.select()
//                        .from(TestModel.class)
//                        .asListObservable()
//                        .restartOnChange()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<List<TestModel>>() {
//                            private boolean mHasUpdated = false;
//                            @Override
//                            public void call(List<TestModel> testModels) {
//                                if (!mHasUpdated) {
//                                    mHasUpdated = true;
//                                    //noinspection ConstantConditions
//                                    TestModel model =   testModels.get(0);
//                                    model.setFirstName("TEST");
//                                    model.update();
//                                } else {
//                                    TestModel model =   testModels.get(0);
//                                    assertEquals(model.getFirstName(), "TEST");
//                                    mIsRunningASyncTask = false;
//                                }
//                            }
//                        }));
//
//        while (mIsRunningASyncTask) ;
//
//        if (!mWasATaskSyncSuccessful) {
//            assertFalse("Callback failed to occur within 1 second", true);
//        }
//    }
//
//    @After
//    public void tearDown() {
//        mDataSubscribers.unsubscribe();
//        SQLite.delete(TestModel.class).execute();
//    }
//
//    @Test
//    public void testSelectWithSubscriptionInsert_QueryList() throws Exception {
//
//        mIsRunningASyncTask = true;
//        mWasATaskSyncSuccessful = true;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if (mIsRunningASyncTask) {
//                    mWasATaskSyncSuccessful = false;
//                }
//                mIsRunningASyncTask = false;
//            }
//        }).start();
//
//        mDataSubscribers.add(
//                RxSQLite.select()
//                        .from(TestModel.class)
//                        .asQueryObservable()
//                        .restartOnChange()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<Cursor>() {
//                            @Override
//                            public void call(Cursor testModels) {
//                                if (testModels.getCount() == 3) {
//                                    TestModel modelThree = new TestModel();
//                                    modelThree.setFirstName("New");
//                                    modelThree.setLastName("Insertion");
//                                    modelThree.insert();
//                                } else if (testModels.getCount() == 4) {
//                                    mIsRunningASyncTask = false;
//                                }
//                            }
//                        }));
//
//        while (mIsRunningASyncTask) ;
//
//        if (!mWasATaskSyncSuccessful) {
//            assertFalse("Callback failed to occur within 1 second", true);
//        }
//    }
}
