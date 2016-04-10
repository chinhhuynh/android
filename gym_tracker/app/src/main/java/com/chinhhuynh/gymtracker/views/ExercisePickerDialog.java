package com.chinhhuynh.gymtracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.database.table.ExerciseTable;
import com.chinhhuynh.gymtracker.loaders.ExerciseLoader;
import com.chinhhuynh.gymtracker.model.Exercise;

public final class ExercisePickerDialog {

    public interface EventsListener {
        void onExerciseSelect(Exercise exercise);
    }

    private static final int EXERCISE_LOADER = 0;

    private static final int MUSCLE_GROUP_POSITION = 0;
    private static final int EXERCISE_POSITION = 1;

    private static String[] MUSCLE_GROUPS = {
            Exercise.MUSCLE_GROUP_CHEST,
            Exercise.MUSCLE_GROUP_ARM,
            Exercise.MUSCLE_GROUP_LEG,
            Exercise.MUSCLE_GROUP_ABS,
            Exercise.MUSCLE_GROUP_BACK,
            Exercise.MUSCLE_GROUP_SHOULDER,
            Exercise.MUSCLE_GROUP_GLUTE,
    };

    private final Context mContext;
    private final Resources mResources;
    private final LayoutInflater mLayoutInflater;
    private final LoaderManager mLoaderManager;
    private final int mLayoutResId;
    private final int mIconSize;
    private final int mIconPadding;
    private final int mExerciseLeftPadding;

    private AlertDialog mAlertDialog;
    private ViewPager mLayoutView;
    private View mMuscleGroupView;
    private View mExercisesLayoutView;
    private ListView mExercisesView;
    private ExerciseCursorAdapter mExercisesAdapter;
    private TextView mMuscleGroupTitleView;

    private ExerciseLoaderCallbacks mExerciseLoaderCallbacks;
    private String mMuscleGroup;

    private EventsListener mListener;

    public ExercisePickerDialog(Context context, int layoutResId, LoaderManager loaderManager) {
        mContext = context;
        mResources = mContext.getResources();
        mLayoutInflater = LayoutInflater.from(context);
        mLoaderManager = loaderManager;
        mLayoutResId = layoutResId;

        mIconSize = mResources.getDimensionPixelOffset(R.dimen.exercise_picker_icon_size);
        mIconPadding = mResources.getDimensionPixelOffset(R.dimen.exercise_picker_icon_padding);
        mExerciseLeftPadding = mResources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
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

        mMuscleGroup = "";
        mExerciseLoaderCallbacks = new ExerciseLoaderCallbacks();
        mLoaderManager.initLoader(EXERCISE_LOADER, null, mExerciseLoaderCallbacks);

        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && mLayoutView.getCurrentItem() == EXERCISE_POSITION) {
                    mLayoutView.setCurrentItem(MUSCLE_GROUP_POSITION, true /*smoothScroll*/);
                    return true;
                }
                return false;
            }
        });
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
                mMuscleGroup = MUSCLE_GROUPS[position];
                mMuscleGroupTitleView.setText(mMuscleGroup);
                mLoaderManager.restartLoader(EXERCISE_LOADER, null, mExerciseLoaderCallbacks);
                mLayoutView.setCurrentItem(EXERCISE_POSITION, true /*smoothScroll*/);
            }
        });
        return view;
    }

    private final class ExerciseLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new ExerciseLoader(mContext, mMuscleGroup);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mExercisesAdapter = new ExerciseCursorAdapter(mContext, data, false /*autoRequery*/);
            mExercisesView.setAdapter(mExercisesAdapter);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // no-op.
        }
    }

    private View newExercisesLayoutView() {
        View view = mLayoutInflater.inflate(R.layout.exercise_picker_exercises, null);

        mExercisesView = (ListView) view.findViewById(R.id.exercises);
        mExercisesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise exercise = mExercisesAdapter.getItem(position);
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

    private void notifyExerciseSelect(Exercise exercise) {
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

    private final class ExerciseCursorAdapter extends CursorAdapter {

        private ExerciseCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
            super(context, cursor, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final TextView textView = (TextView) mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null);
            textView.setPadding(mExerciseLeftPadding, 0, 0, 0);
            Target target = new SimpleTarget<Bitmap>(mIconSize, mIconSize) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(
                            new BitmapDrawable(mResources, resource), null, null, null);
                }
            };
            textView.setTag(target);
            return textView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Exercise exercise = getExercise(cursor);

            final TextView exerciseView = (TextView) view.findViewById(android.R.id.text1);
            exerciseView.setText(exercise.mExerciseName);
            exerciseView.setCompoundDrawablePadding(mIconPadding);

            Glide.with(mContext)
                    .load(exercise.getIconAbsolutePath())
                    .asBitmap()
                    .into((Target) view.getTag());
        }

        @Override
        public Exercise getItem(int position) {
            Cursor cursor = (Cursor) super.getItem(position);
            return getExercise(cursor);
        }

        private Exercise getExercise(Cursor cursor) {
            String name = cursor.getString(ExerciseTable.COL_IDX_NAME);
            String muscleGroup = cursor.getString(ExerciseTable.COL_IDX_MUSCLE_GROUP);
            String iconFileName = cursor.getString(ExerciseTable.COL_IDX_ICON_FILE_NAME);

            return new Exercise(name, muscleGroup, iconFileName);
        }
    }
}
