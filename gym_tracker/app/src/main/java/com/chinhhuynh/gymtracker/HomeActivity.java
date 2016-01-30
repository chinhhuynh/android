package com.chinhhuynh.gymtracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;
import com.chinhhuynh.gymtracker.loaders.ExerciseLoader;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements Loader.OnLoadCompleteListener<Cursor> {

    private RecyclerView mDailySummaries;
    private DailySummaryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        extractAssets();
        initializeViews();

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

    private void initializeViews() {
        Exercise sitUp = new Exercise("Sit up", "sit_up.png");
        ExerciseSummary exerciseSummary1 = new ExerciseSummary(sitUp)
                .setDuration(30)
                .setWeight(0)
                .setSet(4)
                .setRep(10);
        ExerciseSummary exerciseSummary2 = new ExerciseSummary(sitUp)
                .setDuration(25)
                .setWeight(0)
                .setSet(4)
                .setRep(10);
        DailySummary dailySummary = new DailySummary(new Date(), Arrays.asList(exerciseSummary1, exerciseSummary2));

        mDailySummaries = (RecyclerView) findViewById(R.id.daily_summaries);
        mAdapter = new DailySummaryAdapter(this);
        mAdapter.setSummaries(Arrays.asList(dailySummary));
        mDailySummaries.setAdapter(mAdapter);
        mDailySummaries.setLayoutManager(new LinearLayoutManager(this));
    }

    private void extractAssets() {
        ExtractAssetsTask extractAssetsTask = new ExtractAssetsTask(this);
        extractAssetsTask.execute();
    }
}
