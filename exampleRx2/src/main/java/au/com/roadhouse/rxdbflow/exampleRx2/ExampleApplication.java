package au.com.roadhouse.rxdbflow.exampleRx2;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        FlowManager.init(new FlowConfig.Builder(this).build());
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        FlowManager.destroy();
        super.onTerminate();
    }
}
