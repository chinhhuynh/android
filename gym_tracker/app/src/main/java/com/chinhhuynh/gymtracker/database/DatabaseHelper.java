package com.chinhhuynh.gymtracker.database;

import com.chinhhuynh.gymtracker.GymTrackerApplication;
import com.chinhhuynh.gymtracker.database.table.DbTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GymTracker.db";

    private static DatabaseHelper INSTANCE;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHelper(GymTrackerApplication.getAppContext());
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < DbTable.TABLES.length; i++) {
            DbTable table = DbTable.TABLES[i];
            db.execSQL(table.getCreateTableQuery());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }
}
