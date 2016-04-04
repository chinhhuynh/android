package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public abstract class DbTable<T> implements BaseColumns {

    public static DbTable[] TABLES = {
            ExerciseTable.getInstance(),
            WorkoutTable.getInstance(),
    };

    /**
     * @return Name of the table.
     */
    public abstract String getName();

    /**
     * @return Columns of the table in the following format: Name | Type.
     */
    public abstract String[][] getColumns();

    /**
     * Prepopulate data in a new table. This method will be called right after a new table was created.
     */
    public abstract void prepopulateData(SQLiteDatabase db);

    /**
     * Upgrade the table from old version to new version.
     */
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * Get {@link ContentValues} from an object to save into the database.
     */
    protected abstract ContentValues getContentValues(T objectToSave);

    protected abstract String getUniqueString();

    public String getCreateTableQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(getName()).append("(");

        String[][] columns = getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(columns[i][0]).append(" ").append(columns[i][1]);
        }

        String uniqueString = getUniqueString();
        if (uniqueString != null) {
            sb.append(", ");
            sb.append(getUniqueString());
        }

        sb.append(");");
        return sb.toString();
    }
}
