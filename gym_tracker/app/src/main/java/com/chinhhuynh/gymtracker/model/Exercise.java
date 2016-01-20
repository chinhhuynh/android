package com.chinhhuynh.gymtracker.model;

public final class Exercise {

    private final String mExerciseName;
    private final String mIconPath;

    public Exercise(String exerciseName,
                    String iconFileName) {
        mExerciseName = exerciseName;
        mIconPath = iconFileName;
    }
}
