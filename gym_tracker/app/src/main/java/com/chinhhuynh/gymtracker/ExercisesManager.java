package com.chinhhuynh.gymtracker;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.UiThread;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.chinhhuynh.gymtracker.database.table.WorkoutTable;
import com.chinhhuynh.gymtracker.loaders.SummaryLoader;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import utils.ThreadUtils;

public class ExercisesManager implements
        Loader.OnLoadCompleteListener<Cursor> {

    public interface DailySummariesListener {
        @UiThread
        void onDailySummariesLoaded(List<DailySummary> dailySummaries);
    }

    private static ExercisesManager sInstance;

    private final Context mContext;

    private boolean mIsLoadingSummary;
    private boolean mIsSummaryLoaded;
    private SummaryLoader mSummaryLoader;

    private List<DailySummary> mDailySummaries;
    private List<DailySummariesListener> mDailySummariesListeners;

    public static ExercisesManager getInstance() {
        if (sInstance == null) {
            sInstance = new ExercisesManager();
        }
        return sInstance;
    }

    private ExercisesManager() {
        mContext = GymTrackerApplication.getAppContext();
        mDailySummaries = Collections.EMPTY_LIST;
        mDailySummariesListeners = new ArrayList<>();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        ThreadUtils.assertMainThread();
        mIsLoadingSummary = false;
        mIsSummaryLoaded = true;
        mDailySummaries = getDailySummaries(data);
        notifyDailySummariesLoaded(new ArrayList<>(mDailySummaries));
    }

    public void addDailySummariesListener(DailySummariesListener listener) {
        if (mIsSummaryLoaded) {
            notifyDailySummariesLoaded(listener, new ArrayList<>(mDailySummaries));
        }
        mDailySummariesListeners.add(listener);
    }

    public void loadDailySummaries(long start, long end) {
        if (mIsLoadingSummary) {
            return;
        }
        mIsLoadingSummary = true;
        mSummaryLoader = new SummaryLoader(mContext, start, end);
        mSummaryLoader.registerListener(0 /*id*/, this);
        mSummaryLoader.startLoading();
    }

    private List<DailySummary> getDailySummaries(Cursor cursor) {
        List<DailySummary> dailySummaries = new ArrayList<>();

        if (!cursor.moveToFirst()) {
            return dailySummaries;
        }

        List<ExerciseSummary> summaries = new ArrayList<>();
        Date date = null;

        do {
            Exercise exercise = new Exercise(
                    cursor.getString(WorkoutTable.COL_IDX_EXERCISE_NAME),
                    cursor.getString(WorkoutTable.COL_IDX_EXERCISE_MUSCLE_GROUP),
                    cursor.getString(WorkoutTable.COL_IDX_EXERCISE_ICON_FILE_NAME));

            ExerciseSummary summary = new ExerciseSummary(exercise)
                    .setStartTime(cursor.getLong(WorkoutTable.COL_IDX_START_TIME))
                    .setSet(cursor.getInt(WorkoutTable.COL_IDX_SET_COUNT))
                    .setWeight(cursor.getInt(WorkoutTable.COL_IDX_WEIGHT))
                    .setDuration(cursor.getInt(WorkoutTable.COL_IDX_WORKOUT_DURATION))
                    .setRestDuration(cursor.getInt(WorkoutTable.COL_IDX_REST_DURATION));

            Date exerciseDate = new Date(summary.startTime);
            if (date == null) {
                date = exerciseDate;
            }

            if (utils.DateUtils.isSameDay(date, exerciseDate)) {
                // Same day summary.
                summaries.add(summary);
            } else {
                // Date changed. Add previous day's summary.
                dailySummaries.add(new DailySummary(date, summaries));

                // New day's summary.
                date = exerciseDate;
                summaries = new ArrayList<>();
                summaries.add(summary);
            }
        } while (cursor.moveToNext());

        // Add the last day's summary.
        dailySummaries.add(new DailySummary(date, summaries));

        return dailySummaries;
    }

    private void notifyDailySummariesLoaded(List<DailySummary> dailySummaries) {
        if (!mDailySummariesListeners.isEmpty()) {
            List<DailySummariesListener> listeners = new ArrayList<>(mDailySummariesListeners);
            for (DailySummariesListener listener : listeners) {
                notifyDailySummariesLoaded(listener, dailySummaries);
            }
        }
    }

    private void notifyDailySummariesLoaded(DailySummariesListener listener,
                                            List<DailySummary> dailySummaries) {
        if (listener != null) {
            listener.onDailySummariesLoaded(dailySummaries);
        }
    }
}
