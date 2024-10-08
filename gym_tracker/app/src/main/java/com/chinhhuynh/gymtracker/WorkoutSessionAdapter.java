package com.chinhhuynh.gymtracker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bumptech.glide.Glide;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;

/**
 * Adapter for workout session.
 */
public class WorkoutSessionAdapter extends BaseAdapter {

    public interface EventListener {
        void onItemClicked(Exercise exercise);
        void onItemRemoved(Exercise exercise);
    }

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<Exercise> mExercises;
    private final Set<Exercise> mCompleted;
    private final Map<Exercise, ExerciseSummary> mSummaries;

    private boolean mIsEditMode;
    private EventListener mListener;

    public WorkoutSessionAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mExercises = new ArrayList<>();
        mCompleted = new HashSet<>();
        mSummaries = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mExercises.size();
    }

    @Override
    public Exercise getItem(int position) {
        return mExercises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = newExerciseView();
        }

        final Exercise exercise = mExercises.get(position);
        ExerciseViewHolder viewHolder = (ExerciseViewHolder) convertView.getTag();

        // texts
        viewHolder.title.setText(exercise.mExerciseName);
        String subText = getExerciseSubText(exercise);
        viewHolder.subText.setText(subText);
        viewHolder.subText.setVisibility(subText == null ? View.GONE : View.VISIBLE);

        // action icon
        Drawable actionIcon = getActionIcon(exercise);
        if (actionIcon != null) {
            viewHolder.action.setImageDrawable(getActionIcon(exercise));
            viewHolder.action.setOnClickListener(new ActionClickListener(exercise));
            viewHolder.action.setVisibility(View.VISIBLE);
        } else {
            viewHolder.action.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEditMode) {
                    return;
                }
                notifyItemClicked(exercise);
            }
        });

        // exercise icon
        Glide.with(mContext)
                .load(exercise.getIconAbsolutePath())
                .asBitmap()
                .into(viewHolder.icon);

        return convertView;
    }

    public void setEventListener(EventListener listener) {
        mListener = listener;
    }

    public void addExercises(List<Exercise> exercises) {
        for (Exercise exercise : exercises) {
            addExercise(exercise);
        }
    }

    public void addExercise(Exercise exercise) {
        if (mExercises.contains(exercise)) {
            return;
        }
        mExercises.add(exercise);
        notifyDataSetChanged();
    }

    public Exercise getNext(Exercise exercise) {
        int index = mExercises.indexOf(exercise);
        if (index != -1 && index < mExercises.size() - 1) {
            return mExercises.get(index + 1);
        }
        return null;
    }

    public void setExerciseCompleted(ExerciseSummary summary) {
        mCompleted.add(summary.exercise);
        mSummaries.put(summary.exercise, summary);
        notifyDataSetChanged();
    }

    public void setEditMode(boolean isEditMode) {
        mIsEditMode = isEditMode;
        notifyDataSetChanged();
    }

    private View newExerciseView() {
        View view = mLayoutInflater.inflate(R.layout.workout_session_exercise, null);

        ExerciseViewHolder viewHolder = new ExerciseViewHolder(
                (TextView) view.findViewById(R.id.exercise_title),
                (TextView) view.findViewById(R.id.exercise_subtext),
                (ImageView) view.findViewById(R.id.exercise_icon),
                (ImageView) view.findViewById(R.id.action));
        view.setTag(viewHolder);

        return view;
    }

    private Drawable getActionIcon(Exercise exercise) {
        if (mIsEditMode) {
            return mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp);
        } else if (mCompleted.contains(exercise)) {
            return  mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp);
        }
        return null;
    }

    private String getExerciseSubText(Exercise exercise) {
        ExerciseSummary summary = mSummaries.get(exercise);
        return ExerciseUtils.getExerciseSubText(summary);
    }

    private void notifyItemClicked(Exercise exercise) {
        if (mListener != null) {
            mListener.onItemClicked(exercise);
        }
    }

    private void notifyItemRemoved(Exercise exercise) {
        if (mListener != null) {
            mListener.onItemRemoved(exercise);
        }
    }

    public static class ExerciseViewHolder {
        public final TextView title;
        public final TextView subText;
        public final ImageView icon;
        public final ImageView action;

        public ExerciseViewHolder(TextView title, TextView subText, ImageView icon, ImageView action) {
            this.title = title;
            this.subText = subText;
            this.icon = icon;
            this.action = action;
        }
    }

    private class ActionClickListener implements View.OnClickListener {

        private final Exercise mExercise;

        public ActionClickListener(Exercise exercise) {
            mExercise = exercise;
        }

        @Override
        public void onClick(View v) {
            if (mIsEditMode) {
                mExercises.remove(mExercise);
                mSummaries.remove(mExercise);
                mCompleted.remove(mExercise);
                notifyItemRemoved(mExercise);
                notifyDataSetChanged();
            }
        }
    }
}
