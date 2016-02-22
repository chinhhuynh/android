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

import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExercisePickerDialog {

    private static final int MUSCLE_GROUP_POSITION = 0;
    private static final int EXERCISE_POSITION = 1;

    private static String[] MUSCLE_GROUPS = {
            Exercise.MUSCLE_GROUP_ABS,
            Exercise.MUSCLE_GROUP_CHEST,
            Exercise.MUSCLE_GROUP_LOWER_BACK,
            Exercise.MUSCLE_GROUP_TRAPS,
            Exercise.MUSCLE_GROUP_ABDUCTORS,
            Exercise.MUSCLE_GROUP_FOREARMS,
            Exercise.MUSCLE_GROUP_MIDDLE_BACK,
            Exercise.MUSCLE_GROUP_TRICEPS,
            Exercise.MUSCLE_GROUP_ADDUCTORS,
            Exercise.MUSCLE_GROUP_GLUTES,
            Exercise.MUSCLE_GROUP_NECK,
            Exercise.MUSCLE_GROUP_BICEPS,
            Exercise.MUSCLE_GROUP_HAMSTRINGS,
            Exercise.MUSCLE_GROUP_QUADRICEPS,
            Exercise.MUSCLE_GROUP_CALVES,
            Exercise.MUSCLE_GROUP_SHOULDERS,
    };

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResId;

    private ViewPager mLayoutView;
    private View mMuscleGroupView;
    private View mExercisesView;
    private TextView mMuscleGroupTitleView;

    public ExercisePickerDialog(Context context, int layoutResId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mLayoutResId = layoutResId;
    }

    public void show() {
        PickerPagerAdapter adapter = new PickerPagerAdapter();
        mLayoutView = (ViewPager) mLayoutInflater.inflate(mLayoutResId, null);
        mLayoutView.setAdapter(adapter);

        mMuscleGroupView = newMuscleGroupPickerView();
        mExercisesView = newExercisesView();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.select_exercise_title);
        alertDialogBuilder.setView(mLayoutView);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private View newMuscleGroupPickerView() {
        ListView view = (ListView) mLayoutInflater.inflate(R.layout.exercise_picker_muscle_group, null);
        ArrayAdapter<String> muscleGroupAdapter =
                new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, MUSCLE_GROUPS);
        view.setAdapter(muscleGroupAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMuscleGroupTitleView.setText(MUSCLE_GROUPS[position]);
                mLayoutView.setCurrentItem(EXERCISE_POSITION, true /*smoothScroll*/);
            }
        });
        return view;
    }

    private View newExercisesView() {
        View view = mLayoutInflater.inflate(R.layout.exercise_picker_exercises, null);
        mMuscleGroupTitleView = (TextView) view.findViewById(R.id.muscle_group_title);
        mMuscleGroupTitleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() <= mMuscleGroupTitleView.getTotalPaddingLeft()) {
                        mLayoutView.setCurrentItem(MUSCLE_GROUP_POSITION, true /*smoothScroll*/);
                    }
                }
                return true;
            }
        });
        return view;
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
                    container.addView(mExercisesView);
                    return mExercisesView;
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
