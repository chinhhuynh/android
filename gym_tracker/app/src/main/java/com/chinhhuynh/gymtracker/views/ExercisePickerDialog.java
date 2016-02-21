package com.chinhhuynh.gymtracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExercisePickerDialog {

    private static String[] MUSCLE_GROUPS = {
            Exercise.MUSCLE_GROUP_ABS,
            Exercise.MUSCLE_GROUP_CHEST,
            Exercise.MUSCLE_GROUP_LOWER_BACK,
            Exercise.MUSCLE_GROUP_TRAPS,
            Exercise.MUSCLE_GROUP_ABDUCTORS,
            Exercise.MUSCLE_GROUP_FOREARMS,
            Exercise.MUSCLE_GROUP_MIDDLE_BACK,
            Exercise.MUSCLE_GROUP_TRICEPS,
            Exercise.MUSCLE_GROUP_ADDUCTORS,
            Exercise.MUSCLE_GROUP_GLUTES,
            Exercise.MUSCLE_GROUP_NECK,
            Exercise.MUSCLE_GROUP_BICEPS,
            Exercise.MUSCLE_GROUP_HAMSTRINGS,
            Exercise.MUSCLE_GROUP_QUADRICEPS,
            Exercise.MUSCLE_GROUP_CALVES,
            Exercise.MUSCLE_GROUP_SHOULDERS,
    };

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResId;

    private View mLayoutView;
    private ListView mMuscleGroup;

    public ExercisePickerDialog(Context context, int layoutResId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mLayoutResId = layoutResId;
    }

    public void show() {
        mLayoutView = mLayoutInflater.inflate(mLayoutResId, null);

        mMuscleGroup = (ListView) mLayoutView.findViewById(R.id.muscle_group);
        ArrayAdapter<String> muscleGroupAdapter =
                new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, MUSCLE_GROUPS);
        mMuscleGroup.setAdapter(muscleGroupAdapter);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.select_exercise_title);
        alertDialogBuilder.setView(mLayoutView);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
