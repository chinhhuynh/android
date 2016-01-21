package com.chinhhuynh.gymtracker.model;

public final class Exercise {

    private final String mExerciseName;
    private final String mIconFileName;

    public Exercise(String exerciseName,
                    String iconFileName) {
        mExerciseName = exerciseName;
        mIconFileName = iconFileName;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public String getIconFileName() {
        return mIconFileName;
    }
}
