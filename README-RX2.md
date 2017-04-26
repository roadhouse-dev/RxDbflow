

# RxDBFlow-Rx1 Sample Usage
RxJava bindings for DBFlow models, and query language


##RxBaseModel
RxBaseModel is an extension of BaseModel and provides RxJava extensions for individual model operations 

```java

@Table(database = ExampleDatabase.class, name = TestModel.NAME)
public class TestModel extends RxBaseModel<TestModel> {
    ...
}
```


```java
    model.saveAsObservable()
        .subscribeOn(DBFlowSchedulers.background())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
        
    model.insertAsObservable()
        .subscribeOn(DBFlowSchedulers.background())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
        
    model.deleteAsObservable()
         .subscribeOn(DBFlowSchedulers.background())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe();
        
    model.updateAsObservable()
         .subscribeOn(DBFlowSchedulers.background())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe();       
```

##Queries

###List observable
```java
RxSQLite.select()
    .from(Table.class)
    .asListObservable()
    .subscribe(...)
```

###Single model observable

```java
RxSQLite.select()
    .from(TableModel.class)
    .limit()
    .asSingleObservable()
    .subscribe(...) 
```

###Rerun query on table changes

```java
RxSQLite.select()
    .from(TableModel.class)
    .asSingleObservable()
    .restartOnChange()
    .subscribe(...)
```

###Automatically Unsubscribe after initial results

```java
//Do not use in conjunction with restartOnChange()
RxSQLite.select()
    .from(TableModel.class)
    .asSingleObservable()
    .completeOnResult()
    .subscribe(...)
```


###Rerun query on specific table changes

```java
RxSQLite.select()
    .from(ViewModel.class)
    .asSingleObservable()
    .restartOnChange(TableModelOne.class, TableModelTwo.class)
    .subscribe(...)
```


###Other observables 
Retrieve result as a Cursor

```java
.asResultsObservable() 
```
Retrieve result as a FlowQueryList
```java
.asQueryListObservable()
```


Retrieve a single QueryModel
```java
.asCustomSingleObservable(QueryModel.class)
```


Retrieve results as a list of QueryModel
```java
.asCustomListObservable(QueryModel.class)
```



##CRUD Operations

As of DBFlow 3.1.1 the following will not trigger any onChange listeners
```java
RxSQLite.update(TestModel.class)
                .set(TestModel_Table.first_name.eq("UPDATED"))
                .where(TestModel_Table.first_name.eq("Bob"))
                .asExecuteObservable()
                .subscribe();
 ```

Adding .publishTableUpdates() will trigger any onChangeListeners that might be listening to changes on the current table

```java
RxSQLite.update(TestModel.class)
                .set(TestModel_Table.first_name.eq("UPDATED"))
                .where(TestModel_Table.first_name.eq("Bob"))
                .asExecuteObservable()
                .publishTableUpdates()
                .subscribe();
 ```

##Transactions

RxDBFlow provides two easy to use transaction observable structures, depending what type of operations are being performed in the transaction:

###RxGenericTransactionBlock
Wraps any type of database operations within a transaction

```java
  new RxGenericTransactionBlock.Builder(TestModel.class)
                .addOperation(new RxGenericTransactionBlock.TransactionOperation() {
                    @Override
                    public boolean onProcess(DatabaseWrapper databaseWrapper) {
                        TestModel test = new TestModel();
                        test.setFirstName("Bob");
                        test.setLastName("Marley");
                        test.insert();
                        return true;
                    }
                })
                .addOperation(new RxGenericTransactionBlock.TransactionOperation() {
                    @Override
                    public boolean onProcess(DatabaseWrapper databaseWrapper) {
                        TestModel test = new TestModel();
                        test.setFirstName("John");
                        test.setLastName("Doe");
                        test.insert();

                        return true;
                    }
                })
                .addOperation(new RxGenericTransactionBlock.TransactionOperation() {
                    @Override
                    public boolean onProcess(DatabaseWrapper databaseWrapper) {
                        TestModel test = new TestModel();
                        test.setFirstName("Elvis");
                        test.setLastName("Presely");
                        test.insert();

                        return true;
                    }
                }).build()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
```

###RxModelOperationTransaction
Wraps a list of models and operations to perform on those models

```java
  new RxModelOperationTransaction.Builder(TestModel.class
                .setDefaultOperation(RxModelOperationTransaction.MODEL_OPERATION_INSERT)
                .addModel(modelOne)
                .addModel(modelTwo)
                .addModel(modelThree)
                .build()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
```

