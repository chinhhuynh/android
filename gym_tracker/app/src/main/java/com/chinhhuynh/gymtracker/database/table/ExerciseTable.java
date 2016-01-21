package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinhhuynh.gymtracker.database.DatabaseHelper;
import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExerciseTable extends DbTable<Exercise> {

    public static final int COL_IDX_ID = 0;
    public static final int COL_IDX_NAME = 1;
    public static final int COL_IDX_ICON_PATH = 2;

    private static final String TABLE_NAME = "Exercise";

    private static final String COL_NAME = "name";
    private static final String COL_ICON_PATH = "icon_path";

    private static final String[][] COLUMNS = {
            { _ID, DataType.TEXT },
            { COL_NAME, DataType.TEXT },
            { COL_ICON_PATH, DataType.TEXT },
    };

    private static final String[] PROJECTION = {
            _ID,
            COL_NAME,
            COL_ICON_PATH,
    };

    private static final Exercise[] EXERCISES = {
            new Exercise("Barbell Biceps Curls", "barbell_biceps_curls_icon.png")
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
        return TABLE_NAME;
    }

    @Override
    public String[][] getColumns() {
        return COLUMNS;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                prepopulateData(db);
        }
    }

    @Override
    protected ContentValues getContentValues(Exercise exercise) {
        return null;
    }

    public static Cursor queryByName(String... nameQuery) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        String selection = ExerciseTable.COL_NAME + " = ?";

        return db.query(ExerciseTable.TABLE_NAME, PROJECTION /*columns*/, selection /*selection*/,
                nameQuery /*selectionArgs*/, null /*groupBy*/, null /*having*/, null /*orderBy*/);
    }

    private void prepopulateData(SQLiteDatabase db) {
        for (Exercise exercise : EXERCISES) {
            db.insert(TABLE_NAME, null, getContentValues(exercise));
        }
    }
}
