package com.chinhhuynh.gymtracker.fragments;

import android.animation.Animator;
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

import java.util.HashMap;
import java.util.Map;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import com.chinhhuynh.gymtracker.AnimatorEndListener;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.WorkoutSessionAdapter;
import com.chinhhuynh.gymtracker.database.table.WorkoutTable;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.views.ExercisePickerDialog;
import com.chinhhuynh.lifecycle.activity.OnBackPressed;
import org.jetbrains.annotations.NotNull;
import utils.ThreadUtils;

/**
 * Fragment for creating a workout session.
 */
public final class WorkoutSessionFragment extends Fragment implements
        OnBackPressed,
        ExerciseList,
        ExercisePickerDialog.EventsListener,
        WorkoutSessionAdapter.EventListener,
        WorkoutFragment.WorkoutEventListener {

    public static final String TAG = WorkoutSessionFragment.class.getSimpleName();
    private static final int ANIMATION_DURATION_MS = 100;

    private AppCompatActivity mActivity;
    private Context mContext;
    private Map<Exercise, ExerciseSummary> mSummaries;

    private FloatingActionButton mFab;
    private ExercisePickerDialog mExercisePicker;
    private MenuItem mEditMenu, mDoneMenu;
    private ListView mExercises;
    private WorkoutSessionAdapter mExercisesAdapter;

    private boolean mIsEditing;
    private boolean mIsPaused;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_session, container, false);
        setHasOptionsMenu(true);

        mContext = getContext();
        mActivity = (AppCompatActivity) getActivity();

        mExercisePicker = new ExercisePickerDialog(mContext, R.layout.exercise_picker, getLoaderManager())
                .listener(this);

        if (mSummaries == null) {
            mSummaries = new HashMap<>();
        }

        if (mExercisesAdapter == null) {
            mExercisesAdapter = new WorkoutSessionAdapter(mContext);
            mExercisesAdapter.setEventListener(this);
        }

        mExercises = (ListView) fragmentLayout.findViewById(R.id.exercises);
        mExercises.setAdapter(mExercisesAdapter);

        mFab = (FloatingActionButton) fragmentLayout.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
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
    public void onResume() {
        super.onResume();
        mIsPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsPaused = true;
    }

    @Override
    public boolean onBackPressed() {
        if (mIsEditing) {
            onFinishEditing();
            return true;
        }
        return false;
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
    public boolean hasNext(Exercise exercise) {
        return mExercisesAdapter.getNext(exercise) != null;
    }

    @Override
    public void startNext(Exercise exercise) {
        Exercise next = mExercisesAdapter.getNext(exercise);
        if (next != null) {
            startExercise(next);
        }
    }

    @Override
    public void onExerciseSelect(Exercise exercise) {
        mExercisesAdapter.addExercise(exercise);
        updateMenu();
    }

    @Override
    public void onExerciseCompleted(@NotNull ExerciseSummary summary) {
        updateExercise(summary);
        showFab();

        if (!mIsPaused) {
            Fragment workout = mActivity.getSupportFragmentManager().findFragmentByTag(WorkoutFragment.TAG);
            mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .remove(workout)
                    .commit();
            mActivity.getSupportFragmentManager().popBackStack(workout.getTag(), POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onItemClicked(Exercise exercise) {
        startExercise(exercise);
    }

    @Override
    public void onItemRemoved(Exercise exercise) {
        mSummaries.remove(exercise);
        updateMenu();
        if (!hasExercise()) {
            onFinishEditing();
        }
    }

    private void startExercise(Exercise exercise) {
        hideFab();

        WorkoutFragment workout =
                (WorkoutFragment) mActivity.getSupportFragmentManager().findFragmentByTag(WorkoutFragment.TAG);
        if (workout == null) {
            workout = new WorkoutFragment(this);
        }
        workout.setExercise(exercise, mSummaries.get(exercise))
                .setListener(this);

        if (!mIsPaused) {
            mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .add(R.id.drawer_layout, workout, WorkoutFragment.TAG)
                    .addToBackStack(WorkoutFragment.TAG)
                    .commit();
        }
    }

    private void onFinishEditing() {
        mIsEditing = false;
        mExercisesAdapter.setEditMode(false /*isEditMode*/);
        updateMenu();
        showFab();
    }

    private void onEditing() {
        mIsEditing = true;
        mExercisesAdapter.setEditMode(true /*isEditMode*/);
        updateMenu();
        hideFab();
    }

    private void showFab() {
        mFab.setVisibility(View.VISIBLE);
        mFab
                .animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(ANIMATION_DURATION_MS)
                .setListener(null)
                .start();
    }

    private void hideFab() {
        mFab
                .animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(ANIMATION_DURATION_MS)
                .setListener(new AnimatorEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFab.setVisibility(View.GONE);
                    }
                }).start();
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

    private void updateExercise(final ExerciseSummary summary) {
        if (summary.set > 0) {
            mExercisesAdapter.setExerciseCompleted(summary);
            mSummaries.put(summary.exercise, summary);
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    WorkoutTable.getInstance().saveWorkout(summary);
                }
            });
        }
    }

    private boolean hasExercise() {
        return mExercisesAdapter.getCount() > 0;
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.workout_session_title);
    }
}
