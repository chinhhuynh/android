package com.chinhhuynh.gymtracker.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;

public final class ExerciseSummaryViewHolder extends RecyclerView.ViewHolder {

    private final TextView mDuration;
    private final TextView mWeight;
    private final TextView mSets;
    private final TextView mReps;

    public ExerciseSummaryViewHolder(View itemView) {
        super(itemView);

        mDuration = (TextView) itemView.findViewById(R.id.duration);
        mWeight = (TextView) itemView.findViewById(R.id.weight);
        mSets = (TextView) itemView.findViewById(R.id.sets);
        mReps = (TextView) itemView.findViewById(R.id.reps);
    }

    public void bind(ExerciseSummary exercise) {
        mDuration.setText(exercise.mDuration);
        mWeight.setText(exercise.mWeight);
        mSets.setText(exercise.mSet);
        mReps.setText(exercise.mRep);
    }
}
