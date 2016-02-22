package com.chinhhuynh.gymtracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExercisePickerDialog {

    public interface EventsListener {
        void onExerciseSelect(String exercise);
    }

    private static final int MUSCLE_GROUP_POSITION = 0;
    private static final int EXERCISE_POSITION = 1;

    private static String[] MUSCLE_GROUPS = {
            Exercise.MUSCLE_GROUP_ABS,
            Exercise.MUSCLE_GROUP_CHEST,
            Exercise.MUSCLE_GROUP_BICEPS,
            Exercise.MUSCLE_GROUP_TRICEPS,
            Exercise.MUSCLE_GROUP_SHOULDERS,
            Exercise.MUSCLE_GROUP_TRAPS,
            Exercise.MUSCLE_GROUP_QUADRICEPS,
            Exercise.MUSCLE_GROUP_GLUTES,
            Exercise.MUSCLE_GROUP_ABDUCTORS,
            Exercise.MUSCLE_GROUP_FOREARMS,
            Exercise.MUSCLE_GROUP_MIDDLE_BACK,
            Exercise.MUSCLE_GROUP_ADDUCTORS,
            Exercise.MUSCLE_GROUP_NECK,
            Exercise.MUSCLE_GROUP_HAMSTRINGS,
            Exercise.MUSCLE_GROUP_CALVES,
            Exercise.MUSCLE_GROUP_LOWER_BACK,
    };

    private static String[] ABS_EXERCISES = {
            Exercise.EXERCISE_RUSSIAN_TWIST,
            Exercise.EXERCISE_WEIGHTED_SUITCASE_CRUNCH,
            Exercise.EXERCISE_BOTTOMS_UP,
            Exercise.EXERCISE_SPIDER_CRAWL,
            Exercise.EXERCISE_SPELL_CASTER,
            Exercise.EXERCISE_SIT_UP,
            Exercise.EXERCISE_HANGING_LEG_RAISE,
            Exercise.EXERCISE_ROPE_CRUNCH,
    };

    private static String[] CHEST_EXERCISES = {
            Exercise.EXERCISE_PUSH_UP,
            Exercise.EXERCISE_DUMBBELL_BENCH_PRESS,
            Exercise.EXERCISE_BARBELL_BENCH_PRESS,
            Exercise.EXERCISE_INCLINE_BARBELL_PRESS,
            Exercise.EXERCISE_LOW_CABLE_CROSSOVER,
            Exercise.EXERCISE_BUTTERFLY,
    };

    private static Map<String, String[]> EXERCISES;
    static {
        EXERCISES = new HashMap<>();
        EXERCISES.put(Exercise.MUSCLE_GROUP_ABS, ABS_EXERCISES);
        EXERCISES.put(Exercise.MUSCLE_GROUP_CHEST, CHEST_EXERCISES);
    }

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResId;

    private AlertDialog mAlertDialog;
    private ViewPager mLayoutView;
    private View mMuscleGroupView;
    private View mExercisesLayoutView;
    private ListView mExercisesView;
    private ArrayAdapter<String> mExercisesAdapter;
    private TextView mMuscleGroupTitleView;

    private EventsListener mListener;

    public ExercisePickerDialog(Context context, int layoutResId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mLayoutResId = layoutResId;
    }

    public ExercisePickerDialog listener(EventsListener listener) {
        mListener = listener;
        return this;
    }

    public void show() {
        PickerPagerAdapter adapter = new PickerPagerAdapter();
        mLayoutView = (ViewPager) mLayoutInflater.inflate(mLayoutResId, null);
        mLayoutView.setAdapter(adapter);

        mMuscleGroupView = newMuscleGroupPickerView();
        mExercisesLayoutView = newExercisesLayoutView();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.select_exercise_title);
        alertDialogBuilder.setView(mLayoutView);

        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
    }

    private View newMuscleGroupPickerView() {
        ListView view = (ListView) mLayoutInflater.inflate(R.layout.exercise_picker_muscle_group, null);
        ArrayAdapter<String> muscleGroupAdapter =
                new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, MUSCLE_GROUPS);
        view.setAdapter(muscleGroupAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String muscleGroup = MUSCLE_GROUPS[position];
                String[] exercises = EXERCISES.get(muscleGroup);
                mMuscleGroupTitleView.setText(muscleGroup);
                mExercisesAdapter = exercises != null
                        ? new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, exercises)
                        : null;
                mExercisesView.setAdapter(mExercisesAdapter);
                mLayoutView.setCurrentItem(EXERCISE_POSITION, true /*smoothScroll*/);
            }
        });
        return view;
    }

    private View newExercisesLayoutView() {
        View view = mLayoutInflater.inflate(R.layout.exercise_picker_exercises, null);

        mExercisesView = (ListView) view.findViewById(R.id.exercises);
        mExercisesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exercise = mExercisesAdapter.getItem(position);
                notifyExerciseSelect(exercise);
                mAlertDialog.dismiss();
            }
        });

        mMuscleGroupTitleView = (TextView) view.findViewById(R.id.muscle_group_title);
        mMuscleGroupTitleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() <= mMuscleGroupTitleView.getTotalPaddingLeft()) {
                        mLayoutView.setCurrentItem(MUSCLE_GROUP_POSITION, true /*smoothScroll*/);
                    }
                }
                return true;
            }
        });
        return view;
    }

    private void notifyExerciseSelect(String exercise) {
        if (mListener != null) {
            mListener.onExerciseSelect(exercise);
        }
    }

    private class PickerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            switch (position) {
                case MUSCLE_GROUP_POSITION:
                    container.addView(mMuscleGroupView);
                    return mMuscleGroupView;

                case EXERCISE_POSITION:
                    container.addView(mExercisesLayoutView);
                    return mExercisesLayoutView;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
