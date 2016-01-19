package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public abstract class DbTable<T> implements BaseColumns {

    /**
     * @return Name of the table.
     */
    public abstract String getName();

    /**
     * Upgrade the table from old version to new version.
     */
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * Get {@link ContentValues} from an object to save into the database.
     */
    protected abstract ContentValues getContentValues(T objectToSave);
}
