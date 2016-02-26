package com.chinhhuynh.gymtracker;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;
import com.chinhhuynh.gymtracker.fragments.WorkoutHistoryFragment;
import com.chinhhuynh.gymtracker.fragments.WorkoutSessionFragment;
import com.chinhhuynh.gymtracker.loaders.ExerciseLoader;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;
import com.chinhhuynh.lifecycle.activity.OnBackPressed;

public class HomeActivity extends AppCompatActivity implements
        Loader.OnLoadCompleteListener<Cursor> {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment mWorkoutSessionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeToolbar();

        mWorkoutSessionFragment = new WorkoutSessionFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, mWorkoutSessionFragment)
                    .commit();
        }

        extractAssets();

        ExerciseLoader loader = new ExerciseLoader(this, "Sit up");
        loader.registerListener(0 /*id*/, this);
        loader.startLoading();
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
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof OnBackPressed) {
            ((OnBackPressed) fragment).onBackPressed();
        }
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
                String exerciseName = cursor.getString(ExerciseTable.COL_IDX_NAME);
                String iconFileName = cursor.getString(ExerciseTable.COL_IDX_ICON_FILE_NAME);
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
        Fragment fragment = getFragment(menuItem.getItemId());

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Fragment getFragment(int menuItemId) {
        try {
            switch (menuItemId) {
                case R.id.workout_session:
                    return mWorkoutSessionFragment;

                case R.id.history:
                    return WorkoutHistoryFragment.class.newInstance();
            }
        } catch (InstantiationException|IllegalAccessException ignored) {}

        return null;
    }

    private void extractAssets() {
        ExtractAssetsTask extractAssetsTask = new ExtractAssetsTask(this);
        extractAssetsTask.execute();
    }
}
