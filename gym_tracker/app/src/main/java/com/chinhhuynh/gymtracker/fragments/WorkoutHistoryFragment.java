package com.chinhhuynh.gymtracker.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.chinhhuynh.gymtracker.DailySummaryAdapter;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;

/**
 * Fragment for creating new workout set.
 */
public final class WorkoutHistoryFragment extends Fragment {

    public static final String TAG = WorkoutHistoryFragment.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private View mFragmentLayout;
    private RecyclerView mSummariesView;
    private DailySummaryAdapter mAdapter;

    private List<DailySummary> mSummaries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_history_fragment, container, false);

        mFragmentLayout = fragmentLayout;
        mContext = fragmentLayout.getContext();
        mActivity = (AppCompatActivity) getActivity();

        generateSummaries();

        mSummariesView = (RecyclerView) fragmentLayout.findViewById(R.id.daily_summaries);
        mAdapter = new DailySummaryAdapter(mContext);
        mAdapter.setSummaries(mSummaries);
        mSummariesView.setAdapter(mAdapter);
        mSummariesView.setLayoutManager(new LinearLayoutManager(mContext));

        return fragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupActionBar();
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.history_title);
    }

    private void generateSummaries() {
        long now = System.currentTimeMillis();
        DailySummary dailySummary1 = generateDailySummary(new Date(now), 3);
        DailySummary dailySummary2 = generateDailySummary(new Date(now - DateUtils.DAY_IN_MILLIS), 4);

        mSummaries = Arrays.asList(dailySummary1, dailySummary2);
    }

    private DailySummary generateDailySummary(Date date, int exercisesCount) {
        List<ExerciseSummary> exercises = new ArrayList<>();
        Exercise sitUp = new Exercise(Exercise.EXERCISE_SIT_UP, Exercise.MUSCLE_GROUP_ABS, Exercise.EXERCISE_SIT_UP_ICON);
        for (int i = 0; i < exercisesCount; i++) {
            ExerciseSummary exerciseSummary = new ExerciseSummary(sitUp)
                    .setDuration(30)
                    .setWeight(150)
                    .setSet(4)
                    .setRep(10);
            exercises.add(exerciseSummary);
        }
        return new DailySummary(date, exercises);
    }

    private final class ActionSheetListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int menuId) {
            switch (menuId) {
                case R.id.create_workout_set:
                    CreateWorkoutFragment fragment = new CreateWorkoutFragment();
                    mActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(fragment, CreateWorkoutFragment.TAG)
                            .commit();
                    break;
                default:
                    break;
            }
        }
    }
}
