package com.chinhhuynh.gymtracker.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.chinhhuynh.gymtracker.database.table.ExerciseTable;

public final class ExerciseLoader extends CursorLoader {

    private final String mMuscleGroup;

    public ExerciseLoader(Context context, String muscleGroup) {
        super(context);
        mMuscleGroup = muscleGroup;
    }

    @Override
    public Cursor loadInBackground() {
        return ExerciseTable.queryByMuscleGroup(mMuscleGroup);
    }
}
