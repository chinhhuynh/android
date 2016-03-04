package com.chinhhuynh.gymtracker.model;

public final class ExerciseSummary {

    public final Exercise exercise;

    public long startTime;
    public int duration;
    public int weight;
    public int set;
    public int restDuration;
    public int rep;

    public ExerciseSummary(Exercise exercise) {
        this.exercise = exercise;
    }

    public ExerciseSummary(ExerciseSummary other) {
        exercise = other.exercise;

        startTime = other.startTime;
        duration = other.duration;
        weight = other.weight;
        set = other.set;
        restDuration = other.restDuration;
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

    public ExerciseSummary setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public ExerciseSummary setRestDuration(int restDuration) {
        this.restDuration = restDuration;
        return this;
    }

    public ExerciseSummary setRep(int rep) {
        this.rep = rep;
        return this;
    }
}
