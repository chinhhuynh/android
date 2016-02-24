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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.chinhhuynh.gymtracker.model.Exercise;

/**
 * Adapter for workout session.
 */
public class WorkoutSessionAdapter extends BaseAdapter {

    public interface EventListener {
        void onItemRemoved(Exercise exercise);
    }

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<Exercise> mExercises;
    private final Set<Exercise> mCompleted;

    private boolean mIsEditMode;
    private EventListener mListener;

    public WorkoutSessionAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mExercises = new ArrayList<>();
        mCompleted = new HashSet<>();
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

        Exercise exercise = mExercises.get(position);
        ExerciseViewHolder viewHolder = (ExerciseViewHolder) convertView.getTag();
        viewHolder.title.setText(exercise.mExerciseName);

        Drawable actionIcon = getActionIcon(exercise);
        if (actionIcon != null) {
            viewHolder.action.setImageDrawable(getActionIcon(exercise));
            viewHolder.action.setOnClickListener(new ActionClickListener(exercise));
            viewHolder.action.setVisibility(View.VISIBLE);
        } else {
            viewHolder.action.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setEventListener(EventListener listener) {
        mListener = listener;
    }

    public void addExercise(Exercise exercise) {
        mExercises.add(exercise);
        notifyDataSetChanged();
    }

    public void setEditMode(boolean isEditMode) {
        mIsEditMode = isEditMode;
        notifyDataSetChanged();
    }

    private View newExerciseView() {
        View view = mLayoutInflater.inflate(R.layout.workout_session_exercise, null);

        ExerciseViewHolder viewHolder = new ExerciseViewHolder(
                (TextView) view.findViewById(R.id.exercise),
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

    private void notifyItemRemoved(Exercise exercise) {
        if (mListener != null) {
            mListener.onItemRemoved(exercise);
        }
    }

    public static class ExerciseViewHolder {
        public final TextView title;
        public final ImageView action;

        public ExerciseViewHolder(TextView title, ImageView action) {
            this.title = title;
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
                notifyItemRemoved(mExercise);
                notifyDataSetChanged();
            }
        }
    }
}
