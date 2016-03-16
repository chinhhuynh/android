package com.chinhhuynh.gymtracker;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public final class LockScreenService extends Service {

    private Context mContext;
    private IBinder mBinder;
    private WindowManager mWindowManager;
    private BroadcastReceiver mBroadcastReceiver;
    private View mLockScreenView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        mBinder = new Binder();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mBroadcastReceiver = new ScreenBroadcastReceiver();
        mLockScreenView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.widget, null);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        stopSelf();
    }

    private void addLockScreenView() {
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.RGBA_8888);

        mWindowManager.addView(mLockScreenView, mLayoutParams);
    }

    private void removeLockScreenView() {
        mWindowManager.removeView(mLockScreenView);
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                addLockScreenView();
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                removeLockScreenView();
            }
        }
    }
}
