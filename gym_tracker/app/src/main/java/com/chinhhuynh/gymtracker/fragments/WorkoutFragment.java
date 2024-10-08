package com.chinhhuynh.gymtracker.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.chinhhuynh.gymtracker.GymTrackerApplication;
import com.chinhhuynh.gymtracker.R;
import com.chinhhuynh.gymtracker.model.Exercise;
import com.chinhhuynh.gymtracker.model.ExerciseSummary;
import com.chinhhuynh.gymtracker.tasks.ExtractAssetsTask;
import com.chinhhuynh.gymtracker.views.NumberPickerDialog;
import com.chinhhuynh.gymtracker.views.RestCountdown;
import com.chinhhuynh.gymtracker.views.StartButton;
import com.chinhhuynh.lifecycle.activity.OnBackPressed;
import org.jetbrains.annotations.NotNull;

/**
 * Fragment for starting a workout.
 */
public final class WorkoutFragment extends Fragment implements
        OnBackPressed,
        RestCountdown.CountdownListener {

    public static final String TAG = WorkoutFragment.class.getSimpleName();
    public static final int ACTIVE_WORKOUT_NOTIF_ID = 1;

    public interface WorkoutEventListener {
        void onExerciseCompleted(@NotNull ExerciseSummary summary);
    }

    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final long ANIMATE_START_BUTTON_DURATION = DateUtils.SECOND_IN_MILLIS / 4;
    private static final long ONE_TENTH_SECOND = DateUtils.SECOND_IN_MILLIS / 10;
    private static final int MIN_WEIGHT = 0;
    private static final int MAX_WEIGHT = 200;
    private static final int WEIGHT_INTERVAL = 5;
    private static final int MIN_REST_DURATION = 5;
    private static final int MAX_REST_DURATION = 120;
    private static final int REST_DURATION_INTERVAL = 5;

    private static final String CLOCK_RESET = "00:00";
    private static final String CLOCK_DISPLAY = "%02d:%02d";

    private final String mRestNotifText;
    private final String mWorkoutNotifText;

    private final float mMinimizeShiftDistance;
    private final BroadcastReceiver mNotifActionHandler;

    private AppCompatActivity mActivity;
    private Context mContext;
    private Resources mResources;
    private Handler mHandler;
    private TextView mClockView;
    private StartButton mStartButton;
    private RestCountdown mRestCountdownView;
    private NumberPickerDialog mWeightPicker;
    private NumberPickerDialog mRestDurationPicker;
    private NotificationManager mNotificationManager;
    private PowerManager.WakeLock mWakeLock;
    private Ringtone mRingtone;
    private NotificationCompat.Builder mNotificationBuilder;

    private Toolbar mToolbar;
    private TextView mSetView;
    private TextView mWeightView;
    private TextView mRestDurationView;

    private Exercise mExercise;
    private ExerciseList mExerciseList;
    private ExerciseSummary mSummary;
    private WorkoutEventListener mListener;
    private int mDurationSec;
    private long mClockMinutes;
    private long mClockSeconds;
    private boolean mWasPaused;
    private boolean mIsWorkingOut;
    private boolean mIsResting;

    private long mCurrentSetStartTime;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long elapsed = TimeUnit.MILLISECONDS.toSeconds(now - mCurrentSetStartTime);
            long minutes = elapsed / 60;
            long seconds = elapsed % 60;
            if (minutes != mClockMinutes || seconds != mClockSeconds) {
                onClockTextChanged(minutes, seconds);
            }
            mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        }
    };

    public WorkoutFragment() {
        Resources resources = GymTrackerApplication.getAppContext().getResources();
        int buttonSize = resources.getDimensionPixelSize(R.dimen.button_size);
        mMinimizeShiftDistance = buttonSize / (2 * SQRT_2);

        mRestNotifText = resources.getString(R.string.rest_notif_text);
        mWorkoutNotifText = resources.getString(R.string.workout_notif_text);

        mNotifActionHandler = new NotifActionHandler();
    }

    @SuppressLint("ValidFragment")
    public WorkoutFragment(ExerciseList exerciseList) {
        this();
        mExerciseList = exerciseList;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.workout_fragment, container, false);

        mActivity = (AppCompatActivity) getActivity();
        mContext = mActivity;
        mResources = mContext.getResources();
        mHandler = new Handler(Looper.getMainLooper());
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Activity.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);

        String soundFolder = mContext.getDir(ExtractAssetsTask.SOUND_FOLDER, Context.MODE_PRIVATE).getAbsolutePath();
        Uri notification = Uri.fromFile(new File(soundFolder.concat("/").concat("WorkoutStart.ogg")));
        mRingtone = RingtoneManager.getRingtone(mContext, notification);

        mClockView = (TextView) fragmentLayout.findViewById(R.id.clock);
        mStartButton = (StartButton) fragmentLayout.findViewById(R.id.start_button);
        mRestCountdownView = (RestCountdown) fragmentLayout.findViewById(R.id.rest_countdown);
        mToolbar = (Toolbar) fragmentLayout.findViewById(R.id.toolbar);
        mSetView = (TextView) fragmentLayout.findViewById(R.id.workout_set);
        mWeightView = (TextView) fragmentLayout.findViewById(R.id.workout_weight);
        mRestDurationView = (TextView) fragmentLayout.findViewById(R.id.workout_rest_duration);

        mNotificationBuilder = new NotificationCompat.Builder(mContext)
                .setOngoing(true) // not dismissible.
                .setOnlyAlertOnce(true);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mStartButton.getState()) {
                    case StartButton.STATE_START:
                        startWorkout();
                        break;
                    case StartButton.STATE_STOP:
                        if (!mIsResting) {
                            increaseSet();
                        }
                        stopWorkout();
                        break;
                }
            }
        });
        mRestCountdownView.setListener(this);
        mRestCountdownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRestCountdownView.isActive()) {
                    stopRestCountdown();
                } else {
                    increaseSet();
                    rest();
                }
            }
        });

        mWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeightPicker.selectedValue(getCurrentWeight()).show();
            }
        });
        mRestDurationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRestDurationPicker.selectedValue(getCurrentRestDuration()).show();
            }
        });

        mWeightPicker = new NumberPickerDialog(mContext, R.layout.number_picker)
                .title(R.string.select_weight_title)
                .minValue(MIN_WEIGHT)
                .maxValue(MAX_WEIGHT)
                .interval(WEIGHT_INTERVAL)
                .listener(new NumberPickerDialog.EventsListener() {

                    @Override
                    public void onNumberSelect(int value) {
                        mWeightView.setText(Integer.toString(value));
                    }

                    @Override
                    public void onCancel() {
                        // no-op.
                    }
                });

        mRestDurationPicker = new NumberPickerDialog(mContext, R.layout.number_picker)
                .title(R.string.select_rest_duration_title)
                .minValue(MIN_REST_DURATION)
                .maxValue(MAX_REST_DURATION)
                .interval(REST_DURATION_INTERVAL)
                .listener(new NumberPickerDialog.EventsListener() {

                    @Override
                    public void onNumberSelect(int value) {
                        mRestDurationView.setText(Integer.toString(value));
                        mRestCountdownView.setRestDuration(value);
                    }

                    @Override
                    public void onCancel() {
                        // no-op.
                    }
                });

        updateViews();
        setAlarmVolume();

        mContext.registerReceiver(mNotifActionHandler, new IntentFilter(NotifActionHandler.ACTION_NEXT), null, null);
        mContext.registerReceiver(mNotifActionHandler, new IntentFilter(NotifActionHandler.ACTION_REST), null, null);
        mContext.registerReceiver(mNotifActionHandler, new IntentFilter(NotifActionHandler.ACTION_START), null, null);
        mContext.registerReceiver(mNotifActionHandler, new IntentFilter(NotifActionHandler.ACTION_STOP), null, null);

        return fragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupToolbar();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWasPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWasPaused = false;
        mNotificationManager.cancelAll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(mNotifActionHandler);
    }

    @Override
    public boolean onBackPressed() {
        onWorkoutCompleted();
        return true;
    }

    @Override
    public void onCountdownChanged(int remaining) {
        String notifText = String.format(mRestNotifText, getCurrentSet() + 1, remaining);
        if (shouldShowNotification()) {
            showNotification(notifText);
        }
    }

    @Override
    public void onCountdownFinished() {
        notifyUserExerciseStarted();
        mClockView.setText(CLOCK_RESET);
        mDurationSec += mRestCountdownView.getElapsedSec();

        // start the workout again.
        mRestCountdownView.stop();
        mRestCountdownView.setRestDuration(getCurrentRestDuration());
        mRestCountdownView.setVisibility(View.VISIBLE);

        mCurrentSetStartTime = System.currentTimeMillis();
        mIsWorkingOut = true;
        mIsResting = false;
        mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
    }

    public WorkoutFragment setExercise(Exercise exercise, ExerciseSummary summary) {
        mExercise = exercise;
        mSummary = summary;
        return this;
    }

    public WorkoutFragment setListener(WorkoutEventListener listener) {
        mListener = listener;
        return this;
    }

    private void notifyUserExerciseStarted() {
        wakeScreen();
        playAlarm();
    }

    private void wakeScreen() {
        mWakeLock.acquire();
        mWakeLock.release();
    }

    private void playAlarm() {
        mRingtone.play();
    }

    private void setAlarmVolume() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int ringMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        int defaultVolume = (int) Math.round(ringMaxVolume * 0.4);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, defaultVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    private void updateViews() {
        mToolbar.setTitle(mExercise.mExerciseName);
        mSetView.setText(String.valueOf(mSummary.set));
        mWeightView.setText(String.valueOf(mSummary.weight));
        if (mSummary.restDurationSec != 0) {
            mRestDurationView.setText(String.valueOf(mSummary.restDurationSec));
        }
    }

    private void startWorkout() {
        mRestCountdownView.stop();
        mRestCountdownView.setRestDuration(getCurrentRestDuration());
        mRestCountdownView.setVisibility(View.VISIBLE);

        long now = System.currentTimeMillis();
        if (mSummary.startTime == 0) {
            mSummary.startTime = now;
        }

        mCurrentSetStartTime = now;
        mIsWorkingOut = true;
        mIsResting = false;
        onClockTextChanged(0, 0);
        mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        changeToStopButton();
    }

    private void setStopWorkoutState() {
        mClockView.setText(CLOCK_RESET);
        mIsWorkingOut = false;
        mIsResting = false;
        mHandler.removeCallbacks(mClockTimer);
        if (mCurrentSetStartTime != 0) {
            mDurationSec += TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - mCurrentSetStartTime);
            mCurrentSetStartTime = 0;
        }
    }

    private void stopWorkout() {
        setStopWorkoutState();
        setStopRestCountdownState();
        changeToStartButton();
    }

    private void increaseSet() {
        int set = getCurrentSet() + 1;
        mSetView.setText(Integer.toString(set));
    }

    private void setStopRestCountdownState() {
        mRestCountdownView.stop();
        mDurationSec += mRestCountdownView.getElapsedSec();
    }

    private void stopRestCountdown() {
        setStopRestCountdownState();
        changeToStartButton();
    }

    private void rest() {
        mIsWorkingOut = false;
        mIsResting = true;
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

    private int getCurrentSet() {
        return Integer.parseInt(mSetView.getText().toString());
    }

    private int getCurrentWeight() {
        return Integer.parseInt(mWeightView.getText().toString());
    }

    private int getCurrentRestDuration() {
        return Integer.parseInt(mRestDurationView.getText().toString());
    }

    private boolean shouldShowNotification() {
        return mWasPaused && (mIsWorkingOut || mIsResting);
    }

    private void showNotification(String text) {
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

            notification = mNotificationBuilder.build();
            notification.bigContentView = getBigNotificationView(text);
            notification.contentView = getNotificationView(text);
        } else {
            mNotificationBuilder
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(mExercise.mExerciseName);
            notification = mNotificationBuilder.build();
        }

        mNotificationBuilder.setContentIntent(newNotifIntent());
        mNotificationManager.notify(ACTIVE_WORKOUT_NOTIF_ID, notification);
    }

    private RemoteViews getBigNotificationView(String text) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.notification_big);
        view.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        view.setTextViewText(R.id.title, mExercise.mExerciseName);
        view.setTextViewText(R.id.text, text);

        setNotifWorkoutIntent(view);
        setNotifNextWorkoutIntent(view);

        return view;
    }

    private RemoteViews getNotificationView(String text) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.notification);
        view.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        view.setTextViewText(R.id.title, mExercise.mExerciseName);
        view.setTextViewText(R.id.text, text);

        setNotifWorkoutIntent(view);
        setNotifNextWorkoutIntent(view);

        return view;
    }

    private void setNotifNextWorkoutIntent(RemoteViews notifView) {
        if (mExerciseList.hasNext(mExercise)) {
            notifView.setImageViewResource(R.id.next_stop_button_icon, R.drawable.ic_skip_next_black_48dp);
            notifView.setTextViewText(R.id.next_stop_button_text, mResources.getString(R.string.notif_next));

            Intent intent = new Intent(NotifActionHandler.ACTION_NEXT);
            notifView.setOnClickPendingIntent(R.id.next_stop_button, PendingIntent.getBroadcast(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT));
        } else {
            notifView.setImageViewResource(R.id.next_stop_button_icon, R.drawable.ic_stop_black_48dp);
            notifView.setTextViewText(R.id.next_stop_button_text, mResources.getString(R.string.notif_stop));

            Intent intent = new Intent(NotifActionHandler.ACTION_STOP);
            PendingIntent resumeActivity = newNotifIntent();
            intent.putExtra(NotifActionHandler.RESUME_ACTIVITY_INTENT_EXTRA, resumeActivity);
            notifView.setOnClickPendingIntent(R.id.next_stop_button, PendingIntent.getBroadcast(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    private void setNotifWorkoutIntent(RemoteViews notifView) {
        if (mIsWorkingOut) {
            notifView.setImageViewResource(R.id.start_rest_button_icon, R.drawable.ic_pause_black_48dp);
            notifView.setTextViewText(R.id.start_rest_button_text, mResources.getString(R.string.notif_rest));

            Intent intent = new Intent(NotifActionHandler.ACTION_REST);
            notifView.setOnClickPendingIntent(R.id.start_rest_button, PendingIntent.getBroadcast(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT));
        } else if (mIsResting) {
            notifView.setImageViewResource(R.id.start_rest_button_icon, R.drawable.ic_play_arrow_black_48dp);
            notifView.setTextViewText(R.id.start_rest_button_text, mResources.getString(R.string.notif_start));

            Intent intent = new Intent(NotifActionHandler.ACTION_START);
            notifView.setOnClickPendingIntent(R.id.start_rest_button, PendingIntent.getBroadcast(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    private PendingIntent newNotifIntent() {
        // use activity's intent to resume the activity instead of creating new one.
        Intent intent = getActivity().getIntent();
        return PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void onWorkoutCompleted() {
        setStopWorkoutState();
        setStopRestCountdownState();

        mSummary.setWeight(Integer.parseInt(mWeightView.getText().toString()))
                .setSet(Integer.parseInt(mSetView.getText().toString()))
                .setDuration(mDurationSec + mSummary.durationSec)
                .setRep(Integer.parseInt(mRestDurationView.getText().toString()));
        if (mListener != null) {
            mListener.onExerciseCompleted(mSummary);
        }
    }

    private void onClockTextChanged(long minutes, long seconds) {
        mClockMinutes = minutes;
        mClockSeconds = seconds;
        String clockText = String.format(CLOCK_DISPLAY, minutes, seconds);
        mClockView.setText(clockText);
        if (shouldShowNotification()) {
            String notifText = String.format(mWorkoutNotifText, getCurrentSet() + 1, clockText);
            showNotification(notifText);
        }
    }

    private void setupToolbar() {
        if (mExercise == null) {
            return;
        }
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_back_white_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWorkoutCompleted();
            }
        });
    }

    public class NotifActionHandler extends BroadcastReceiver {

        private static final String ACTION_REST = "com.chinhhuynh.gymtracker.rest";
        private static final String ACTION_START = "com.chinhhuynh.gymtracker.start";
        private static final String ACTION_NEXT = "com.chinhhuynh.gymtracker.next";
        private static final String ACTION_STOP = "com.chinhhuynh.gymtracker.stop";

        private static final String RESUME_ACTIVITY_INTENT_EXTRA = "com.chinhhuynh.gymtracker.resume_activity_intent";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_REST:
                    increaseSet();
                    rest();
                    break;
                case ACTION_START:
                    stopWorkout();
                    startWorkout();
                    break;
                case ACTION_NEXT:
                    if (!mIsResting) {
                        increaseSet();
                    }

                    if (mIsWorkingOut) {
                        stopWorkout();
                    }

                    if (getCurrentSet() > 0) {
                        onWorkoutCompleted();
                    }

                    mExerciseList.startNext(mExercise);
                    updateViews();
                    startWorkout();
                    break;
                case ACTION_STOP:
                    stopWorkout();
                    onWorkoutCompleted();

                    PendingIntent resumeActivityIntent =
                            intent.getParcelableExtra(RESUME_ACTIVITY_INTENT_EXTRA);

                    try {
                        resumeActivityIntent.send();
                    } catch (PendingIntent.CanceledException ignored) {}

                    // close notification drawer.
                    context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                    break;
            }
        }
    }
}
