package com.chinhhuynh.gymtracker.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
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
        RepeatExercisesListener,
        WorkoutFragment.WorkoutEventListener {

    public static final String TAG = WorkoutSessionFragment.class.getSimpleName();
    private static final int FAB_ANIMATION_DURATION_MS = 100;
    private static final int FOOTER_ANIMATION_DURATION_MS = 100;

    public interface ExerciseChangedListener {
        void onExerciseChanged();
    }

    private AppCompatActivity mActivity;
    private Context mContext;
    private Map<Exercise, ExerciseSummary> mSummaries;

    private FloatingActionButton mFab;
    private ExercisePickerDialog mExercisePicker;
    private Button mEditButton;
    private ListView mExercises;
    private View mFooter;
    private WorkoutSessionAdapter mExercisesAdapter;
    private ExerciseChangedListener mListener;

    private boolean mIsEditing;
    private boolean mIsPaused;
    private boolean mIsFooterVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_session, container, false);
        setHasOptionsMenu(true);

        mContext = getContext();
        mActivity = (AppCompatActivity) getActivity();

        mEditButton = (Button) fragmentLayout.findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEditing) {
                    onFinishEditing();
                } else {
                    onEditing();
                }
            }
        });

        mFooter = fragmentLayout.findViewById(R.id.footer);

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
        updateEditButton();
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
        if (!hasExercise()) {
            onFinishEditing();
        }
    }

    @Override
    public void onRepeatExercises(List<Exercise> exercises) {

    }

    public void setExerciseChangedListener(ExerciseChangedListener listener) {
        mListener = listener;
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
                    .add(R.id.home_layout, workout, WorkoutFragment.TAG)
                    .addToBackStack(WorkoutFragment.TAG)
                    .commit();
        }
    }

    private void onFinishEditing() {
        mIsEditing = false;
        mExercisesAdapter.setEditMode(false /*isEditMode*/);
        updateEditButton();
        showFab();
    }

    private void onEditing() {
        mIsEditing = true;
        mExercisesAdapter.setEditMode(true /*isEditMode*/);
        updateEditButton();
        hideFab();
    }

    private void showFab() {
        mFab.setVisibility(View.VISIBLE);
        mFab
                .animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(FAB_ANIMATION_DURATION_MS)
                .setListener(null)
                .start();
    }

    private void hideFab() {
        mFab
                .animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(FAB_ANIMATION_DURATION_MS)
                .setListener(new AnimatorEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFab.setVisibility(View.GONE);
                    }
                }).start();
    }

    private void updateEditButton() {
        if (hasExercise() && !mIsFooterVisible) {
            showFooter();
            return;
        }

        if (!hasExercise() && mIsFooterVisible) {
            hideFooter();
            return;
        }

        String buttonText = mIsEditing
                ? getString(R.string.workout_session_done_action)
                : getString(R.string.workout_session_edit_action);
        mEditButton.setText(buttonText);
    }

    private void showFooter() {
        int footerHeight = mFooter.getHeight();
        mIsFooterVisible = true;

        mExercises.setPadding(0, 0, 0, footerHeight);
        mEditButton.setText(getString(R.string.workout_session_edit_action));
        mFooter.setVisibility(View.VISIBLE);
        mFooter.setTranslationY(footerHeight);

        ObjectAnimator fabAnimator = ObjectAnimator
                .ofFloat(mFab, "translationY", -footerHeight)
                .setDuration(FOOTER_ANIMATION_DURATION_MS);
        ObjectAnimator footerAnimator = ObjectAnimator
                .ofFloat(mFooter, "translationY", 0)
                .setDuration(FOOTER_ANIMATION_DURATION_MS);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(footerAnimator, fabAnimator);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setStartDelay(100);
        animatorSet.start();
    }

    private void hideFooter() {
        mIsFooterVisible = false;

        mExercises.setPadding(0, 0, 0, 0);
        mFooter.setVisibility(View.VISIBLE);
        mFooter.setTranslationY(0);

        ObjectAnimator fabAnimator = ObjectAnimator
                .ofFloat(mFab, "translationY", 0)
                .setDuration(FOOTER_ANIMATION_DURATION_MS);
        ObjectAnimator footerAnimator = ObjectAnimator
                .ofFloat(mFooter, "translationY", mFooter.getHeight())
                .setDuration(FOOTER_ANIMATION_DURATION_MS);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(footerAnimator, fabAnimator);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();
    }

    private void updateExercise(final ExerciseSummary summary) {
        if (summary.set > 0) {
            mExercisesAdapter.setExerciseCompleted(summary);
            mSummaries.put(summary.exercise, summary);
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    WorkoutTable.getInstance().saveWorkout(summary);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyExerciseChanged();
                        }
                    });
                }
            });
        }
    }

    private void notifyExerciseChanged() {
        ThreadUtils.assertMainThread();
        if (mListener != null) {
            mListener.onExerciseChanged();
        }
    }

    private boolean hasExercise() {
        return mExercisesAdapter.getCount() > 0;
    }
}
