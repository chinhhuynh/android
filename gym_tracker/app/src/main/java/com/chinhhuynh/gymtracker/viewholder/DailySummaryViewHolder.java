package com.chinhhuynh.gymtracker.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import com.chinhhuynh.gymtracker.ExerciseSummaryAdapter;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.fragments.RepeatExercisesListener;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;

public final class DailySummaryViewHolder extends RecyclerView.ViewHolder {

    private final RecyclerView.RecycledViewPool mRecycledViewPool;
    private final ExerciseSummaryAdapter mExercisesAdapter;
    private final TextView mDateHeader;
    private final View mRepeatView;
    private final LinearLayout mExercisesView;

    private RepeatExercisesListener mListener;
    private List<ExerciseSummary> mExerciseSummaries;
    private List<Exercise> mExercises;

    public DailySummaryViewHolder(RecyclerView.RecycledViewPool recycledViewPool, View itemView) {
        super(itemView);

        mRecycledViewPool = recycledViewPool;
        mExercisesAdapter = new ExerciseSummaryAdapter(itemView.getContext());

        mDateHeader = (TextView) itemView.findViewById(R.id.date_header);
        mRepeatView = itemView.findViewById(R.id.repeat);
        mExercisesView = (LinearLayout) itemView.findViewById(R.id.exercises);

        mRepeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyRepeatExercises(mExercises);
            }
        });
    }

    public void setListener(RepeatExercisesListener listener) {
        mListener = listener;
    }

    public void bind(DailySummary summary) {
        mDateHeader.setText(summary.getDateText());

        mExerciseSummaries = new ArrayList<>(summary.mExercises);
        mExercises = Lists.transform(mExerciseSummaries, new Function<ExerciseSummary, Exercise>() {
            @Override
            public Exercise apply(ExerciseSummary exerciseSummary) {
                return exerciseSummary.exercise;
            }
        });
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
        int reuseViewCount = Math.min(viewCount, mExerciseSummaries.size());
        for (int i = 0; i < reuseViewCount; i++) {
            View exerciseView = mExercisesView.getChildAt(i);
            ExerciseSummaryViewHolder viewHolder = (ExerciseSummaryViewHolder) exerciseView.getTag();

            if (i < mExerciseSummaries.size()) {
                boolean isLastItem = i == mExerciseSummaries.size() - 1;
                exerciseView.setVisibility(View.VISIBLE);
                viewHolder.bind(mExerciseSummaries.get(i), isLastItem);
            } else {
                exerciseView.setVisibility(View.GONE);
                mRecycledViewPool.putRecycledView(viewHolder);
            }
        }

        if (reuseViewCount < viewCount) {
            // if number of views exceeds number of exercises to display, remove extra views.
            mExercisesView.removeViews(reuseViewCount, viewCount - reuseViewCount);
        } else {
            // otherwise, create new views.
            for (int i = viewCount; i < mExerciseSummaries.size(); i++) {
                boolean isLastItem = i == mExerciseSummaries.size() - 1;
                ExerciseSummaryViewHolder viewHolder = getExerciseSummaryViewHolder();
                View exerciseView = viewHolder.itemView;
                exerciseView.setVisibility(View.VISIBLE);
                viewHolder.bind(mExerciseSummaries.get(i), isLastItem);
                mExercisesView.addView(exerciseView);
            }
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

    private void notifyRepeatExercises(List<Exercise> exercises) {
        if (mListener != null) {
            mListener.onRepeatExercises(exercises);
        }
    }
}
