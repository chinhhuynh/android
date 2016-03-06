package com.chinhhuynh.gymtracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.viewholder.ExerciseSummaryViewHolder;
import com.chinhhuynh.gymtracker.viewholder.ViewType;

public final class ExerciseSummaryAdapter extends RecyclerView.Adapter<ExerciseSummaryViewHolder> {

    private final LayoutInflater mInflater;
    private final int mViewHeight;

    private List<ExerciseSummary> mExercises;

    public ExerciseSummaryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

        mViewHeight = context.getResources().getDimensionPixelOffset(R.dimen.history_exercise_list_item_height);
    }

    @Override
    public ExerciseSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.exercise_summary, parent);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, mViewHeight);
        view.setLayoutParams(layoutParams);
        ExerciseSummaryViewHolder viewHolder = new ExerciseSummaryViewHolder(view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseSummaryViewHolder holder, int position) {
        ExerciseSummary exercise = mExercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public void onViewAttachedToWindow(ExerciseSummaryViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public int getItemCount() {
        return mExercises != null ? mExercises.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return ViewType.EXERCISE_SUMMARY;
    }

    public void setExercises(List<ExerciseSummary> exercises) {
        mExercises = exercises;
    }
}
