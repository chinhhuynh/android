package utils;

import android.os.Looper;

import junit.framework.Assert;

public class ThreadUtils {

    public static void assertMainThread() {
        Assert.assertTrue(Looper.getMainLooper().getThread() == Thread.currentThread());
    }

    public static void assertBackgroundThread() {
        Assert.assertFalse(Looper.getMainLooper().getThread() == Thread.currentThread());
    }

    public static void runOnBackgroundThread(Runnable runnable) {
        Executors.BACKGROUND_EXECUTOR.execute(runnable);
    }
}
