package com.chinhhuynh.gymtracker.fragments;

import com.chinhhuynh.gymtracker.model.Exercise;

public interface ExerciseList {
    boolean hasNext(Exercise exercise);
    void startNext(Exercise exercise);
}
