package com.chinhhuynh.gymtracker.fragments;

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

/**
 * Fragment for starting a workout.
 */
public final class WorkoutFragment extends Fragment {

    public static final String TAG = "WorkoutFragment";

    private AppCompatActivity mActivity;
    private Handler mHandler;

    private View mFragmentLayout;
    private TextView mClock;
    private View mStart;

    private long mStartTime;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now - mStartTime);
            mClock.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            mHandler.postDelayed(mClockTimer, DateUtils.SECOND_IN_MILLIS);
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
        mStart = fragmentLayout.findViewById(R.id.start_button);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartTime = System.currentTimeMillis();
                mHandler.postDelayed(mClockTimer, DateUtils.SECOND_IN_MILLIS);
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
        mActivity.getSupportActionBar().setTitle(R.string.workout_title);
    }
}
