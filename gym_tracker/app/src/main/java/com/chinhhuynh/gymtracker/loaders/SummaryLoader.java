package com.chinhhuynh.gymtracker.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.chinhhuynh.gymtracker.database.table.WorkoutTable;

public final class SummaryLoader extends CursorLoader {

    private final long mStart;
    private final long mEnd;

    public SummaryLoader(Context context, long start, long end) {
        super(context);
        mStart = start;
        mEnd = end;
    }

    @Override
    public Cursor loadInBackground() {
        return WorkoutTable.queryByTimeRange(mStart, mEnd);
    }
}
