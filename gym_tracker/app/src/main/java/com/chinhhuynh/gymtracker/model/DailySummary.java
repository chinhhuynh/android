package com.chinhhuynh.gymtracker.model;

import java.util.Date;
import java.util.List;

public class DailySummary {

    private final Date mDate;
    private final List<ExerciseSummary> mExercises;

    public DailySummary(Date date, List<ExerciseSummary> exercises) {
        mDate = date;
        mExercises = exercises;
    }
}
