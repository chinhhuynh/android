package com.chinhhuynh.gymtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinhhuynh.gymtracker.database.table.WorkoutTable;
import com.chinhhuynh.gymtracker.fragments.RepeatExercisesListener;
import com.chinhhuynh.gymtracker.fragments.WorkoutHistoryFragment;
import com.chinhhuynh.gymtracker.fragments.WorkoutSessionFragment;
import com.chinhhuynh.gymtracker.loaders.SummaryLoader;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;
import com.chinhhuynh.lifecycle.activity.OnBackPressed;

public class HomeActivity extends AppCompatActivity implements
        Loader.OnLoadCompleteListener<Cursor> {

    private static final int WORKOUT_PAGE_POSITION = 0;
    private static final int HISTORY_PAGE_POSITION = 1;

    private WorkoutSessionFragment mWorkoutSessionFragment;
    private WorkoutHistoryFragment mWorkoutHistoryFragment;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private Map<Integer, String> mMenuIdFragmentNamesMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMenuIdFragmentNamesMapping = new HashMap<>();
        mMenuIdFragmentNamesMapping.put(R.id.workout_session, WorkoutSessionFragment.TAG);
        mMenuIdFragmentNamesMapping.put(R.id.history, WorkoutHistoryFragment.TAG);

        mWorkoutSessionFragment = new WorkoutSessionFragment();
        mWorkoutHistoryFragment = new WorkoutHistoryFragment();

        mWorkoutSessionFragment.setExerciseChangedListener(mWorkoutHistoryFragment);
        mWorkoutHistoryFragment.setRepeatExercisesListener(new RepeatExercisesListener() {
            @Override
            public void onRepeatExercises(List<Exercise> exercises) {
                mWorkoutSessionFragment.onRepeatExercises(exercises);
                mViewPager.setCurrentItem(WORKOUT_PAGE_POSITION, true /*smoothScroll*/);
            }
        });

        mPagerAdapter = new AppPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

        extractAssets();
        startService(new Intent(this, LockScreenService.class));
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int entryCount = fm.getBackStackEntryCount();
        if (entryCount == 0) {
            finish();
            return;
        }

        BackStackEntry top = fm.getBackStackEntryAt(entryCount - 1);
        Fragment fragment = fm.findFragmentByTag(top.getName());

        boolean consumed = false;
        if (fragment instanceof OnBackPressed) {
            consumed = ((OnBackPressed) fragment).onBackPressed();
        }

        if (consumed) {
            // if the fragment handled the back pressed, do nothing.
            return;
        }

        fm.popBackStackImmediate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        long now = System.currentTimeMillis();
        SummaryLoader loader = new SummaryLoader(this, now - DateUtils.DAY_IN_MILLIS, now);
        loader.registerListener(0 /*id*/, this);
        loader.startLoading();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            return;
        }
        try {
            if (!cursor.moveToFirst()) {
                return;
            }
            do {
                String exerciseName = cursor.getString(WorkoutTable.COL_IDX_EXERCISE_NAME);
                String muscleGroup = cursor.getString(WorkoutTable.COL_IDX_EXERCISE_MUSCLE_GROUP);
                String iconFileName = cursor.getString(WorkoutTable.COL_IDX_EXERCISE_ICON_FILE_NAME);
                String startTime = cursor.getString(WorkoutTable.COL_IDX_START_TIME);
                String set = cursor.getString(WorkoutTable.COL_IDX_SET_COUNT);
                String weight = cursor.getString(WorkoutTable.COL_IDX_WEIGHT);
                String duration = cursor.getString(WorkoutTable.COL_IDX_WORKOUT_DURATION);
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
        }
    }

    private void extractAssets() {
        ExtractAssetsTask extractAssetsTask = new ExtractAssetsTask(this);
        extractAssetsTask.execute();
    }

    private class AppPagerAdapter extends FragmentPagerAdapter {

        public AppPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case WORKOUT_PAGE_POSITION:
                    return mWorkoutSessionFragment;
                case HISTORY_PAGE_POSITION:
                    return mWorkoutHistoryFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case WORKOUT_PAGE_POSITION:
                    return getResources().getString(R.string.workout_session_title);
                case HISTORY_PAGE_POSITION:
                    return getResources().getString(R.string.history_title);
            }
            return null;
        }
    }
}
