package com.chinhhuynh.gymtracker.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;

public final class ExerciseSummaryViewHolder extends RecyclerView.ViewHolder {

    private final Context mContext;

    private final ImageView mIconView;
    private final TextView mTitleView;
    private final TextView mSubTextView;

    private ExerciseSummary mSummary;
    private BitmapImageViewTarget mViewTarget;

    public ExerciseSummaryViewHolder(View itemView) {
        super(itemView);

        mContext = itemView.getContext();

        mIconView = (ImageView) itemView.findViewById(R.id.exercise_icon);
        mTitleView = (TextView) itemView.findViewById(R.id.exercise_title);
        mSubTextView = (TextView) itemView.findViewById(R.id.exercise_subtext);

        mViewTarget = new BitmapImageViewTarget(mIconView);
    }

    public void bind(ExerciseSummary summary) {

        mSummary = summary;

        mTitleView.setText(String.valueOf(mSummary.exercise.mExerciseName));
        mSubTextView.setText(getExerciseSubText(mSummary.exercise));
        Glide.clear(mViewTarget);
    }

    public void onViewAttachedToWindow() {
        File dir = mContext.getDir(ExtractAssetsTask.EXERCISE_FOLDER, Context.MODE_PRIVATE);
        File icon = new File(dir, mSummary.exercise.mIconFileName);
        Glide.with(mContext)
                .load(icon)
                .asBitmap()
                .into(mViewTarget);
    }

    private String getExerciseSubText(Exercise exercise) {
        if (mSummary != null) {
            int durationMin = mSummary.durationSec < 60 ? 1 : mSummary.durationSec / 60;
            return String.format("%d mins | %d sets | %s lbs", durationMin, mSummary.set, mSummary.weight);
        }
        return null;
    }
}
