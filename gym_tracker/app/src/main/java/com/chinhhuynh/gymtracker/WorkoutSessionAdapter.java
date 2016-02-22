package com.chinhhuynh.gymtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chinhhuynh.gymtracker.model.Exercise;

/**
 * Adapter for workout session.
 */
public class WorkoutSessionAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<Exercise> mExercises;

    public WorkoutSessionAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mExercises = new ArrayList<>();
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
        return convertView;
    }

    public void addExercise(Exercise exercise) {
        mExercises.add(exercise);
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

    public class ExerciseViewHolder {
        public final TextView title;
        public final ImageView action;

        public ExerciseViewHolder(TextView title, ImageView action) {
            this.title = title;
            this.action = action;
        }
    }
}
