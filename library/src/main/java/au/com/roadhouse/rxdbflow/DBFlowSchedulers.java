package au.com.roadhouse.rxdbflow;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


public class DBFlowSchedulers {

    private static Scheduler sScheduler;

    /**
     * A single threaded background scheduler, this should be used for dbflow observable operations
     * to avoid slamming the connection pool with  multiple connection.
     *
     * @return A DBFlow background scheduler
     */
    public static Scheduler background() {
        if (sScheduler == null) {
            BlockingQueue<Runnable> threadQueue = new ArrayBlockingQueue<>(200);
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, threadQueue);
            sScheduler = Schedulers.from(threadPoolExecutor);
        }

        return sScheduler;
    }
}