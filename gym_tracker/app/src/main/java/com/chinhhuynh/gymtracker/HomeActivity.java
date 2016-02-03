package com.chinhhuynh.gymtracker;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;
import com.chinhhuynh.gymtracker.loaders.ExerciseLoader;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

public class HomeActivity extends AppCompatActivity implements Loader.OnLoadCompleteListener<Cursor> {

    private RecyclerView mDailySummaries;
    private DailySummaryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(this)
                        .title("title")
                        .sheet(R.menu.home_add_menu)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case R.id.start_workout_session:
                                        Toast.makeText(HomeActivity.this, "Help me!", Toast.LENGTH_SHORT);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

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
        long now = System.currentTimeMillis();
        DailySummary dailySummary1 = generateDailySummary(new Date(now), 3);
        DailySummary dailySummary2 = generateDailySummary(new Date(now - DateUtils.DAY_IN_MILLIS), 4);

        mDailySummaries = (RecyclerView) findViewById(R.id.daily_summaries);
        mAdapter = new DailySummaryAdapter(this);
        mAdapter.setSummaries(Arrays.asList(dailySummary1, dailySummary2));
        mDailySummaries.setAdapter(mAdapter);
        mDailySummaries.setLayoutManager(new LinearLayoutManager(this));
    }

    private DailySummary generateDailySummary(Date date, int exercisesCount) {
        List<ExerciseSummary> exercises = new ArrayList<>();
        Exercise sitUp = new Exercise("Sit up", "sit_up.png");
        for (int i = 0; i < exercisesCount; i++) {
            ExerciseSummary exerciseSummary = new ExerciseSummary(sitUp)
                    .setDuration(30)
                    .setWeight(150)
                    .setSet(4)
                    .setRep(10);
            exercises.add(exerciseSummary);
        }
        return new DailySummary(date, exercises);
    }

    private void extractAssets() {
        ExtractAssetsTask extractAssetsTask = new ExtractAssetsTask(this);
        extractAssetsTask.execute();
    }
}
