package com.chinhhuynh.gymtracker;

import android.app.Application;
import android.content.Context;

public class GymTrackerApplication extends Application {

    private static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        GymTrackerApplication.CONTEXT = getApplicationContext();
    }

    public static Context getAppContext() {
        return CONTEXT;
    }
}
