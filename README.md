# RxDbflow
RxJava bindings for DBFlow models, and query language

#Download
Available soon

#Build
```bash
$ git clone https://github.com/roadhouse-dev/RxDbflow.git
$ ./gradlew build
```

#DBFlow documentation
For help with DBFlow please take a look at [DBFlow Github](https://github.com/Raizlabs/DBFlow)

#Sample usage

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
.asQueryListObservable
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

Adding .publishTableUpdates() will trigger any onChangeListers that might be listening to changes on the current table

```java
RxSQLite.update(TestModel.class)
                .set(TestModel_Table.first_name.eq("UPDATED"))
                .where(TestModel_Table.first_name.eq("Bob"))
                .asExecuteObservable()
                .publishTableUpdates()
                .subscribe();
 ```

##Transactions

RxDBFlow provides two easy to use transaction observable structures, depending what type of operations being performed in the transaction:

###RxGenericTransactionBlock
Wraps any type of database operations within a transaction

```java
mDataSubscribers.add(new RxGenericTransactionBlock.Builder(TestModel.class)
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



#Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/roadhouse-dev/RxDbflow/issues).

#Pull Requests
All pull requests are welcome, however to make the whole process smoother please use the following guides

* All pull requests should be against the ```develop``` branch
* Code formatting should match the default Android Studio format
* Limit code changes to the scope of what you're implementing
* Provide standard JavaDoc for any public accessible members and classes