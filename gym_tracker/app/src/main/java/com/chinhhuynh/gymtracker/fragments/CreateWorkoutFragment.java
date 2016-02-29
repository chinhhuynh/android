package com.chinhhuynh.gymtracker.fragments;

import com.chinhhuynh.gymtracker.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment for creating new workout set.
 */
public final class CreateWorkoutFragment extends Fragment {

    public static final String TAG = CreateWorkoutFragment.class.getSimpleName();

    private View mFragmentLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.create_workout_fragment, container, false);

        mFragmentLayout = fragmentLayout;

        return fragmentLayout;
    }
}
