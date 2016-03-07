package com.chinhhuynh.gymtracker;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class LockScreenService extends Service {

    public class LocalBinder extends Binder {
        LockScreenService getService() {
            return LockScreenService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        View mView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.widget, null);

        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.RGBA_8888);

        mWindowManager.addView(mView, mLayoutParams);
        return START_STICKY;
    }
}
