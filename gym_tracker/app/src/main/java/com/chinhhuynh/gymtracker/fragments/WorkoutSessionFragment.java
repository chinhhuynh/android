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
public final class WorkoutSessionFragment extends Fragment implements ExercisePickerDialog.EventsListener,
        WorkoutSessionAdapter.EventListener {

    public static final String TAG = WorkoutSessionFragment.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private MenuItem mEditMenu, mDoneMenu;
    private ListView mExercises;
    private WorkoutSessionAdapter mExercisesAdapter;

    private ExercisePickerDialog mExercisePicker;
    private boolean mIsEditing;

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
        mExercisesAdapter.setEventListener(this);
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
                onEditing();
                return true;
            case R.id.action_done:
                onFinishEditing();
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

    @Override
    public void onItemRemoved(Exercise exercise) {
        updateMenu();
        if (!hasExercise()) {
            onFinishEditing();
        }
    }

    private void onFinishEditing() {
        mIsEditing = false;
        mExercisesAdapter.setEditMode(false /*isEditMode*/);
        updateMenu();
    }

    private void onEditing() {
        mIsEditing = true;
        mExercisesAdapter.setEditMode(true /*isEditMode*/);
        updateMenu();
    }

    private void updateMenu() {
        boolean hasExercise = hasExercise();
        if (mIsEditing) {
            mEditMenu.setVisible(false);
            mDoneMenu.setVisible(hasExercise);
        } else {
            mEditMenu.setVisible(hasExercise);
            mDoneMenu.setVisible(false);
        }
    }

    private boolean hasExercise() {
        return mExercisesAdapter.getCount() > 0;
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.workout_session_title);
    }
}
