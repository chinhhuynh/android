package utils;

import android.os.Handler;
import android.os.Looper;

import junit.framework.Assert;
import org.apache.http.conn.scheme.HostNameResolver;

public class ThreadUtils {

    private static Handler sMainThreadHandler = new Handler(Looper.getMainLooper());

    public static void assertMainThread() {
        Assert.assertTrue(isOnMainThread());
    }

    public static void assertBackgroundThread() {
        Assert.assertFalse(isOnMainThread());
    }

    public static void runOnMainThread(Runnable runnable) {
        if (isOnMainThread()) {
            runnable.run();
        } else {
            sMainThreadHandler.post(runnable);
        }
    }

    public static void runOnBackgroundThread(Runnable runnable) {
        Executors.BACKGROUND_EXECUTOR.execute(runnable);
    }

    private static boolean isOnMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
