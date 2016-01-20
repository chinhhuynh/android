package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExerciseTable extends DbTable<Exercise> {

    private static final String NAME = "Exercise";

    private static final String COL_NAME = "name";
    private static final String COL_ICON_PATH = "icon_path";

    private static final int COL_IDX_ID = 0;
    private static final int COL_IDX_NAME = 1;
    private static final int COL_IDX_ICON_PATH = 2;

    private static final String[][] COLUMNS = {
            { _ID, DataType.TEXT },
            { COL_NAME, DataType.TEXT },
            { COL_ICON_PATH, DataType.TEXT },
    };

    private static ExerciseTable INSTANCE;

    public static ExerciseTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExerciseTable();
        }
        return INSTANCE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[][] getColumns() {
        return COLUMNS;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    protected ContentValues getContentValues(Exercise objectToSave) {
        return null;
    }
}
