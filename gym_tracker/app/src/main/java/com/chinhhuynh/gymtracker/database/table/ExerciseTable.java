package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinhhuynh.gymtracker.database.DatabaseHelper;
import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExerciseTable extends DbTable<Exercise> {

    public static final int COL_IDX_ID = 0;
    public static final int COL_IDX_NAME = 1;
    public static final int COL_IDX_ICON_FILE_NAME = 2;

    private static final String TABLE_NAME = "Exercise";

    private static final String COL_NAME = "name";
    private static final String COL_ICON_FILE_NAME = "icon_file_name";

    private static final String[][] COLUMNS = {
            { _ID, DataType.TEXT },
            { COL_NAME, DataType.TEXT },
            { COL_ICON_FILE_NAME, DataType.TEXT },
    };

    private static final String[] PROJECTION = {
            _ID,
            COL_NAME,
            COL_ICON_FILE_NAME,
    };

    private static final Exercise[] EXERCISES = {
            new Exercise(Exercise.EXERCISE_SIT_UP, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON)
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
                // upgrade steps.
        }
    }

    @Override
    public void prepopulateData(SQLiteDatabase db) {
        for (Exercise exercise : EXERCISES) {
            db.insert(TABLE_NAME, null, getContentValues(exercise));
        }
    }

    @Override
    protected ContentValues getContentValues(Exercise exercise) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, exercise.mExerciseName);
        contentValues.put(COL_ICON_FILE_NAME, exercise.mIconFileName);
        return contentValues;
    }

    public static Cursor queryByName(String... queryTokens) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        String[] formattedTokens = new String[queryTokens.length];
        for (int i = 0; i < queryTokens.length; i++) {
            formattedTokens[i] = '%' + queryTokens[i] + '%';
            if (i > 0) {
                sb.append(" OR ");
            }
            sb.append(ExerciseTable.COL_NAME).append(" LIKE ?");
        }
        String selection = sb.toString();

        return db.query(TABLE_NAME, PROJECTION /*columns*/, selection,
                formattedTokens /*selectionArgs*/, null /*groupBy*/, null /*having*/, null /*orderBy*/);
    }
}
