package com.chinhhuynh.gymtracker;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;
import com.chinhhuynh.gymtracker.loaders.ExerciseLoader;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements Loader.OnLoadCompleteListener<Cursor> {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ExerciseLoader loader = new ExerciseLoader(this, "Sit up");
        loader.registerListener(0 /*id*/, this);
        loader.startLoading();

        ExtractAssetsTask extractAssetsTask = new ExtractAssetsTask(this);
        extractAssetsTask.execute();
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
}
