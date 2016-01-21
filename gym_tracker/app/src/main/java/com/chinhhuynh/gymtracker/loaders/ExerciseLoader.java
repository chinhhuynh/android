package com.chinhhuynh.gymtracker.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;

public final class ExerciseLoader extends CursorLoader {

    private final String mNameQuery;

    public ExerciseLoader(Context context, String nameQuery) {
        super(context);
        mNameQuery = nameQuery;
    }

    @Override
    public Cursor loadInBackground() {
        return ExerciseTable.queryByName(mNameQuery);
    }
}
