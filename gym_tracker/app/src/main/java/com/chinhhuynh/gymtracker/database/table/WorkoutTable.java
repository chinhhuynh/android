package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.WorkerThread;

import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import utils.ThreadUtils;

public final class WorkoutTable extends DbTable<ExerciseSummary> {

    public static final int COL_IDX_ID = 0;
    public static final int COL_IDX_EXERCISE_NAME = 1;
    public static final int COL_IDX_EXERCISE_MUSCLE_GROUP = 2;
    public static final int COL_IDX_SET = 3;
    public static final int COL_IDX_WEIGHT = 4;
    public static final int COL_IDX_START_TIME = 5;
    public static final int COL_IDX_WORKOUT_DURATION = 6;
    public static final int COL_IDX_REST_DURATION = 7;

    private static final String TABLE_NAME = "Workout";

    private static final String COL_EXERCISE_NAME = "exercise_name";
    private static final String COL_EXERCISE_MUSCLE_GROUP = "exercise_muscle_group";
    private static final String COL_SET = "set";
    private static final String COL_WEIGHT = "weight";
    private static final String COL_START_TIME = "start_time";
    private static final String COL_WORKOUT_DURATION = "workout_duration";
    private static final String COL_REST_DURATION = "rest_duration";

    private static final String[][] COLUMNS = {
            { _ID, DataType.TEXT },
            { COL_EXERCISE_NAME, DataType.TEXT },
            { COL_EXERCISE_MUSCLE_GROUP, DataType.TEXT },
            { COL_SET, DataType.INTEGER },
            { COL_WEIGHT, DataType.INTEGER },
            { COL_START_TIME, DataType.INTEGER },
            { COL_WORKOUT_DURATION, DataType.INTEGER },
            { COL_REST_DURATION, DataType.INTEGER },
    };

    private static final String[] PROJECTION = {
            _ID,
            COL_EXERCISE_NAME,
            COL_EXERCISE_MUSCLE_GROUP,
            COL_SET,
            COL_WEIGHT,
            COL_START_TIME,
            COL_WORKOUT_DURATION,
            COL_REST_DURATION,
    };

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
        contentValues.put(COL_SET, summary.set);
        contentValues.put(COL_WEIGHT, summary.weight);
        contentValues.put(COL_START_TIME, summary.startTime);
        contentValues.put(COL_WORKOUT_DURATION, summary.duration);
        contentValues.put(COL_REST_DURATION, summary.restDuration);
        return contentValues;
    }

    @WorkerThread
    public void saveWorkout(ExerciseSummary summary) {
        ThreadUtils.assertBackgroundThread();
    }
}
