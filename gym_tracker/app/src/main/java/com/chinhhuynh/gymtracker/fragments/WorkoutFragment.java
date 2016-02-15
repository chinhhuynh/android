package com.chinhhuynh.gymtracker.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import com.chinhhuynh.gymtracker.GymTrackerApplication;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.views.RestCountdown;
import com.chinhhuynh.gymtracker.views.StartButton;

/**
 * Fragment for starting a workout.
 */
public final class WorkoutFragment extends Fragment implements RestCountdown.CountdownListener {

    public static final String TAG = "WorkoutFragment";

    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final long ANIMATE_START_BUTTON_DURATION = DateUtils.SECOND_IN_MILLIS / 4;
    private static final int REST_DURATION_SECONDS = 45;
    private static final long ONE_TENTH_SECOND = DateUtils.SECOND_IN_MILLIS / 10;
    private static final long HALF_SECOND = DateUtils.SECOND_IN_MILLIS / 2;
    private static final int MIN_WEIGHT = 0;
    private static final int MAX_WEIGHT = 200;
    private static final int WEIGHT_INTERVAL = 5;
    private static final int MIN_REST_DURATION = 5;
    private static final int MAX_REST_DURATION = 120;
    private static final int REST_DURATION_INTERVAL = 5;

    private static final String CLOCK_RESET = "00:00";
    private static final String CLOCK_DISPLAY = "%02d:%02d";

    private final float mMinimizeShiftDistance;

    private AppCompatActivity mActivity;
    private Context mContext;
    private Handler mHandler;
    private Vibrator mVibrator;
    private TextView mClockView;
    private StartButton mStartButton;
    private RestCountdown mRestCountdownView;

    private TextView mWeightView;
    private TextView mRestDurationView;

