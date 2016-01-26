package com.chinhhuynh.gymtracker.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinhhuynh.gymtracker.R;

public final class DailySummaryViewHolder extends RecyclerView.ViewHolder {

    private final TextView mDateHeader;
    private final LinearLayout mExercisesView;

    public DailySummaryViewHolder(View itemView) {
        super(itemView);

        mDateHeader = (TextView) itemView.findViewById(R.id.date_header);
        mExercisesView = (LinearLayout) itemView.findViewById(R.id.exercises);
    }


}
