package com.chinhhuynh.gymtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.chinhhuynh.gymtracker.DailySummaryAdapter;
import com.chinhhuynh.gymtracker.ExercisesManager;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.DailySummary;

/**
 * Fragment for creating new workout set.
 */
public final class WorkoutHistoryFragment extends Fragment implements
        WorkoutSessionFragment.ExerciseChangedListener,
        ExercisesManager.DailySummariesListener {

    public static final String TAG = WorkoutHistoryFragment.class.getSimpleName();

    private static final long QUERY_RANGE_MS = 30 * DateUtils.DAY_IN_MILLIS;

    private Context mContext;
    private RecyclerView mSummariesView;
    private DailySummaryAdapter mAdapter;

    private RepeatExercisesListener mRepeatExercisesListener;
    private ExercisesManager mExercisesManager;
    private long mQueryTimestamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_history_fragment, container, false);

        mContext = fragmentLayout.getContext();
        mExercisesManager = ExercisesManager.getInstance();
        mExercisesManager.addDailySummariesListener(this);

        mQueryTimestamp = System.currentTimeMillis() - QUERY_RANGE_MS;
        mExercisesManager.loadDailySummaries(mQueryTimestamp, mQueryTimestamp + QUERY_RANGE_MS);

        mSummariesView = (RecyclerView) fragmentLayout.findViewById(R.id.daily_summaries);
        mAdapter = new DailySummaryAdapter(mContext);
        mAdapter.setListener(mRepeatExercisesListener);
        mSummariesView.setAdapter(mAdapter);
        mSummariesView.setLayoutManager(new LinearLayoutManager(mContext));

        return fragmentLayout;
    }

    @Override
    public void onDailySummariesLoaded(List<DailySummary> dailySummaries) {
        mAdapter.addSummaries(dailySummaries);
    }

    @Override
    public void onExerciseChanged() {
        mQueryTimestamp = System.currentTimeMillis() - QUERY_RANGE_MS;
        mExercisesManager.loadDailySummaries(mQueryTimestamp, mQueryTimestamp + QUERY_RANGE_MS);
    }

    public void setRepeatExercisesListener(RepeatExercisesListener listener) {
        mRepeatExercisesListener = listener;
    }
}
