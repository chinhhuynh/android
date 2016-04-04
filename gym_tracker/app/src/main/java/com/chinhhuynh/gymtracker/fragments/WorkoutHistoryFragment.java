package com.chinhhuynh.gymtracker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chinhhuynh.gymtracker.DailySummaryAdapter;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.database.table.WorkoutTable;
import com.chinhhuynh.gymtracker.loaders.SummaryLoader;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;

/**
 * Fragment for creating new workout set.
 */
public final class WorkoutHistoryFragment extends Fragment implements
        Loader.OnLoadCompleteListener<Cursor> {

    public static final String TAG = WorkoutHistoryFragment.class.getSimpleName();

    private static final long QUERY_RANGE_MS = 7 * DateUtils.DAY_IN_MILLIS;

    private AppCompatActivity mActivity;
    private Context mContext;
    private RecyclerView mSummariesView;
    private DailySummaryAdapter mAdapter;

    private long mQueryTimestamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_history_fragment, container, false);

        mContext = fragmentLayout.getContext();
        mActivity = (AppCompatActivity) getActivity();

        mQueryTimestamp = System.currentTimeMillis() - QUERY_RANGE_MS;
        SummaryLoader loader = new SummaryLoader(mContext, mQueryTimestamp, mQueryTimestamp + QUERY_RANGE_MS);
        loader.registerListener(0 /*id*/, this);
        loader.startLoading();

        mSummariesView = (RecyclerView) fragmentLayout.findViewById(R.id.daily_summaries);
        mAdapter = new DailySummaryAdapter(mContext);
        mSummariesView.setAdapter(mAdapter);
        mSummariesView.setLayoutManager(new LinearLayoutManager(mContext));

        return fragmentLayout;
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        List<DailySummary> dailySummaries = getDailySummaries(data);
        mAdapter.addSummaries(dailySummaries);
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

            if (isSameDay(date, exerciseDate)) {
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

    private static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
