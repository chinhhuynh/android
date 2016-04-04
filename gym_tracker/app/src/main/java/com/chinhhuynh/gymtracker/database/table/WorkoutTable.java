package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.WorkerThread;

import com.chinhhuynh.gymtracker.database.DatabaseHelper;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import utils.ThreadUtils;

public final class WorkoutTable extends DbTable<ExerciseSummary> {

    public static final int COL_IDX_ID = 0;
    public static final int COL_IDX_EXERCISE_NAME = 1;
    public static final int COL_IDX_EXERCISE_MUSCLE_GROUP = 2;
    public static final int COL_IDX_SET_COUNT = 3;
    public static final int COL_IDX_WEIGHT = 4;
    public static final int COL_IDX_START_TIME = 5;
    public static final int COL_IDX_WORKOUT_DURATION = 6;
    public static final int COL_IDX_REST_DURATION = 7;
    public static final int COL_IDX_EXERCISE_ICON_FILE_NAME = 8;

    private static final String TABLE_NAME = "Workout";

    private static final String COL_EXERCISE_NAME = "exercise_name";
    private static final String COL_EXERCISE_MUSCLE_GROUP = "exercise_muscle_group";
    private static final String COL_SET_COUNT = "set_count";
    private static final String COL_WEIGHT = "weight";
    private static final String COL_START_TIME = "start_time";
    private static final String COL_WORKOUT_DURATION = "workout_duration";
    private static final String COL_REST_DURATION = "rest_duration";

    private static final String[][] COLUMNS = {
            { _ID, DataType.TEXT },
            { COL_EXERCISE_NAME, DataType.TEXT },
            { COL_EXERCISE_MUSCLE_GROUP, DataType.TEXT },
            { COL_SET_COUNT, DataType.INTEGER },
            { COL_WEIGHT, DataType.INTEGER },
            { COL_START_TIME, DataType.INTEGER },
            { COL_WORKOUT_DURATION, DataType.INTEGER },
            { COL_REST_DURATION, DataType.INTEGER },
    };

    private static final String[] PROJECTION = {
            _ID,
            COL_EXERCISE_NAME,
            COL_EXERCISE_MUSCLE_GROUP,
            COL_SET_COUNT,
            COL_WEIGHT,
            COL_START_TIME,
            COL_WORKOUT_DURATION,
            COL_REST_DURATION,
    };

    private static String SQL_QUERY_BY_TIME_RANGE =
            "SELECT workout.*, exercise.%s " +
            "FROM %s workout INNER JOIN %s exercise " +
            "ON workout.%s=exercise.%s AND workout.%s=exercise.%s " +
            "WHERE workout.%s BETWEEN %s and %s " +
            "ORDER BY %s DESC";

    private static WorkoutTable INSTANCE;

    public static WorkoutTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WorkoutTable();
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
        // no-op.
    }

    @Override
    protected ContentValues getContentValues(ExerciseSummary summary) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EXERCISE_NAME, summary.exercise.mExerciseName);
        contentValues.put(COL_EXERCISE_MUSCLE_GROUP, summary.exercise.mMuscleGroup);
        contentValues.put(COL_SET_COUNT, summary.set);
        contentValues.put(COL_WEIGHT, summary.weight);
        contentValues.put(COL_START_TIME, summary.startTime);
        contentValues.put(COL_WORKOUT_DURATION, summary.durationSec);
        contentValues.put(COL_REST_DURATION, summary.restDurationSec);
        return contentValues;
    }

    @Override
    protected String getUniqueString() {
        return "UNIQUE(" + COL_START_TIME + ") ON CONFLICT REPLACE";
    }

    @WorkerThread
    public void saveWorkout(ExerciseSummary summary) {
        ThreadUtils.assertBackgroundThread();
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        db.insert(TABLE_NAME, null, getContentValues(summary));
    }

    @WorkerThread
    public static Cursor queryByTimeRange(long start, long end) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();

        String query = String.format(SQL_QUERY_BY_TIME_RANGE, ExerciseTable.COL_ICON_FILE_NAME, TABLE_NAME,
                ExerciseTable.TABLE_NAME, COL_EXERCISE_NAME, ExerciseTable.COL_NAME, COL_EXERCISE_MUSCLE_GROUP,
                ExerciseTable.COL_MUSCLE_GROUP, COL_START_TIME, String.valueOf(start), String.valueOf(end), COL_START_TIME);

        return db.rawQuery(query, null);
    }
}
