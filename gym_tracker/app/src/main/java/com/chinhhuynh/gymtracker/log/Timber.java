package com.chinhhuynh.gymtracker.log;

import android.util.Log;

public class Timber {

    public static void e(String tag, String format, String... args) {
        Log.e(tag, String.format(format, args));
    }
}
