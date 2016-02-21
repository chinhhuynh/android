package com.chinhhuynh.gymtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.views.ExercisePickerDialog;

/**
 * Fragment for creating a workout session.
 */
public final class WorkoutSession extends Fragment {

    public static final String TAG = WorkoutSession.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private RecyclerView mExercises;

    private ExercisePickerDialog mExercisePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_session, container, false);

        mContext = fragmentLayout.getContext();
        mActivity = (AppCompatActivity) getActivity();

        mExercisePicker = new ExercisePickerDialog(mContext, R.layout.exercise_picker);

        mExercises = (RecyclerView) fragmentLayout.findViewById(R.id.exercises);
        mExercises.setLayoutManager(new LinearLayoutManager(mContext));

        FloatingActionButton fab = (FloatingActionButton) fragmentLayout.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExercisePicker.show();
            }
        });

        return fragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupActionBar();
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.session_title);
    }
}
