package com.chinhhuynh.gymtracker.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chinhhuynh.gymtracker.ExerciseSummaryAdapter;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;

public final class DailySummaryViewHolder extends RecyclerView.ViewHolder {

    private final RecyclerView.RecycledViewPool mRecycledViewPool;
    private final ExerciseSummaryAdapter mExercisesAdapter;
    private final TextView mDateHeader;
    private final LinearLayout mExercisesView;

    private List<ExerciseSummary> mExercises;

    public DailySummaryViewHolder(RecyclerView.RecycledViewPool recycledViewPool, View itemView) {
        super(itemView);

        mRecycledViewPool = recycledViewPool;
        mExercisesAdapter = new ExerciseSummaryAdapter(itemView.getContext());

        mDateHeader = (TextView) itemView.findViewById(R.id.date_header);
        mExercisesView = (LinearLayout) itemView.findViewById(R.id.exercises);
    }

    public void bind(DailySummary summary) {
        mDateHeader.setText(summary.getDateText());

        mExercises = new ArrayList<>(summary.mExercises);
        bindExercises();
    }

    public void onViewAttachedToWindow() {
        int viewCount = mExercisesView.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View exerciseView = mExercisesView.getChildAt(i);
            ExerciseSummaryViewHolder exerciseViewHolder = (ExerciseSummaryViewHolder) exerciseView.getTag();
            exerciseViewHolder.onViewAttachedToWindow();
        }
    }

    private void bindExercises() {
        int viewCount = mExercisesView.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View exerciseView = mExercisesView.getChildAt(i);
            ExerciseSummaryViewHolder viewHolder = (ExerciseSummaryViewHolder) exerciseView.getTag();
            if (i < mExercises.size()) {
                exerciseView.setVisibility(View.VISIBLE);
                viewHolder.bind(mExercises.get(i));
            } else {
                exerciseView.setVisibility(View.GONE);
                mExercisesView.removeViewAt(i);
                mRecycledViewPool.putRecycledView(viewHolder);
            }
        }
        for (int i = viewCount; i < mExercises.size(); i++) {
            ExerciseSummaryViewHolder viewHolder = getExerciseSummaryViewHolder();
            View exerciseView = viewHolder.itemView;
            exerciseView.setVisibility(View.VISIBLE);
            viewHolder.bind(mExercises.get(i));
            mExercisesView.addView(exerciseView);
        }
    }

    private ExerciseSummaryViewHolder getExerciseSummaryViewHolder() {
        ExerciseSummaryViewHolder viewHolder =
                (ExerciseSummaryViewHolder) mRecycledViewPool.getRecycledView(ViewType.EXERCISE_SUMMARY);
        if (viewHolder == null) {
            viewHolder = mExercisesAdapter.createViewHolder(null, ViewType.EXERCISE_SUMMARY);
        }
        return viewHolder;
    }
}
