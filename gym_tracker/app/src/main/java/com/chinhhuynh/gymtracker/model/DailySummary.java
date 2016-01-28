package com.chinhhuynh.gymtracker.model;

import java.util.Date;
import java.util.List;

public final class DailySummary {

    public final Date mDate;
    public final List<ExerciseSummary> mExercises;

    public DailySummary(Date date, List<ExerciseSummary> exercises) {
        mDate = date;
        mExercises = exercises;
    }
}
