package com.chinhhuynh.gymtracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.chinhhuynh.gymtracker.model.DailySummary;
import com.chinhhuynh.gymtracker.viewholder.DailySummaryViewHolder;
import com.chinhhuynh.gymtracker.viewholder.ViewType;

public final class DailySummaryAdapter extends RecyclerView.Adapter<DailySummaryViewHolder> {

    private final LayoutInflater mInflater;
    private final RecyclerView.RecycledViewPool mRecycledViewPool;

    private List<DailySummary> mSummaries;

    public DailySummaryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public DailySummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.daily_summary, null);
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
    public int getItemCount() {
        return mSummaries != null ? mSummaries.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return ViewType.DAILY_SUMMARY;
    }

    public void setSummaries(List<DailySummary> summaries) {
        mSummaries = summaries;
    }
}
