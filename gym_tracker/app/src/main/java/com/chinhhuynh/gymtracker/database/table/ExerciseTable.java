package com.chinhhuynh.gymtracker.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.WorkerThread;

import java.util.HashMap;
import java.util.Map;

import com.chinhhuynh.gymtracker.database.DatabaseHelper;
import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExerciseTable extends DbTable<Exercise> {

    public static final int COL_IDX_ID = 0;
    public static final int COL_IDX_NAME = 1;
    public static final int COL_IDX_MUSCLE_GROUP = 2;
    public static final int COL_IDX_ICON_FILE_NAME = 3;

    public static final String TABLE_NAME = "Exercise";

    public static final String COL_NAME = "name";
    public static final String COL_MUSCLE_GROUP = "muscle_group";
    public static final String COL_ICON_FILE_NAME = "icon_file_name";

    private static final String[][] COLUMNS = {
            { _ID, DataType.TEXT },
            { COL_NAME, DataType.TEXT },
            { COL_MUSCLE_GROUP, DataType.TEXT },
            { COL_ICON_FILE_NAME, DataType.TEXT },
    };

    private static final String[] PROJECTION = {
            _ID,
            COL_NAME,
            COL_MUSCLE_GROUP,
            COL_ICON_FILE_NAME,
    };

    private static Exercise[] ABS_EXERCISES = {
            new Exercise(Exercise.EXERCISE_RUSSIAN_TWIST, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_WEIGHTED_SUITCASE_CRUNCH, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_BOTTOMS_UP, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_SPIDER_CRAWL, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_SPELL_CASTER, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_SIT_UP, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_HANGING_LEG_RAISE, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_ROPE_CRUNCH, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
    };

    private static Exercise[] CHEST_EXERCISES = {
            new Exercise(Exercise.EXERCISE_PUSH_UP, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_DUMBBELL_BENCH_PRESS, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_BARBELL_BENCH_PRESS, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_INCLINE_BARBELL_PRESS, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_LOW_CABLE_CROSSOVER, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_SIT_UP_ICON),
            new Exercise(Exercise.EXERCISE_BUTTERFLY, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_SIT_UP_ICON),
    };

    private static Map<String, Exercise[]> EXERCISES;
    static {
        EXERCISES = new HashMap<>();
        EXERCISES.put(Exercise.MUSCLE_GROUP_ABS, ABS_EXERCISES);
        EXERCISES.put(Exercise.MUSCLE_GROUP_CHEST, CHEST_EXERCISES);
    }

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
        for (String muscleGroup : EXERCISES.keySet()) {
            Exercise[] exercises = EXERCISES.get(muscleGroup);
            for (Exercise exercise : exercises) {
                db.insert(TABLE_NAME, null, getContentValues(exercise));
            }
        }
    }

    @Override
    protected ContentValues getContentValues(Exercise exercise) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, exercise.mExerciseName);
        contentValues.put(COL_MUSCLE_GROUP, exercise.mMuscleGroup);
        contentValues.put(COL_ICON_FILE_NAME, exercise.mIconFileName);
        return contentValues;
    }

    @WorkerThread
    public static Cursor queryByMuscleGroup(String muscleGroup) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        String selection = String.format("%s = ?", ExerciseTable.COL_MUSCLE_GROUP);

        return db.query(TABLE_NAME, PROJECTION /*columns*/, selection, new String[] { muscleGroup } /*selectionArgs*/,
                null /*groupBy*/, null /*having*/, null /*orderBy*/);
    }
}
