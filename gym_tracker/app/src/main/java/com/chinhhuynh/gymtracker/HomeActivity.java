package com.chinhhuynh.gymtracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.Date;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;
import com.chinhhuynh.gymtracker.loaders.ExerciseLoader;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

public class HomeActivity extends FragmentActivity
        implements Loader.OnLoadCompleteListener<Cursor> {

    private RecyclerView mDailySummaries;
    private DailySummaryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        extractAssets();
        initializeViews();

        ExerciseLoader loader = new ExerciseLoader(this, "Sit up");
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
                String exerciseName = cursor.getString(ExerciseTable.COL_IDX_NAME);
                String iconFileName = cursor.getString(ExerciseTable.COL_IDX_ICON_FILE_NAME);
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
        }
    }

    private void initializeViews() {
        Exercise sitUp = new Exercise("Sit up", "sit_up.png");
        ExerciseSummary exerciseSummary = new ExerciseSummary(sitUp)
                .setDuration(30)
                .setSet(4)
                .setRep(10);
        DailySummary dailySummary = new DailySummary(new Date(), Arrays.asList(exerciseSummary));

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
