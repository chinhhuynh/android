package com.chinhhuynh.gymtracker.model;

import android.graphics.drawable.Drawable;

public final class Exercise {

    public final String mExerciseName;
    public final String mMuscleGroup;
    public final String mIconFileName;

    public Drawable mIconDrawable;

    public Exercise(String exerciseName,
                    String muscleGroup,
                    String iconFileName) {
        mExerciseName = exerciseName;
        mMuscleGroup = muscleGroup;
        mIconFileName = iconFileName;
    }

    @Override
    public String toString() {
        return mExerciseName;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mExerciseName.hashCode();
        result = 31 * result + mMuscleGroup.hashCode();
        return result;
    }

    public static final String MUSCLE_GROUP_ABS = "Abdominals";
    public static final String MUSCLE_GROUP_CHEST = "Chest";
    public static final String MUSCLE_GROUP_LOWER_BACK = "Lower Back";
    public static final String MUSCLE_GROUP_TRAPS = "Traps";
    public static final String MUSCLE_GROUP_ABDUCTORS = "Abductors";
    public static final String MUSCLE_GROUP_FOREARMS = "Forearms";
    public static final String MUSCLE_GROUP_MIDDLE_BACK = "Middle Back";
    public static final String MUSCLE_GROUP_TRICEPS = "Triceps";
    public static final String MUSCLE_GROUP_ADDUCTORS = "Adductors";
    public static final String MUSCLE_GROUP_GLUTES = "Glutes";
    public static final String MUSCLE_GROUP_NECK = "Neck";
    public static final String MUSCLE_GROUP_BICEPS = "Biceps";
    public static final String MUSCLE_GROUP_HAMSTRINGS = "Hamstrings";
    public static final String MUSCLE_GROUP_QUADRICEPS = "Quadriceps";
    public static final String MUSCLE_GROUP_CALVES = "Calves";
    public static final String MUSCLE_GROUP_SHOULDERS = "Shoulders";

    // Abdominals
    public static final String EXERCISE_RUSSIAN_TWIST = "Russian Twist";
    public static final String EXERCISE_WEIGHTED_SUITCASE_CRUNCH = "Weighted Suitcase Crunch";
    public static final String EXERCISE_BOTTOMS_UP = "Bottoms Up";
    public static final String EXERCISE_SPIDER_CRAWL = "Spider Crawl";
    public static final String EXERCISE_SPELL_CASTER = "Spell Caster";
    public static final String EXERCISE_SIT_UP = "Sit Up";
    public static final String EXERCISE_SIT_UP_ICON = "sit_up.png";
    public static final String EXERCISE_HANGING_LEG_RAISE = "Hanging Leg Raise";
    public static final String EXERCISE_ROPE_CRUNCH = "Rope Crunch";

    // Chests
    public static final String EXERCISE_PUSH_UP = "Push up";
    public static final String EXERCISE_DUMBBELL_BENCH_PRESS = "Dumbbell Bench Press";
    public static final String EXERCISE_BARBELL_BENCH_PRESS = "Barbell Bench Press";
    public static final String EXERCISE_INCLINE_BARBELL_PRESS = "Incline Barbell Press";
    public static final String EXERCISE_LOW_CABLE_CROSSOVER = "Low Cable Crossover";
    public static final String EXERCISE_BUTTERFLY = "Butterfly";
}
