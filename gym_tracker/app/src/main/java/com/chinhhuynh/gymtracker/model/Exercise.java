package com.chinhhuynh.gymtracker.model;

import android.content.Context;

import com.chinhhuynh.gymtracker.GymTrackerApplication;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

public final class Exercise {

    public final String mExerciseName;
    public final String mMuscleGroup;
    public final String mIconFileName;

    private final String mExerciseFolderPath;

    public Exercise(String exerciseName,
                    String muscleGroup,
                    String iconFileName) {
        mExerciseName = exerciseName;
        mMuscleGroup = muscleGroup;
        mIconFileName = iconFileName;

        Context context = GymTrackerApplication.getAppContext();
        mExerciseFolderPath = context.getDir(ExtractAssetsTask.EXERCISE_FOLDER, Context.MODE_PRIVATE).getAbsolutePath();
    }

    @Override
    public String toString() {
        return mExerciseName;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Exercise) {
            Exercise other = (Exercise) object;
            return mExerciseName.equals(other.mExerciseName)
                    && mMuscleGroup.equals(other.mMuscleGroup);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mExerciseName.hashCode();
        result = 31 * result + mMuscleGroup.hashCode();
        return result;
    }

    public String getIconAbsolutePath() {
        return mExerciseFolderPath.concat("/").concat(mIconFileName);
    }

    public static final String MUSCLE_GROUP_CHEST = "Chest";
    public static final String MUSCLE_GROUP_ARMS = "Arms";
    public static final String MUSCLE_GROUP_LEGS = "Legs";
    public static final String MUSCLE_GROUP_ABS = "Abs";
    public static final String MUSCLE_GROUP_BACK = "Back";
    public static final String MUSCLE_GROUP_SHOULDER = "Shoulder";
    public static final String MUSCLE_GROUP_GLUTE = "Glute";

    // Chest
    public static final String EXERCISE_PUSH_UP = "Push up";
    public static final String EXERCISE_CHEST_PRESS = "Chest Press";
    public static final String EXERCISE_INCLINE_CHEST_PRESS = "Incline Chest Press";
    public static final String EXERCISE_DECLINE_CHEST_PRESS = "Decline Chest Press";
    public static final String EXERCISE_DIP = "Dip";
    public static final String EXERCISE_CABLE_CROSSOVER = "Cable Crossover";
    public static final String EXERCISE_LOW_CABLE_CROSSOVER = "Low Cable Crossover";
    public static final String EXERCISE_DUMBBELL_FLY = "Dumbbell Fly";
    public static final String EXERCISE_DUMBBELL_PULL_OVER = "Dumbbell Pull Over";

    // Arms
        // Biceps
    public static final String EXERCISE_CURL = "Curl";
    public static final String EXERCISE_PREACHER_CURL = "Preacher Curl";
    public static final String EXERCISE_HAMMER_CURL = "Hammer Curl";
    public static final String EXERCISE_CONCENTRATION_CURL = "Concentration Curl";
    public static final String EXERCISE_REVERSE_CURL = "Reverse Curl";

        // Triceps
    public static final String EXERCISE_SKULL_CRUSHER = "Skull Crusher";
    public static final String EXERCISE_DUMBBELL_KICK_BACK = "Dumbbell Kickback";
    public static final String EXERCISE_TRICEPS_PUSH_DOWN = "Triceps Push Down";
    public static final String EXERCISE_TRICEPS_DIP = "Triceps Dip";
    public static final String EXERCISE_DUMBBELL_TRICEPS_EXTENSION = "Dumbbell Triceps Extension";
    public static final String EXERCISE_CABLE_TRICEPS_EXTENSION = "Cable Triceps Extension";

    // Legs
    public static final String EXERCISE_BARBELL_BACK_SQUAT = "Barbell Back Squat";
    public static final String EXERCISE_BARBELL_FRONT_SQUAT = "Barbell Front Squat";
    public static final String EXERCISE_DEADLIFT = "Deadlift";
    public static final String EXERCISE_LEG_PRESS = "Leg Press";
    public static final String EXERCISE_LEG_EXTENSION = "Leg Extension";
    public static final String EXERCISE_LEG_CURL = "Leg Curl";
    public static final String EXERCISE_CALF_RAISE = "Calf Raise";
    public static final String EXERCISE_LUNGE = "Lunge";
    public static final String EXERCISE_LATERAL_LUNGE = "Lateral Lunge";
    public static final String EXERCISE_REVERSE_LUNGE = "Reverse Lunge";
    public static final String EXERCISE_BOX_JUMP = "Box Jump";

    // Abs
    public static final String EXERCISE_BOTTOMS_UP = "Bottoms Up";
    public static final String EXERCISE_CABLE_WOOD_CHOP = "Cable Wood Chop";
    public static final String EXERCISE_HANGING_KNEE_RAISE = "Hanging Knee Raise";
    public static final String EXERCISE_HANGING_LEG_RAISE = "Hanging Leg Raise";
    public static final String EXERCISE_PLANK = "Plank";
    public static final String EXERCISE_CABLE_CRUNCH = "Cable Crunch";
    public static final String EXERCISE_REVERSE_CRUNCH = "Reverse Crunch";
    public static final String EXERCISE_WEIGHTED_SUITCASE_CRUNCH = "Weighted Suitcase Crunch";
    public static final String EXERCISE_ELBOW_TO_KNEE_TWIST = "Elbow To Knee Twist";
    public static final String EXERCISE_RUSSIAN_TWIST = "Russian Twist";
    public static final String EXERCISE_SPIDER_CRAWL = "Spider Crawl";
    public static final String EXERCISE_MOUNTAIN_CLIMBER = "Mountain Climber";
    public static final String EXERCISE_SIT_UP = "Sit Up";
    public static final String EXERCISE_SIT_UP_ICON = "sit_up.png";

    // Back
    public static final String EXERCISE_PULL_UP = "Pull Up";
    public static final String EXERCISE_CABLE_ROW = "Cable Row";
    public static final String EXERCISE_BARBELL_ROW = "Barbell Row";
    public static final String EXERCISE_SINGLE_ARM_DUMBBELL_ROW = "Single Arm Dumbbell Row";
    public static final String EXERCISE_LAT_PULL_DOWN = "Lat Pull Down";
    public static final String EXERCISE_BEHIND_NECK_LAT_PULL_DOWN = "Behind Neck Lat Pull Down";
    // public static final String EXERCISE_DUMBBELL_PULL_OVER = "Dumbbell Pull Over";
    public static final String EXERCISE_CABLE_PULL_OVER = "Cable Pull Over";
}
