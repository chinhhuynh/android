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

    private static Exercise[] CHEST_EXERCISES = {
            new Exercise(Exercise.EXERCISE_PUSH_UP, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_PUSH_UP_ICON),
            new Exercise(Exercise.EXERCISE_CHEST_PRESS, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_CHEST_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_INCLINE_CHEST_PRESS, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_INCLINE_CHEST_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_DECLINE_CHEST_PRESS, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_DECLINE_CHEST_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_DIP, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_DIP_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_CROSS_OVER, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_CABLE_CROSS_OVER_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_CHEST_PRESS, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_CABLE_CHEST_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_DUMBBELL_FLY, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_DUMBBELL_FLY_ICON),
            new Exercise(Exercise.EXERCISE_DUMBBELL_PULL_OVER, Exercise.MUSCLE_GROUP_CHEST, Exercise.EXERCISE_DUMBBELL_PULL_OVER_ICON ),
    };

    private static Exercise[] ARMS_EXERCISES = {
            new Exercise(Exercise.EXERCISE_CURL, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_CURL_ICON),
            new Exercise(Exercise.EXERCISE_PREACHER_CURL, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_PREACHER_CURL_ICON),
            new Exercise(Exercise.EXERCISE_HAMMER_CURL, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_HAMMER_CURL_ICON),
            new Exercise(Exercise.EXERCISE_CONCENTRATION_CURL, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_CONCENTRATION_CURL_ICON),
            new Exercise(Exercise.EXERCISE_REVERSE_CURL, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_REVERSE_CURL_ICON),

            new Exercise(Exercise.EXERCISE_SKULL_CRUSHER, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_SKULL_CRUSHER_ICON),
            new Exercise(Exercise.EXERCISE_DUMBBELL_KICK_BACK, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_DUMBBELL_KICK_BACK_ICON),
            new Exercise(Exercise.EXERCISE_TRICEPS_PUSH_DOWN, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_TRICEPS_PUSH_DOWN_ICON),
            new Exercise(Exercise.EXERCISE_TRICEPS_DIP, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_TRICEPS_DIP_ICON),
            new Exercise(Exercise.EXERCISE_DUMBBELL_TRICEPS_EXTENSION, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_DUMBBELL_TRICEPS_EXTENSION_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_TRICEPS_EXTENSION, Exercise.MUSCLE_GROUP_ARMS, Exercise.EXERCISE_CABLE_TRICEPS_EXTENSION_ICON),
    };

    private static Exercise[] LEGS_AND_GLUTE_EXERCISES = {
            new Exercise(Exercise.EXERCISE_BARBELL_BACK_SQUAT, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_BARBELL_BACK_SQUAT_ICON),
            new Exercise(Exercise.EXERCISE_BARBELL_FRONT_SQUAT, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_BARBELL_FRONT_SQUAT_ICON),
            new Exercise(Exercise.EXERCISE_JUMP_SQUAT, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_JUMP_SQUAT_ICON),
            new Exercise(Exercise.EXERCISE_DEADLIFT, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_DEADLIFT_ICON),
            new Exercise(Exercise.EXERCISE_LEG_PRESS, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_LEG_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_LEG_EXTENSION, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_LEG_EXTENSION_ICON),
            new Exercise(Exercise.EXERCISE_LEG_CURL, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_LEG_CURL_ICON),
            new Exercise(Exercise.EXERCISE_CALF_RAISE, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_CALF_RAISE_ICON),
            new Exercise(Exercise.EXERCISE_LUNGE, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_LUNGE_ICON),
            new Exercise(Exercise.EXERCISE_LATERAL_LUNGE, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_LATERAL_LUNGE_ICON),
            new Exercise(Exercise.EXERCISE_REVERSE_LUNGE, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_REVERSE_LUNGE_ICON),
            new Exercise(Exercise.EXERCISE_BOX_JUMP, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_BOX_JUMP_ICON),
            new Exercise(Exercise.EXERCISE_STEP_UP, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_STEP_UP_ICON),
            new Exercise(Exercise.EXERCISE_DONKEY_KICK_BACK, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_DONKEY_KICK_BACK_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_KICK_BACK, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_CABLE_KICK_BACK_ICON),
            new Exercise(Exercise.EXERCISE_GLUTE_BRIDGE, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_GLUTE_BRIDGE_ICON),
            new Exercise(Exercise.EXERCISE_LATERAL_BAND_WALK, Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, Exercise.EXERCISE_LATERAL_BAND_WALK_ICON),
    };

    private static Exercise[] ABS_EXERCISES = {
            new Exercise(Exercise.EXERCISE_BOTTOMS_UP, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_BOTTOMS_UP_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_WOOD_CHOP, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_CABLE_WOOD_CHOP_ICON),
            new Exercise(Exercise.EXERCISE_HANGING_KNEE_RAISE, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_HANGING_KNEE_RAISE_ICON),
            new Exercise(Exercise.EXERCISE_HANGING_LEG_RAISE, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_HANGING_LEG_RAISE_ICON),
            new Exercise(Exercise.EXERCISE_PLANK, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_PLANK_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_CRUNCH, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_CABLE_CRUNCH_ICON),
            new Exercise(Exercise.EXERCISE_REVERSE_CRUNCH, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_REVERSE_CRUNCH_ICON),
            new Exercise(Exercise.EXERCISE_WEIGHTED_SUITCASE_CRUNCH, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_WEIGHTED_SUITCASE_CRUNCH_ICON),
            new Exercise(Exercise.EXERCISE_ELBOW_TO_KNEE_TWIST, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_ELBOW_TO_KNEE_TWIST_ICON),
            new Exercise(Exercise.EXERCISE_RUSSIAN_TWIST, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_RUSSIAN_TWIST_ICON),
            new Exercise(Exercise.EXERCISE_SPIDER_CRAWL, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SPIDER_CRAWL_ICON),
            new Exercise(Exercise.EXERCISE_MOUNTAIN_CLIMBER, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_MOUNTAIN_CLIMBER_ICON),
            new Exercise(Exercise.EXERCISE_SIT_UP, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON),
    };

    private static Exercise[] BACK_EXERCISES = {
            new Exercise(Exercise.EXERCISE_PULL_UP, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_PULL_UP_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_ROW, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_CABLE_ROW_ICON),
            new Exercise(Exercise.EXERCISE_BARBELL_ROW, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_BARBELL_ROW_ICON),
            new Exercise(Exercise.EXERCISE_SINGLE_ARM_DUMBBELL_ROW, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_SINGLE_ARM_DUMBBELL_ROW_ICON),
            new Exercise(Exercise.EXERCISE_LAT_PULL_DOWN, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_LAT_PULL_DOWN_ICON),
            new Exercise(Exercise.EXERCISE_BEHIND_NECK_LAT_PULL_DOWN, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_BEHIND_NECK_LAT_PULL_DOWN_ICON),
            new Exercise(Exercise.EXERCISE_DUMBBELL_PULL_OVER, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_DUMBBELL_PULL_OVER_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_PULL_OVER, Exercise.MUSCLE_GROUP_BACK, Exercise.EXERCISE_CABLE_PULL_OVER_ICON),
    };

    private static Exercise[] SHOULDER_EXERCISES = {
            new Exercise(Exercise.EXERCISE_SHOULDER_PRESS, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_SHOULDER_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_ARNOLD_PRESS, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_ARNOLD_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_MILITARY_PRESS, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_MILITARY_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_DUMBBELL_LATERAL_RAISE, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_DUMBBELL_LATERAL_RAISE_ICON),
            new Exercise(Exercise.EXERCISE_BENT_OVER_LATERAL_RAISE, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_BENT_OVER_LATERAL_RAISE_ICON),
            new Exercise(Exercise.EXERCISE_LOW_CABLE_CROSS_OVER, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_LOW_CABLE_CROSS_OVER_ICON),
            new Exercise(Exercise.EXERCISE_CABLE_CHEST_PRESS, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_CABLE_CHEST_PRESS_ICON),
            new Exercise(Exercise.EXERCISE_PLATE_FRONT_RAISE, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_PLATE_FRONT_RAISE_ICON),
            new Exercise(Exercise.EXERCISE_FRONT_DUMBBELL_RAISE, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_FRONT_DUMBBELL_RAISE_ICON),
            new Exercise(Exercise.EXERCISE_UP_RIGHT_ROW, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_UP_RIGHT_ROW_ICON),
            new Exercise(Exercise.EXERCISE_REVERSE_CABLE_FLY, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_REVERSE_CABLE_FLY_ICON),
            new Exercise(Exercise.EXERCISE_REVERSE_DUMBBELL_FLY, Exercise.MUSCLE_GROUP_SHOULDER, Exercise.EXERCISE_REVERSE_DUMBBELL_FLY_ICON),
    };

    private static Map<String, Exercise[]> EXERCISES;
    static {
        EXERCISES = new HashMap<>();
        EXERCISES.put(Exercise.MUSCLE_GROUP_CHEST, CHEST_EXERCISES);
        EXERCISES.put(Exercise.MUSCLE_GROUP_ARMS, ARMS_EXERCISES);
        EXERCISES.put(Exercise.MUSCLE_GROUP_LEGS_AND_GLUTE, LEGS_AND_GLUTE_EXERCISES);
        EXERCISES.put(Exercise.MUSCLE_GROUP_ABS, ABS_EXERCISES);
        EXERCISES.put(Exercise.MUSCLE_GROUP_BACK, BACK_EXERCISES);
        EXERCISES.put(Exercise.MUSCLE_GROUP_SHOULDER, SHOULDER_EXERCISES);
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

    @Override
    protected String getUniqueString() {
        return null;
    }

    @WorkerThread
    public static Cursor queryByMuscleGroup(String muscleGroup) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        String selection = String.format("%s = ?", ExerciseTable.COL_MUSCLE_GROUP);

        return db.query(TABLE_NAME, PROJECTION /*columns*/, selection, new String[] { muscleGroup } /*selectionArgs*/,
                null /*groupBy*/, null /*having*/, null /*orderBy*/);
    }
}
