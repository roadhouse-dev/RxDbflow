package au.com.roadhouse.rxdbflow;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import au.com.roadhouse.rxdbflow.structure.RxModelAdapter;

public class RxDbFlow {

    public static <TModel> RxModelAdapter<TModel> getModelAdapter(Class<TModel> modelClass) {
        return new RxModelAdapter<>(FlowManager.getModelAdapter(modelClass));
    }

}
