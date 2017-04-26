package au.com.roadhouse.rxdbflow.rx1;

import com.raizlabs.android.dbflow.config.FlowManager;

import au.com.roadhouse.rxdbflow.rx1.structure.RxModelAdapter;

public class RxDbFlow {

    public static <TModel> RxModelAdapter<TModel> getModelAdapter(Class<TModel> modelClass) {
        return new RxModelAdapter<>(FlowManager.getModelAdapter(modelClass));
    }

}
