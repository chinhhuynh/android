package com.chinhhuynh.gymtracker.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import com.chinhhuynh.gymtracker.GymTrackerApplication;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.views.NumberPickerDialog;
import com.chinhhuynh.gymtracker.views.RestCountdown;
import com.chinhhuynh.gymtracker.views.StartButton;
import com.chinhhuynh.lifecycle.activity.OnBackPressed;
import org.jetbrains.annotations.NotNull;

/**
 * Fragment for starting a workout.
 */
public final class WorkoutFragment extends Fragment implements
        OnBackPressed,
        RestCountdown.CountdownListener {

    public static final String TAG = WorkoutFragment.class.getSimpleName();

    public interface WorkoutEventListener {
        void onExerciseCompleted(@NotNull ExerciseSummary summary);
    }

    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final long ANIMATE_START_BUTTON_DURATION = DateUtils.SECOND_IN_MILLIS / 4;
    private static final long ONE_TENTH_SECOND = DateUtils.SECOND_IN_MILLIS / 10;
    private static final long HALF_SECOND = DateUtils.SECOND_IN_MILLIS / 2;
    private static final int MIN_WEIGHT = 0;
    private static final int MAX_WEIGHT = 200;
    private static final int WEIGHT_INTERVAL = 5;
    private static final int MIN_REST_DURATION = 5;
    private static final int MAX_REST_DURATION = 120;
    private static final int REST_DURATION_INTERVAL = 5;

    private static final String CLOCK_RESET = "00:00";
    private static final String CLOCK_DISPLAY = "%02d:%02d";

    private final float mMinimizeShiftDistance;

    private AppCompatActivity mActivity;
    private Context mContext;
    private Handler mHandler;
    private Vibrator mVibrator;
    private TextView mClockView;
    private StartButton mStartButton;
    private RestCountdown mRestCountdownView;
    private NumberPickerDialog mWeightPicker;
    private NumberPickerDialog mRestDurationPicker;

    private Toolbar mToolbar;
    private TextView mSetView;
    private TextView mWeightView;
    private TextView mRestDurationView;

    private Exercise mExercise;
    private ExerciseSummary mSummary;
    private WorkoutEventListener mListener;
    private int mDurationSec;

    private long mCurrentSetStartTime;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now - mCurrentSetStartTime);
            mClockView.setText(String.format(CLOCK_DISPLAY, seconds / 60, seconds % 60));
            mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        }
    };

    public WorkoutFragment() {
        Resources resources = GymTrackerApplication.getAppContext().getResources();
        int buttonSize = resources.getDimensionPixelSize(R.dimen.button_size);
        mMinimizeShiftDistance = buttonSize / (2 * SQRT_2);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_fragment, container, false);

        mActivity = (AppCompatActivity) getActivity();
        mContext = mActivity;
        mHandler = new Handler(Looper.getMainLooper());
        mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);

        mClockView = (TextView) fragmentLayout.findViewById(R.id.clock);
        mStartButton = (StartButton) fragmentLayout.findViewById(R.id.start_button);
        mRestCountdownView = (RestCountdown) fragmentLayout.findViewById(R.id.rest_countdown);
        mToolbar = (Toolbar) fragmentLayout.findViewById(R.id.toolbar);
        mSetView = (TextView) fragmentLayout.findViewById(R.id.workout_set);
        mWeightView = (TextView) fragmentLayout.findViewById(R.id.workout_weight);
        mRestDurationView = (TextView) fragmentLayout.findViewById(R.id.workout_rest_duration);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mStartButton.getState()) {
                    case StartButton.STATE_START:
                        startWorkout();
                        break;
                    case StartButton.STATE_STOP:
                        increaseSet();
                        stopWorkout();
                        break;
                }
            }
        });
        mRestCountdownView.setListener(this);
        mRestCountdownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRestCountdownView.isActive()) {
                    stopRestCountdown();
                } else {
                    increaseSet();
                    rest();
                }
            }
        });

        mWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeightPicker.selectedValue(getCurrentWeight()).show();
            }
        });
        mRestDurationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRestDurationPicker.selectedValue(getCurrentRestDuration()).show();
            }
        });

        mWeightPicker = new NumberPickerDialog(mContext, R.layout.number_picker)
                .minValue(MIN_WEIGHT)
                .maxValue(MAX_WEIGHT)
                .interval(WEIGHT_INTERVAL)
                .listener(new NumberPickerDialog.EventsListener() {

                    @Override
                    public void onNumberSelect(int value) {
                        mWeightView.setText(Integer.toString(value));
                    }

                    @Override
                    public void onCancel() {
                        // no-op.
                    }
                });

        mRestDurationPicker = new NumberPickerDialog(mContext, R.layout.number_picker)
                .minValue(MIN_REST_DURATION)
                .maxValue(MAX_REST_DURATION)
                .interval(REST_DURATION_INTERVAL)
                .listener(new NumberPickerDialog.EventsListener() {

                    @Override
                    public void onNumberSelect(int value) {
                        mRestDurationView.setText(Integer.toString(value));
                        mRestCountdownView.setRestDuration(value);
                    }

                    @Override
                    public void onCancel() {
                        // no-op.
                    }
                });

        initViews();

        return fragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupToolbar();
    }

    @Override
    public boolean onBackPressed() {
        onWorkoutCompleted();
        return true;
    }

    @Override
    public void onCountdownFinished() {
        mVibrator.vibrate(HALF_SECOND);
        mClockView.setText(CLOCK_RESET);
        mDurationSec += mRestCountdownView.getElapsedSec();

        // start the workout again.
        mRestCountdownView.stop();
        mRestCountdownView.setRestDuration(getCurrentRestDuration());
        mRestCountdownView.setVisibility(View.VISIBLE);

        mCurrentSetStartTime = System.currentTimeMillis();
        mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
    }

    public WorkoutFragment setExercise(Exercise exercise, ExerciseSummary summary) {
        mExercise = exercise;
        mSummary = summary == null ? new ExerciseSummary(exercise) : new ExerciseSummary(summary);
        return this;
    }

    public WorkoutFragment setListener(WorkoutEventListener listener) {
        mListener = listener;
        return this;
    }

    private void initViews() {
        mSetView.setText(String.valueOf(mSummary.set));
        mWeightView.setText(String.valueOf(mSummary.weight));
        if (mSummary.restDurationSec != 0) {
            mRestDurationView.setText(String.valueOf(mSummary.restDurationSec));
        }
    }

    private void startWorkout() {
        mRestCountdownView.stop();
        mRestCountdownView.setRestDuration(getCurrentRestDuration());
        mRestCountdownView.setVisibility(View.VISIBLE);

        long now = System.currentTimeMillis();
        if (mSummary.startTime == 0) {
            mSummary.startTime = now;
        }

        mCurrentSetStartTime = now;
        mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        changeToStopButton();
    }

    private void setStopWorkoutState() {
        mClockView.setText(CLOCK_RESET);
        mHandler.removeCallbacks(mClockTimer);
        if (mCurrentSetStartTime != 0) {
            mDurationSec += TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - mCurrentSetStartTime);
            mCurrentSetStartTime = 0;
        }
    }

    private void stopWorkout() {
        setStopWorkoutState();
        changeToStartButton();
    }

    private void increaseSet() {
        int set = getCurrentSet() + 1;
        mSetView.setText(Integer.toString(set));
    }

    private void setStopRestCountdownState() {
        mRestCountdownView.stop();
        mDurationSec += mRestCountdownView.getElapsedSec();
    }

    private void stopRestCountdown() {
        setStopRestCountdownState();
        changeToStartButton();
    }

    private void rest() {
        mHandler.removeCallbacks(mClockTimer);
        mRestCountdownView.countdown();
    }

    private void changeToStopButton() {
        mStartButton.setState(StartButton.STATE_STOP);
        PropertyValuesHolder shiftRight = PropertyValuesHolder.ofFloat("translationX", 0, mMinimizeShiftDistance);
        PropertyValuesHolder shiftDown = PropertyValuesHolder.ofFloat("translationY", 0, mMinimizeShiftDistance);

        PropertyValuesHolder scaleWidth = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f);
        PropertyValuesHolder scaleHeight = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f);

        ObjectAnimator
                .ofPropertyValuesHolder(mStartButton, shiftRight, shiftDown, scaleWidth, scaleHeight)
                .setDuration(ANIMATE_START_BUTTON_DURATION)
                .start();
    }

    private void changeToStartButton() {
        mStartButton.setState(StartButton.STATE_START);
        PropertyValuesHolder shiftLeft = PropertyValuesHolder.ofFloat("translationX", mMinimizeShiftDistance, 0);
        PropertyValuesHolder shiftUp = PropertyValuesHolder.ofFloat("translationY", mMinimizeShiftDistance, 0);

        PropertyValuesHolder scaleWidth = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder scaleHeight = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);

        ObjectAnimator
                .ofPropertyValuesHolder(mStartButton, shiftLeft, shiftUp, scaleWidth, scaleHeight)
                .setDuration(ANIMATE_START_BUTTON_DURATION)
                .start();
    }

    private int getCurrentSet() {
        return Integer.parseInt(mSetView.getText().toString());
    }

    private int getCurrentWeight() {
        return Integer.parseInt(mWeightView.getText().toString());
    }

    private int getCurrentRestDuration() {
        return Integer.parseInt(mRestDurationView.getText().toString());
    }

    private void onWorkoutCompleted() {
        setStopWorkoutState();
        setStopRestCountdownState();

        mSummary.setWeight(Integer.parseInt(mWeightView.getText().toString()))
                .setSet(Integer.parseInt(mSetView.getText().toString()))
                .setDuration(mDurationSec + mSummary.durationSec)
                .setRep(Integer.parseInt(mRestDurationView.getText().toString()));
        if (mListener != null) {
            mListener.onExerciseCompleted(mSummary);
        }
    }

    private void setupToolbar() {
        if (mExercise == null) {
            return;
        }
        mToolbar.setTitle(mExercise.mExerciseName);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_back_white_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWorkoutCompleted();
            }
        });
    }
}
