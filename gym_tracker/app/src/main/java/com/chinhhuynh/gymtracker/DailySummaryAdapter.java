package com.chinhhuynh.gymtracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.viewholder.DailySummaryViewHolder;
import com.chinhhuynh.gymtracker.viewholder.ViewType;
import utils.DateUtils;

public final class DailySummaryAdapter extends RecyclerView.Adapter<DailySummaryViewHolder> {

    private static final Comparator<ExerciseSummary> EXERCISE_TIME_COMPARATOR = new Comparator<ExerciseSummary>() {
        @Override
        public int compare(ExerciseSummary lhs, ExerciseSummary rhs) {
            return (int) (lhs.startTime - rhs.startTime);
        }
    };
    private static final Comparator<DailySummary> DAILY_SUMMARY_DATE_COMPARATOR = new Comparator<DailySummary>() {
        @Override
        public int compare(DailySummary lhs, DailySummary rhs) {
            return lhs.mDate.compareTo(rhs.mDate);
        }
    };

    private final LayoutInflater mInflater;
    private final RecyclerView.RecycledViewPool mRecycledViewPool;

    private List<DailySummary> mSummaries;

    public DailySummaryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mRecycledViewPool = new RecyclerView.RecycledViewPool();

        mSummaries = new ArrayList<>();
    }

    @Override
    public DailySummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = newView(parent, viewType);
        DailySummaryViewHolder viewHolder = new DailySummaryViewHolder(mRecycledViewPool, view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DailySummaryViewHolder holder, int position) {
        DailySummary summary = mSummaries.get(position);
        holder.bind(summary);
    }

    @Override
    public void onViewAttachedToWindow(DailySummaryViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public int getItemCount() {
        return mSummaries != null ? mSummaries.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return ViewType.DAILY_SUMMARY;
    }

    public void addSummaries(List<DailySummary> summaries) {
        for (DailySummary summary: summaries) {
            mergeDailySummary(summary);
        }
        Collections.sort(mSummaries, DAILY_SUMMARY_DATE_COMPARATOR);

        notifyDataSetChanged();
    }

    private void mergeDailySummary(DailySummary newSummary) {
        for (DailySummary summary : mSummaries) {
            if (DateUtils.isSameDay(summary.mDate, newSummary.mDate)) {
                // Summary for the same date exists; merges exercises list.
                mergeExerciseSummaries(summary.mExercises, newSummary.mExercises);
                Collections.sort(summary.mExercises, EXERCISE_TIME_COMPARATOR);
                return;
            }
        }
        // Summary for a new date.
        mSummaries.add(newSummary);
    }

    private void mergeExerciseSummaries(List<ExerciseSummary> lhs, List<ExerciseSummary> rhs) {
        if (lhs == null || rhs == null) {
            return;
        }

        for (ExerciseSummary summary : rhs) {
            mergeExerciseSummary(lhs, summary);
        }
    }

    private void mergeExerciseSummary(List<ExerciseSummary> summaries, ExerciseSummary newSummary) {
        for (ExerciseSummary summary : summaries) {
            if (summary.startTime == newSummary.startTime) {
                update(summary, newSummary);
                return;
            }
        }
        summaries.add(newSummary);
    }

    private void update(ExerciseSummary oldSummary, ExerciseSummary newSummary) {
        oldSummary.durationSec = newSummary.durationSec;
        oldSummary.weight = newSummary.weight;
        oldSummary.set = newSummary.set;
        oldSummary.restDurationSec = newSummary.restDurationSec;
        oldSummary.rep = newSummary.rep;
    }

    private View newView(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.daily_summary, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return view;
    }
}
