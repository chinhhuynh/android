package com.chinhhuynh.gymtracker.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

import java.io.File;

public final class ExerciseSummaryViewHolder extends RecyclerView.ViewHolder {

    private final Context mContext;

    private final ImageView mIconView;
    private final TextView mDuration;
    private final TextView mWeight;
    private final TextView mSets;
    private final TextView mReps;

    private Exercise mExercise;
    private BitmapImageViewTarget mViewTarget;

    public ExerciseSummaryViewHolder(View itemView) {
        super(itemView);

        mContext = itemView.getContext();

        mIconView = (ImageView) itemView.findViewById(R.id.exercise_icon);
        mDuration = (TextView) itemView.findViewById(R.id.duration);
        mWeight = (TextView) itemView.findViewById(R.id.workout_weight);
        mSets = (TextView) itemView.findViewById(R.id.sets);
        mReps = (TextView) itemView.findViewById(R.id.reps);

        mViewTarget = new BitmapImageViewTarget(mIconView);
    }

    public void bind(ExerciseSummary exerciseSummary) {
        if (mExercise != null &&
                !TextUtils.equals(mExercise.mExerciseName, exerciseSummary.mExercise.mExerciseName)) {
            Glide.clear(mViewTarget);
        }

        mExercise = exerciseSummary.mExercise;

        mDuration.setText(String.valueOf(exerciseSummary.duration));
        mWeight.setText(String.valueOf(exerciseSummary.weight));
        mSets.setText(String.valueOf(exerciseSummary.set));
        mReps.setText(String.valueOf(exerciseSummary.rep));
    }

    public void onViewAttachedToWindow() {
        File dir = mContext.getDir(ExtractAssetsTask.EXERCISE_FOLDER, Context.MODE_PRIVATE);
        File icon = new File(dir, mExercise.mIconFileName);
        Glide.with(mContext)
                .load(icon)
                .asBitmap()
                .into(mViewTarget);
    }
}
