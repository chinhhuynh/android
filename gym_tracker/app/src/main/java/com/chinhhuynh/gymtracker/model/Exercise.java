package com.chinhhuynh.gymtracker.model;

public final class Exercise {

    public final String mExerciseName;
    public final String mMuscleGroup;
    public final String mIconFileName;

    public Exercise(String exerciseName,
                    String muscleGroup,
                    String iconFileName) {
        mExerciseName = exerciseName;
        mMuscleGroup = muscleGroup;
        mIconFileName = iconFileName;
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

    public static final String EXERCISE_SIT_UP = "Sit Up";
    public static final String EXERCISE_SIT_UP_ICON = "sit_up.png";
}
