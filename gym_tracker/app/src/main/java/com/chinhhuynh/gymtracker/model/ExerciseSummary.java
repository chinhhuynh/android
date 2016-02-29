package com.chinhhuynh.gymtracker.model;

public final class ExerciseSummary {

    public final Exercise mExercise;

    public int duration;
    public int weight;
    public int set;
    public int rep;

    public ExerciseSummary(Exercise exercise) {
        mExercise = exercise;
    }

    public ExerciseSummary setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public ExerciseSummary setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public ExerciseSummary setSet(int set) {
        this.set = set;
        return this;
    }

    public ExerciseSummary setRep(int rep) {
        this.rep = rep;
        return this;
    }
}
