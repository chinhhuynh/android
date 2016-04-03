package com.chinhhuynh.gymtracker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import com.chinhhuynh.gymtracker.database.table.WorkoutTable;
import com.chinhhuynh.gymtracker.fragments.WorkoutHistoryFragment;
import com.chinhhuynh.gymtracker.fragments.WorkoutSessionFragment;
import com.chinhhuynh.gymtracker.loaders.SummaryLoader;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;
import com.chinhhuynh.lifecycle.activity.OnBackPressed;

public class HomeActivity extends AppCompatActivity implements
        Loader.OnLoadCompleteListener<Cursor> {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment mWorkoutSessionFragment;
    private Fragment mWorkoutHistoryFragment;

    private Map<Integer, String> mMenuIdFragmentNamesMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMenuIdFragmentNamesMapping = new HashMap<>();
        mMenuIdFragmentNamesMapping.put(R.id.workout_session, WorkoutSessionFragment.TAG);
        mMenuIdFragmentNamesMapping.put(R.id.history, WorkoutHistoryFragment.TAG);

        initializeToolbar();

        mWorkoutSessionFragment = new WorkoutSessionFragment();
        mWorkoutHistoryFragment = new WorkoutHistoryFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, mWorkoutHistoryFragment, WorkoutHistoryFragment.class.getSimpleName())
                    .commit();
        }

        extractAssets();
        startService(new Intent(this, LockScreenService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.syncState();
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

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        String fragmentName = mMenuIdFragmentNamesMapping.get(menuItem.getItemId());
        switchToFragment(fragmentName);

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private void switchToFragment(String fragmentName) {
        Fragment fragment = getFragment(fragmentName);

        if (fragment == null) {
            return;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment, fragmentName)
                .addToBackStack(fragmentName)
                .commit();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Fragment getFragment(String fragmentName) {
        switch (fragmentName) {
            case "WorkoutSessionFragment":
                return mWorkoutSessionFragment;

            case "WorkoutHistoryFragment":
                return mWorkoutHistoryFragment;
        }

        return null;
    }

    private void extractAssets() {
        ExtractAssetsTask extractAssetsTask = new ExtractAssetsTask(this);
        extractAssetsTask.execute();
    }
}
