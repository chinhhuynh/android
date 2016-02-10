package com.chinhhuynh.gymtracker.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import com.chinhhuynh.gymtracker.GymTrackerApplication;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.views.RestCountdown;

/**
 * Fragment for starting a workout.
 */
public final class WorkoutFragment extends Fragment implements RestCountdown.CountdownListener {

    public static final String TAG = "WorkoutFragment";

    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final long ANIMATE_START_BUTTON_DURATION = DateUtils.SECOND_IN_MILLIS / 4;
    private static final int REST_DURATION_SECONDS = 10;
    private static final long ONE_TENTH_SECOND = DateUtils.SECOND_IN_MILLIS / 10;

    private final float mMinimizeShiftDistance;

    private AppCompatActivity mActivity;
    private Handler mHandler;
    private TextView mClockView;
    private View mStartButton;
    private RestCountdown mRestCountdownView;

    private long mStartTime;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now - mStartTime);
            mClockView.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_fragment, container, false);

        mActivity = (AppCompatActivity) getActivity();
        mHandler = new Handler(Looper.getMainLooper());

        mClockView = (TextView) fragmentLayout.findViewById(R.id.clock);
        mStartButton = fragmentLayout.findViewById(R.id.start_button);
        mRestCountdownView = (RestCountdown) fragmentLayout.findViewById(R.id.rest_countdown);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkout();
            }
        });
        mRestCountdownView.setListener(this);
        mRestCountdownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rest();
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
    public void onCountdownFinished() {
        mClockView.setText("00:00");
        restoreStartButton();
    }

    private void startWorkout() {
        mRestCountdownView.stop();
        mRestCountdownView.setRestDuration(REST_DURATION_SECONDS);
        mRestCountdownView.setVisibility(View.VISIBLE);

        mStartTime = System.currentTimeMillis();
        mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        minimizeStartButton();
    }

    private void rest() {
        mHandler.removeCallbacks(mClockTimer);
        mRestCountdownView.countdown();
    }

    private void minimizeStartButton() {
        PropertyValuesHolder shiftRight = PropertyValuesHolder.ofFloat("translationX", 0, mMinimizeShiftDistance);
        PropertyValuesHolder shiftDown = PropertyValuesHolder.ofFloat("translationY", 0, mMinimizeShiftDistance);

        PropertyValuesHolder scaleWidth = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f);
        PropertyValuesHolder scaleHeight = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f);

        ObjectAnimator
                .ofPropertyValuesHolder(mStartButton, shiftRight, shiftDown, scaleWidth, scaleHeight)
                .setDuration(ANIMATE_START_BUTTON_DURATION)
                .start();
    }

    private void restoreStartButton() {
        PropertyValuesHolder shiftLeft = PropertyValuesHolder.ofFloat("translationX", mMinimizeShiftDistance, 0);
        PropertyValuesHolder shiftUp = PropertyValuesHolder.ofFloat("translationY", mMinimizeShiftDistance, 0);

        PropertyValuesHolder scaleWidth = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder scaleHeight = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);

        ObjectAnimator
                .ofPropertyValuesHolder(mStartButton, shiftLeft, shiftUp, scaleWidth, scaleHeight)
                .setDuration(ANIMATE_START_BUTTON_DURATION)
                .start();
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.workout_title);
    }
}
