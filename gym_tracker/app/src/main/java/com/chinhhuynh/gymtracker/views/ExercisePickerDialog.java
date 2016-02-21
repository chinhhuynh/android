package com.chinhhuynh.gymtracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.chinhhuynh.gymtracker.R;

public final class ExercisePickerDialog {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResId;

    private View mLayoutView;

    public ExercisePickerDialog(Context context, int layoutResId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mLayoutResId = layoutResId;
    }

    public void show() {
        mLayoutView = mLayoutInflater.inflate(mLayoutResId, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.select_exercise_title);
        alertDialogBuilder.setView(mLayoutView);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
