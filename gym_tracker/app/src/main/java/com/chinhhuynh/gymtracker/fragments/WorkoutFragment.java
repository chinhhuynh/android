package com.chinhhuynh.gymtracker.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.views.RestCountdown;

/**
 * Fragment for starting a workout.
 */
public final class WorkoutFragment extends Fragment {

    public static final String TAG = "WorkoutFragment";

    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final long MINIMIZE_START_BUTTON_DURATION = DateUtils.SECOND_IN_MILLIS / 4;
    private static final int REST_DURATION_SECONDS = 45;
    private static final long ONE_TENTH_SECOND = DateUtils.SECOND_IN_MILLIS / 10;

    private AppCompatActivity mActivity;
    private Handler mHandler;

    private View mFragmentLayout;
    private TextView mClock;
    private View mStartButton;
    private RestCountdown mRestCountdownView;

    private long mStartTime;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now - mStartTime);
            mClock.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_fragment, container, false);

        mFragmentLayout = fragmentLayout;
        mActivity = (AppCompatActivity) getActivity();
        mHandler = new Handler(Looper.getMainLooper());

        mClock = (TextView) fragmentLayout.findViewById(R.id.clock);
        mStartButton = fragmentLayout.findViewById(R.id.start_button);
        mRestCountdownView = (RestCountdown) fragmentLayout.findViewById(R.id.rest_countdown);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRestCountdownView.setRestDuration(REST_DURATION_SECONDS);
                mRestCountdownView.setVisibility(View.VISIBLE);

                mStartTime = System.currentTimeMillis();
                mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
                minimizeStartButton();
            }
        });

        return fragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupActionBar();
    }

    private void minimizeStartButton() {
        float radius = mStartButton.getWidth() / 2;
        float shiftDistance = radius / SQRT_2;

        PropertyValuesHolder shiftRight = PropertyValuesHolder.ofFloat("translationX", 0, shiftDistance);
        PropertyValuesHolder shiftDown = PropertyValuesHolder.ofFloat("translationY", 0, shiftDistance);

        PropertyValuesHolder scaleWidth = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f);
        PropertyValuesHolder scaleHeight = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f);

        ObjectAnimator
                .ofPropertyValuesHolder(mStartButton, shiftRight, shiftDown, scaleWidth, scaleHeight)
                .setDuration(MINIMIZE_START_BUTTON_DURATION)
                .start();
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.workout_title);
    }
}
