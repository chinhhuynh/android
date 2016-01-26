package com.chinhhuynh.gymtracker.model;

public final class ExerciseSummary {

    private final Exercise mExercise;

    private int mDuration;
    private int mWeight;
    private int mSet;
    private int mRep;

    public ExerciseSummary(Exercise exercise) {
        mExercise = exercise;
    }

    public ExerciseSummary setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public ExerciseSummary setWeight(int weight) {
        mWeight = weight;
        return this;
    }

    public ExerciseSummary setSet(int set) {
        mSet = set;
        return this;
    }

    public ExerciseSummary setRep(int rep) {
        mRep = rep;
        return this;
    }
}
