package com.chinhhuynh.gymtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.WorkoutSessionAdapter;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.views.ExercisePickerDialog;

/**
 * Fragment for creating a workout session.
 */
public final class WorkoutSessionFragment extends Fragment implements ExercisePickerDialog.EventsListener {

    public static final String TAG = WorkoutSessionFragment.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private MenuItem mEditMenu, mDoneMenu;
    private ListView mExercises;
    private WorkoutSessionAdapter mExercisesAdapter;

    private ExercisePickerDialog mExercisePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_session, container, false);
        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.workout_session_action_bar, menu);
        mEditMenu = menu.findItem(R.id.action_edit);
        mDoneMenu = menu.findItem(R.id.action_done);
        updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                onEdit();
                return true;
            case R.id.action_done:
                onEditFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onExerciseSelect(Exercise exercise) {
        mExercisesAdapter.addExercise(exercise);
        updateMenu();
    }

    private void onEditFinish() {
        mExercisesAdapter.setEditMode(false /*isEditMode*/);
        updateMenu();
    }

    private void onEdit() {
        mExercisesAdapter.setEditMode(true /*isEditMode*/);
        mEditMenu.setVisible(false);
        mDoneMenu.setVisible(true);
    }

    private void updateMenu() {
        boolean hasExercise = mExercisesAdapter.getCount() > 0;
        mEditMenu.setVisible(hasExercise);
        mDoneMenu.setVisible(false);
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.workout_session_title);
    }
}
