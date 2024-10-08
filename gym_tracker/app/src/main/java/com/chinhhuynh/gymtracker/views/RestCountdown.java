package com.chinhhuynh.gymtracker.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.TimeUnit;

import com.chinhhuynh.gymtracker.R;

/**
 * Rest countdown view.
 */
public final class RestCountdown extends View {

    public interface CountdownListener {
        void onCountdownChanged(int remaining);
        void onCountdownFinished();
    }

    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final long ONE_TENTH_SECOND = DateUtils.SECOND_IN_MILLIS / 10;
    private static final float STROKE_WIDTH = 27f;

    private Paint mBackgroundPaint;
    private Paint mTextPaint;

    private int mDuration;
    private int mRemaining;
    private int mTextPadding;
    private boolean mIsActive;

    private long mStartTime;
    private Handler mHandler;
    private CountdownListener mListener;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(now - mStartTime);
            if (mDuration - seconds != mRemaining) {
                mRemaining = mDuration - seconds;
                invalidate();
                notifyCountdownChanged(mRemaining);
            }
            if (mRemaining > 0) {
                mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
            } else {
                mIsActive = false;
                notifyCountdownFinished();
            }
        }
    };

    public RestCountdown(Context context) {
        super(context);
        initialize();

    }

    public RestCountdown(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public RestCountdown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawNumber(canvas, mRemaining);
    }

    public void setRestDuration(int duration) {
        if (!mIsActive) {
            mDuration = duration;
            mRemaining = duration;
            invalidate();
        }
    }

    public void setListener(CountdownListener listener) {
        mListener = listener;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public int getElapsedSec() {
        return mDuration - mRemaining;
    }

    public void countdown() {
        mRemaining = mDuration;
        mStartTime = System.currentTimeMillis();
        mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
        mIsActive = true;

        notifyCountdownChanged(mRemaining);
    }

    public void stop() {
        mHandler.removeCallbacks(mClockTimer);
        mIsActive = false;
    }

    private void initialize() {
        int textSize = getContext().getResources().getDimensionPixelSize(R.dimen.countdown_text_size);
        mTextPadding = getContext().getResources().getDimensionPixelSize(R.dimen.countdown_text_padding);
        mHandler = new Handler(Looper.getMainLooper());
        Resources resources = getResources();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(STROKE_WIDTH);
        mBackgroundPaint.setColor(resources.getColor(R.color.md_black_1000));

        mTextPaint = new Paint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(resources.getColor(R.color.md_black_1000));
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    private void drawCircle(Canvas canvas) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x, y);
        canvas.drawCircle(x, y, radius - STROKE_WIDTH, mBackgroundPaint);
    }

    private void drawNumber(Canvas canvas, int number) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x, y);
        float textWidth = SQRT_2 * radius - 2 * mTextPadding;
        float halfTextWidth = textWidth / 2;
        String text = Integer.toString(number);

        RectF textBound = new RectF(x - halfTextWidth, y - halfTextWidth, x + halfTextWidth, y - halfTextWidth);
        textBound.right = mTextPaint.measureText(text, 0, text.length());
        textBound.bottom = mTextPaint.descent() - mTextPaint.ascent();
        textBound.left += (textWidth - textBound.right) / 2;
        textBound.top += (textWidth - textBound.bottom) / 2;

        canvas.drawText(text, textBound.left, textBound.top - mTextPaint.ascent(), mTextPaint);
    }

    private void notifyCountdownFinished() {
        if (mListener != null) {
            mListener.onCountdownFinished();
        }
    }

    private void notifyCountdownChanged(int remaining) {
        if (mListener != null) {
            mListener.onCountdownChanged(remaining);
        }
    }
}

