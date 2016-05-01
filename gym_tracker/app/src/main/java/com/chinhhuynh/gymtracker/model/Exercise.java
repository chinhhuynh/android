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
    public static final String MUSCLE_GROUP_LEGS_AND_GLUTE = "Legs & Glute";
    public static final String MUSCLE_GROUP_ABS = "Abs";
    public static final String MUSCLE_GROUP_BACK = "Back";
    public static final String MUSCLE_GROUP_SHOULDER = "Shoulder";

    // Chest
    public static final String EXERCISE_PUSH_UP = "Push up";
    public static final String EXERCISE_PUSH_UP_ICON = "push_up.png";
    public static final String EXERCISE_CHEST_PRESS = "Chest Press";
    public static final String EXERCISE_CHEST_PRESS_ICON = "chest_press.png";
    public static final String EXERCISE_INCLINE_CHEST_PRESS = "Incline Chest Press";
    public static final String EXERCISE_INCLINE_CHEST_PRESS_ICON = "incline_chest_press.png";
    public static final String EXERCISE_DECLINE_CHEST_PRESS = "Decline Chest Press";
    public static final String EXERCISE_DECLINE_CHEST_PRESS_ICON = "decline_chest_press.png";
    public static final String EXERCISE_DIP = "Dip";
    public static final String EXERCISE_DIP_ICON = "dip.png";
    public static final String EXERCISE_CABLE_CROSS_OVER = "Cable Cross Over";
    public static final String EXERCISE_CABLE_CROSS_OVER_ICON = "cable_cross_over.png";
    public static final String EXERCISE_CABLE_CHEST_PRESS = "Cable Chest Press";
    public static final String EXERCISE_CABLE_CHEST_PRESS_ICON = "cable_chest_press.png";
    public static final String EXERCISE_DUMBBELL_FLY = "Dumbbell Fly";
    public static final String EXERCISE_DUMBBELL_FLY_ICON = "dumbbell_fly.png";
    public static final String EXERCISE_DUMBBELL_PULL_OVER = "Dumbbell Pull Over";
    public static final String EXERCISE_DUMBBELL_PULL_OVER_ICON = "dumbbell_pull_over.png";

    // Arms
        // Biceps
    public static final String EXERCISE_CURL = "Curl";
    public static final String EXERCISE_CURL_ICON = "curl.png";
    public static final String EXERCISE_PREACHER_CURL = "Preacher Curl";
    public static final String EXERCISE_PREACHER_CURL_ICON = "preacher_curl.png";
    public static final String EXERCISE_HAMMER_CURL = "Hammer Curl";
    public static final String EXERCISE_HAMMER_CURL_ICON = "hammer_curl.png";
    public static final String EXERCISE_CONCENTRATION_CURL = "Concentration Curl";
    public static final String EXERCISE_CONCENTRATION_CURL_ICON = "concentration_curl.png";
    public static final String EXERCISE_REVERSE_CURL = "Reverse Curl";
    public static final String EXERCISE_REVERSE_CURL_ICON = "reverse_curl.png";

        // Triceps
    public static final String EXERCISE_SKULL_CRUSHER = "Skull Crusher";
    public static final String EXERCISE_SKULL_CRUSHER_ICON = "skull_crusher.png";
    public static final String EXERCISE_DUMBBELL_KICK_BACK = "Dumbbell Kickback";
    public static final String EXERCISE_DUMBBELL_KICK_BACK_ICON = "dumbbell_kickback.png";
    public static final String EXERCISE_TRICEPS_PUSH_DOWN = "Triceps Push Down";
    public static final String EXERCISE_TRICEPS_PUSH_DOWN_ICON = "triceps_push_down.png";
    public static final String EXERCISE_TRICEPS_DIP = "Triceps Dip";
    public static final String EXERCISE_TRICEPS_DIP_ICON = "triceps_dip.png";
    public static final String EXERCISE_DUMBBELL_TRICEPS_EXTENSION = "Dumbbell Triceps Extension";
    public static final String EXERCISE_DUMBBELL_TRICEPS_EXTENSION_ICON = "dumbbell_triceps_extension.png";
    public static final String EXERCISE_CABLE_TRICEPS_EXTENSION = "Cable Triceps Extension";
    public static final String EXERCISE_CABLE_TRICEPS_EXTENSION_ICON = "cable_triceps_extension.png";

    // Legs & Glute
    public static final String EXERCISE_BARBELL_BACK_SQUAT = "Barbell Back Squat";
    public static final String EXERCISE_BARBELL_BACK_SQUAT_ICON = "barbell_back_squat.png";
    public static final String EXERCISE_BARBELL_FRONT_SQUAT = "Barbell Front Squat";
    public static final String EXERCISE_BARBELL_FRONT_SQUAT_ICON = "barbell_front_squat.png";
    public static final String EXERCISE_JUMP_SQUAT = "Jump Squat";
    public static final String EXERCISE_JUMP_SQUAT_ICON = "jump_squat.png";
    public static final String EXERCISE_DEADLIFT = "Deadlift";
    public static final String EXERCISE_DEADLIFT_ICON = "deadlift.png";
    public static final String EXERCISE_LEG_PRESS = "Leg Press";
    public static final String EXERCISE_LEG_PRESS_ICON = "leg_press.png";
    public static final String EXERCISE_LEG_EXTENSION = "Leg Extension";
    public static final String EXERCISE_LEG_EXTENSION_ICON = "leg_extension.png";
    public static final String EXERCISE_LEG_CURL = "Leg Curl";
    public static final String EXERCISE_LEG_CURL_ICON = "leg_curl.png";
    public static final String EXERCISE_CALF_RAISE = "Calf Raise";
    public static final String EXERCISE_CALF_RAISE_ICON = "calf_raise.png";
    public static final String EXERCISE_LUNGE = "Lunge";
    public static final String EXERCISE_LUNGE_ICON = "lunge.png";
    public static final String EXERCISE_LATERAL_LUNGE = "Lateral Lunge";
    public static final String EXERCISE_LATERAL_LUNGE_ICON = "lateral_lunge.png";
    public static final String EXERCISE_REVERSE_LUNGE = "Reverse Lunge";
    public static final String EXERCISE_REVERSE_LUNGE_ICON = "reverse_lunge.png";
    public static final String EXERCISE_BOX_JUMP = "Box Jump";
    public static final String EXERCISE_BOX_JUMP_ICON = "box_jump.png";
    public static final String EXERCISE_STEP_UP = "Step Up";
    public static final String EXERCISE_STEP_UP_ICON = "step_up.png";
    public static final String EXERCISE_DONKEY_KICK_BACK = "Donkey Kick Back";
    public static final String EXERCISE_DONKEY_KICK_BACK_ICON = "donkey_kick_back.png";
    public static final String EXERCISE_CABLE_KICK_BACK = "Cable Kick Back";
    public static final String EXERCISE_CABLE_KICK_BACK_ICON = "cable_kick_back.png";
    public static final String EXERCISE_GLUTE_BRIDGE = "Glute Bridge";
    public static final String EXERCISE_GLUTE_BRIDGE_ICON = "glute_bridge.png";
    public static final String EXERCISE_LATERAL_BAND_WALK = "Lateral Band Walk";
    public static final String EXERCISE_LATERAL_BAND_WALK_ICON = "lateral_band_walk.png";

    // Abs
    public static final String EXERCISE_BOTTOMS_UP = "Bottoms Up";
    public static final String EXERCISE_BOTTOMS_UP_ICON = "bottoms_up.png";
    public static final String EXERCISE_CABLE_WOOD_CHOP = "Cable Wood Chop";
    public static final String EXERCISE_CABLE_WOOD_CHOP_ICON = "cable_wood_chop.png";
    public static final String EXERCISE_HANGING_KNEE_RAISE = "Hanging Knee Raise";
    public static final String EXERCISE_HANGING_KNEE_RAISE_ICON = "hanging_knee_raise.png";
    public static final String EXERCISE_HANGING_LEG_RAISE = "Hanging Leg Raise";
    public static final String EXERCISE_HANGING_LEG_RAISE_ICON = "hanging_leg_raise.png";
    public static final String EXERCISE_PLANK = "Plank";
    public static final String EXERCISE_PLANK_ICON = "plank.png";
    public static final String EXERCISE_CABLE_CRUNCH = "Cable Crunch";
    public static final String EXERCISE_CABLE_CRUNCH_ICON = "cable_crunch.png";
    public static final String EXERCISE_REVERSE_CRUNCH = "Reverse Crunch";
    public static final String EXERCISE_REVERSE_CRUNCH_ICON = "reverse_crunch.png";
    public static final String EXERCISE_WEIGHTED_SUITCASE_CRUNCH = "Weighted Suitcase Crunch";
    public static final String EXERCISE_WEIGHTED_SUITCASE_CRUNCH_ICON = "weighted_suitcase_crunch.png";
    public static final String EXERCISE_ELBOW_TO_KNEE_TWIST = "Elbow To Knee Twist";
    public static final String EXERCISE_ELBOW_TO_KNEE_TWIST_ICON = "elbow_to_knee_twist.png";
    public static final String EXERCISE_RUSSIAN_TWIST = "Russian Twist";
    public static final String EXERCISE_RUSSIAN_TWIST_ICON = "russian_twist.png";
    public static final String EXERCISE_SPIDER_CRAWL = "Spider Crawl";
    public static final String EXERCISE_SPIDER_CRAWL_ICON = "spider_crawl.png";
    public static final String EXERCISE_MOUNTAIN_CLIMBER = "Mountain Climber";
    public static final String EXERCISE_MOUNTAIN_CLIMBER_ICON = "mountain_climber.png";
    public static final String EXERCISE_SIT_UP = "Sit Up";
    public static final String EXERCISE_SIT_UP_ICON = "sit_up.png";

    // Back
    public static final String EXERCISE_PULL_UP = "Pull Up";
    public static final String EXERCISE_PULL_UP_ICON = "pull_up.png";
    public static final String EXERCISE_CABLE_ROW = "Cable Row";
    public static final String EXERCISE_CABLE_ROW_ICON = "cable_row.png";
    public static final String EXERCISE_BARBELL_ROW = "Barbell Row";
    public static final String EXERCISE_BARBELL_ROW_ICON = "barbell_row.png";
    public static final String EXERCISE_SINGLE_ARM_DUMBBELL_ROW = "Single Arm Dumbbell Row";
    public static final String EXERCISE_SINGLE_ARM_DUMBBELL_ROW_ICON = "single_arm_dumbbell_row.png";
    public static final String EXERCISE_LAT_PULL_DOWN = "Lat Pull Down";
    public static final String EXERCISE_LAT_PULL_DOWN_ICON = "lat_pull_down.png";
    public static final String EXERCISE_BEHIND_NECK_LAT_PULL_DOWN = "Behind Neck Lat Pull Down";
    public static final String EXERCISE_BEHIND_NECK_LAT_PULL_DOWN_ICON = "behind_neck_lat_pull_down.png";
    // EXERCISE_DUMBBELL_PULL_OVER is also a chest exercise.
    public static final String EXERCISE_CABLE_PULL_OVER = "Cable Pull Over";
    public static final String EXERCISE_CABLE_PULL_OVER_ICON = "cable_pull_over.png";

    // Shoulder
    public static final String EXERCISE_SHOULDER_PRESS = "Shoulder Press";
    public static final String EXERCISE_SHOULDER_PRESS_ICON = "shoulder_press.png";
    public static final String EXERCISE_ARNOLD_PRESS = "Arnold Press";
    public static final String EXERCISE_ARNOLD_PRESS_ICON = "arnold_press.png";
    public static final String EXERCISE_MILITARY_PRESS = "Military Press";
    public static final String EXERCISE_MILITARY_PRESS_ICON = "military_press.png";
    public static final String EXERCISE_DUMBBELL_LATERAL_RAISE = "Dumbbell Lateral Raise";
    public static final String EXERCISE_DUMBBELL_LATERAL_RAISE_ICON = "dumbbell_lateral_raise.png";
    public static final String EXERCISE_LOW_CABLE_CROSS_OVER = "Low Cable Cross Over";
    public static final String EXERCISE_LOW_CABLE_CROSS_OVER_ICON = "low_cable_cross_over.png";
    public static final String EXERCISE_PLATE_FRONT_RAISE = "Plate Front Raise";
    public static final String EXERCISE_PLATE_FRONT_RAISE_ICON = "plate_front_raise.png";
    public static final String EXERCISE_FRONT_DUMBBELL_RAISE = "Front Dumbbell Raise";
    public static final String EXERCISE_FRONT_DUMBBELL_RAISE_ICON = "front_dumbbell_raise.png";
    public static final String EXERCISE_UP_RIGHT_ROW = "Upright Row";
    public static final String EXERCISE_UP_RIGHT_ROW_ICON = "up_right_row.png";
    public static final String EXERCISE_REVERSE_CABLE_FLY = "Reverse Cable Fly";
    public static final String EXERCISE_REVERSE_CABLE_FLY_ICON = "reverse_cable_fly.png";
    public static final String EXERCISE_REVERSE_DUMBBELL_FLY = "Reverse Dumbbell Fly";
    public static final String EXERCISE_REVERSE_DUMBBELL_FLY_ICON = "reverse_dumbbell_fly.png";
}
