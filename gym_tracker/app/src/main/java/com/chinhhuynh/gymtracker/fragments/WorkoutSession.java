package com.chinhhuynh.gymtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.WorkoutSessionAdapter;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.views.ExercisePickerDialog;

/**
 * Fragment for creating a workout session.
 */
public final class WorkoutSession extends Fragment implements ExercisePickerDialog.EventsListener {

    public static final String TAG = WorkoutSession.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private ListView mExercises;
    private WorkoutSessionAdapter mExercisesAdapter;

    private ExercisePickerDialog mExercisePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_session, container, false);

        mContext = fragmentLayout.getContext();
        mActivity = (AppCompatActivity) getActivity();

        mExercisePicker = new ExercisePickerDialog(mContext, R.layout.exercise_picker)
                .listener(this);

        mExercises = (ListView) fragmentLayout.findViewById(R.id.exercises);
        mExercisesAdapter = new WorkoutSessionAdapter(mContext);
        mExercises.setAdapter(mExercisesAdapter);

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

    @Override
    public void onExerciseSelect(Exercise exercise) {
        mExercisesAdapter.addExercise(exercise);
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.session_title);
    }
}
