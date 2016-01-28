package com.chinhhuynh.gymtracker.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import com.chinhhuynh.gymtracker.ExerciseSummaryAdapter;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;

public final class DailySummaryViewHolder extends RecyclerView.ViewHolder {

    private final RecyclerView.RecycledViewPool mRecycledViewPool;
    private final ExerciseSummaryAdapter mExercisesAdapter;
    private final DateFormat mDateFormat;
    private final TextView mDateHeader;
    private final LinearLayout mExercisesView;

    private List<ExerciseSummary> mExercises;

    public DailySummaryViewHolder(RecyclerView.RecycledViewPool recycledViewPool, View itemView) {
        super(itemView);

        mRecycledViewPool = recycledViewPool;
        mDateFormat = DateFormat.getDateInstance();
        mExercisesAdapter = new ExerciseSummaryAdapter(itemView.getContext());

        mDateHeader = (TextView) itemView.findViewById(R.id.date_header);
        mExercisesView = (LinearLayout) itemView.findViewById(R.id.exercises);
    }

    public void bind(DailySummary summary) {
        String dateString = mDateFormat.format(summary.mDate);
        mDateHeader.setText(dateString);

        bindExercises(summary.mExercises);
    }

    private void bindExercises(List<ExerciseSummary> exercises) {
        int viewCount = mExercisesView.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View exerciseView = mExercisesView.getChildAt(i);
            ExerciseSummaryViewHolder viewHolder = (ExerciseSummaryViewHolder) exerciseView.getTag();
            if (i < exercises.size()) {
                exerciseView.setVisibility(View.VISIBLE);
                viewHolder.bind(exercises.get(i));
            } else {
                exerciseView.setVisibility(View.GONE);
                mExercisesView.removeViewAt(i);
                mRecycledViewPool.putRecycledView(viewHolder);
            }
        }
        for (int i = viewCount; i < exercises.size(); i++) {
            ExerciseSummaryViewHolder viewHolder = getExerciseSummaryViewHolder();
            View exerciseView = viewHolder.itemView;
            exerciseView.setVisibility(View.VISIBLE);
            viewHolder.bind(exercises.get(i));
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