    private long mStartTime;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now - mStartTime);
            mClockView.setText(String.format(CLOCK_DISPLAY, seconds / 60, seconds % 60));
            mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        }
    };

    public WorkoutFragment() {
        Resources resources = GymTrackerApplication.getAppContext().getResources();
        int buttonSize = resources.getDimensionPixelSize(R.dimen.button_size);
        mMinimizeShiftDistance = buttonSize / (2 * SQRT_2);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_fragment, container, false);

        mActivity = (AppCompatActivity) getActivity();
        mContext = mActivity;
        mHandler = new Handler(Looper.getMainLooper());
        mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);

        mClockView = (TextView) fragmentLayout.findViewById(R.id.clock);
        mStartButton = (StartButton) fragmentLayout.findViewById(R.id.start_button);
        mRestCountdownView = (RestCountdown) fragmentLayout.findViewById(R.id.rest_countdown);

        mWeightView = (TextView) fragmentLayout.findViewById(R.id.weight);
        mRestDurationView = (TextView) fragmentLayout.findViewById(R.id.rest_duration);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mStartButton.getState()) {
                    case StartButton.STATE_START:
                        startWorkout();
                        break;
                    case StartButton.STATE_STOP:
                        mClockView.setText(CLOCK_RESET);
                        mHandler.removeCallbacks(mClockTimer);
                        changeToStartButton();
                        break;
                }
            }
        });
        mRestCountdownView.setListener(this);
        mRestCountdownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rest();
            }
        });

        mWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout pickerLayout = (FrameLayout) inflater.inflate(R.layout.number_picker, null);
                showWeightPicker(pickerLayout);
            }
        });
        mRestDurationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout pickerLayout = (FrameLayout) inflater.inflate(R.layout.number_picker, null);
                showRestDurationPicker(pickerLayout);
            }
        });

        return fragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupActionBar();
    }

    @Override
    public void onCountdownFinished() {
        mVibrator.vibrate(HALF_SECOND);
        mClockView.setText(CLOCK_RESET);
        changeToStartButton();
    }

    private void startWorkout() {
        mRestCountdownView.stop();
        mRestCountdownView.setRestDuration(REST_DURATION_SECONDS);
        mRestCountdownView.setVisibility(View.VISIBLE);

        mStartTime = System.currentTimeMillis();
        mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        changeToStopButton();
    }

    private void rest() {
        mHandler.removeCallbacks(mClockTimer);
        mRestCountdownView.countdown();
    }

    private void changeToStopButton() {
        mStartButton.setState(StartButton.STATE_STOP);
        PropertyValuesHolder shiftRight = PropertyValuesHolder.ofFloat("translationX", 0, mMinimizeShiftDistance);
        PropertyValuesHolder shiftDown = PropertyValuesHolder.ofFloat("translationY", 0, mMinimizeShiftDistance);

        PropertyValuesHolder scaleWidth = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f);
        PropertyValuesHolder scaleHeight = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f);

        ObjectAnimator
                .ofPropertyValuesHolder(mStartButton, shiftRight, shiftDown, scaleWidth, scaleHeight)
                .setDuration(ANIMATE_START_BUTTON_DURATION)
                .start();
    }

    private void changeToStartButton() {
        mStartButton.setState(StartButton.STATE_START);
        PropertyValuesHolder shiftLeft = PropertyValuesHolder.ofFloat("translationX", mMinimizeShiftDistance, 0);
        PropertyValuesHolder shiftUp = PropertyValuesHolder.ofFloat("translationY", mMinimizeShiftDistance, 0);

        PropertyValuesHolder scaleWidth = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder scaleHeight = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);

        ObjectAnimator
                .ofPropertyValuesHolder(mStartButton, shiftLeft, shiftUp, scaleWidth, scaleHeight)
                .setDuration(ANIMATE_START_BUTTON_DURATION)
                .start();
    }

    private void showWeightPicker(View pickerLayout) {
        final NumberPicker numberPicker = (NumberPicker) pickerLayout.findViewById(R.id.number_picker);
        int currentWeight = Integer.parseInt(mWeightView.getText().toString());
        int count = (MAX_WEIGHT - MIN_WEIGHT) / WEIGHT_INTERVAL + 1;
        final int startIndex = MIN_REST_DURATION / REST_DURATION_INTERVAL;
        String[] displayValues = new String[count + 1 - startIndex];
        for (int i = startIndex; i <= count; i++) {
            int value = WEIGHT_INTERVAL * i;
            displayValues[i - startIndex] = Integer.toString(value);
        }

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(count - 1);
        numberPicker.setDisplayedValues(displayValues);
        numberPicker.setValue(currentWeight / REST_DURATION_INTERVAL - startIndex);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.select_weight_title);
        alertDialogBuilder.setView(pickerLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int weight = (numberPicker.getValue() + startIndex) * REST_DURATION_INTERVAL;
                                mWeightView.setText(Integer.toString(weight));
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showRestDurationPicker(View pickerLayout) {
        final NumberPicker numberPicker = (NumberPicker) pickerLayout.findViewById(R.id.number_picker);
        int currentDuration = Integer.parseInt(mRestDurationView.getText().toString());
        int count = (MAX_REST_DURATION - MIN_REST_DURATION) / REST_DURATION_INTERVAL + 1;
        final int startIndex = MIN_REST_DURATION / REST_DURATION_INTERVAL;
        String[] displayValues = new String[count + 1 - startIndex];
        for (int i = startIndex; i <= count; i++) {
            int value = REST_DURATION_INTERVAL * i;
            displayValues[i - startIndex] = Integer.toString(value);
        }

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(count - 1);
        numberPicker.setDisplayedValues(displayValues);
        numberPicker.setValue(currentDuration / REST_DURATION_INTERVAL - startIndex);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.select_rest_duration_title);
        alertDialogBuilder.setView(pickerLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int duration = (numberPicker.getValue() + startIndex) * REST_DURATION_INTERVAL;
                                mRestDurationView.setText(Integer.toString(duration));
                                mRestCountdownView.setRestDuration(duration);
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setupActionBar() {
        mActivity.getSupportActionBar().setTitle(R.string.workout_title);
    }
}
