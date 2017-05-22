package au.com.roadhouse.rxdbflow.rx2.sql.transaction;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides database transaction control
 */
public class DbTransaction {

    private final Class mClass;
    private static Map<String, List<OnDbTransactionListener>> sTransactionListenerMap = new HashMap<>();

    public DbTransaction(Class tableClass){
        mClass = tableClass;
    }

    public static void registerDbTransactionListener(Class tableClass, OnDbTransactionListener listener){
        String databaseName = FlowManager.getDatabase(tableClass).getDatabaseName();
        if(sTransactionListenerMap.containsKey(databaseName)){
            sTransactionListenerMap.get(databaseName).add(listener);
        } else {
            List<OnDbTransactionListener> listenerList = new ArrayList<>();
            listenerList.add(listener);
            sTransactionListenerMap.put(databaseName, listenerList);
        }
    }

    public static void unregisterDbTransactionListener(Class tableClass, OnDbTransactionListener listener) {
        String databaseName = FlowManager.getDatabase(tableClass).getDatabaseName();
        if(sTransactionListenerMap.containsKey(databaseName)){
            sTransactionListenerMap.get(databaseName).remove(databaseName);
            if(sTransactionListenerMap.get(databaseName).size() == 0){
                sTransactionListenerMap.remove(databaseName);
            }
        } else {
            Log.e("DbTransaction", "Attempting to unregister a listener which was not registered");

        }
    }

    public void beginTransaction(){
        FlowManager.getDatabase(mClass).getWritableDatabase().beginTransaction();
        String databaseName = FlowManager.getDatabase(mClass).getDatabaseName();
        if(sTransactionListenerMap.containsKey(databaseName)){
            List<OnDbTransactionListener> listenerList = sTransactionListenerMap.get(databaseName);
            for(int i = 0; i < listenerList.size(); i++){
                listenerList.get(i).onTransactionStarted();
            }
        }
    }

    public void setTransactionSuccessful(){
        FlowManager.getDatabase(mClass).getWritableDatabase().setTransactionSuccessful();
    }

    public void endTransaction() {
        FlowManager.getDatabase(mClass).getWritableDatabase().endTransaction();
        String databaseName = FlowManager.getDatabase(mClass).getDatabaseName();
        if(sTransactionListenerMap.containsKey(databaseName)){
            List<OnDbTransactionListener> listenerList = sTransactionListenerMap.get(databaseName);
            for(int i = 0; i < listenerList.size(); i++){
                listenerList.get(i).onTransactionEnded();
            }
        }
    }

    public interface OnDbTransactionListener {
        void onTransactionStarted();
        void onTransactionEnded();
    }
}
