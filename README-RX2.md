

# RxDBFlow-Rx2 Sample Usage
RxJava bindings for DBFlow models, and query language


## RxBaseModel
RxBaseModel is an extension of BaseModel and provides RxJava extensions for individual model operations 

```java

@Table(database = ExampleDatabase.class, name = TestModel.NAME)
public class TestModel extends RxBaseModel<TestModel> {
    ...
}
```


```java
    model.saveAsSingle()
        .subscribeOn(DBFlowSchedulers.background())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
        
    model.insertAsSingle()
        .subscribeOn(DBFlowSchedulers.background())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
        
    model.deleteAsSingle()
         .subscribeOn(DBFlowSchedulers.background())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe();
        
    model.updateAsSingle()
         .subscribeOn(DBFlowSchedulers.background())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe();       
```

## Queries

### List observable
```java
RxSQLite.select()
    .from(Table.class)
    .asListSingle()
    .subscribe(...)
```

### Single model observable

```java
RxSQLite.select()
    .from(TableModel.class)
    .limit()
    .asSingle()
    .subscribe(...) 
```

### Rerun query on table changes
In this example the Single transforms into an Observable via restartOnChange()
```java
RxSQLite.select()
    .from(TableModel.class)
    .asSingle()
    .restartOnChange()
    .subscribe(...)
```


### Rerun query on specific table changes
This is particularly useful when dealing with views

```java
RxSQLite.select()
    .from(ViewModel.class)
    .asSingle()
    .restartOnChange(TableModelOne.class, TableModelTwo.class)
    .subscribe(...)
```


### Other observables 
Retrieve result as a Cursor

```java
.asResultSingle() 
```
Retrieve result as a FlowQueryList
```java
.asQueryListSingle()
```


Retrieve a single QueryModel
```java
.asCustomSingle(QueryModel.class)
```


Retrieve results as a list of QueryModel
```java
.asCustomListSingle(QueryModel.class)
```



## CRUD Operations

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

## Transactions

RxDBFlow adds to DBFlow's transaction handling by providing two easy to use transaction observable structures, 
depending what type of operations are being performed in the transaction:

### RxGenericTransactionBlock
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

### RxModelOperationTransaction
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

