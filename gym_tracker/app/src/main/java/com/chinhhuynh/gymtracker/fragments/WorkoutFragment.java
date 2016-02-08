package com.chinhhuynh.gymtracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinhhuynh.gymtracker.R;

/**
 * Fragment for starting a workout.
 */
public final class WorkoutFragment extends Fragment {

    public static final String TAG = "WorkoutFragment";

    private AppCompatActivity mActivity;
    private View mFragmentLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_fragment, container, false);

        mFragmentLayout = fragmentLayout;
        mActivity = (AppCompatActivity) getActivity();

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
