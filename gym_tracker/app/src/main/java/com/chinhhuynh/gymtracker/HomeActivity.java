package com.chinhhuynh.gymtracker;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;
import com.chinhhuynh.gymtracker.fragments.WorkoutFragment;
import com.chinhhuynh.gymtracker.fragments.WorkoutHistoryFragment;
import com.chinhhuynh.gymtracker.loaders.ExerciseLoader;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

public class HomeActivity extends AppCompatActivity implements
        Loader.OnLoadCompleteListener<Cursor> {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeToolbar();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, new WorkoutFragment())
                    .commit();
        }

        extractAssets();

        ExerciseLoader loader = new ExerciseLoader(this, "Sit up");
        loader.registerListener(0 /*id*/, this);
        loader.startLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private Fragment getFragment(int menuItemId) {
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItemId) {
            case R.id.workout:
                fragmentClass = WorkoutFragment.class;
                break;
            case R.id.history:
                fragmentClass = WorkoutHistoryFragment.class;
                break;
            default:
                fragmentClass = WorkoutFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    private void extractAssets() {
        ExtractAssetsTask extractAssetsTask = new ExtractAssetsTask(this);
        extractAssetsTask.execute();
    }
}
