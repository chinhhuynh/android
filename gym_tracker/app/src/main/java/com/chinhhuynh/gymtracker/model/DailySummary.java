package com.chinhhuynh.gymtracker.model;

import android.text.format.DateUtils;

import java.util.Date;
import java.util.List;

public final class DailySummary {

    public final Date mDate;
    public final List<ExerciseSummary> mExercises;

    public DailySummary(Date date, List<ExerciseSummary> exercises) {
        mDate = date;
        mExercises = exercises;
    }

    public String getDateText() {
        return DateUtils.getRelativeTimeSpanString(
                mDate.getTime(),
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_NUMERIC_DATE).toString();
    }
}
