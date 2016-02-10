package com.chinhhuynh.gymtracker.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import com.chinhhuynh.gymtracker.R;

/**
 * Rest countdown view.
 */
public final class RestCountdown extends View {

    private static final float SQRT_2 = (float) Math.sqrt(2);
    private static final long ONE_TENTH_SECOND = DateUtils.SECOND_IN_MILLIS / 10;

    private Paint mBackgroundPaint;
    private Paint mTextPaint;

    private int mDuration;
    private int mRemaining;
    private int mTextPadding;

    private long mStartTime;
    private Handler mHandler;
    private Runnable mClockTimer = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(now - mStartTime);
            mRemaining = mDuration - seconds;
            invalidate();
            if (mRemaining > 0) {
                mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
            }
        }
    };
    private GestureDetector mGestureDetector =
            new GestureDetector(getContext(), new SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mRemaining = mDuration;
            mStartTime = System.currentTimeMillis();
            mHandler.postDelayed(mClockTimer, ONE_TENTH_SECOND);
            return true;
        }
    });


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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    public void setRestDuration(int duration) {
        mDuration = duration;
        mRemaining = duration;
    }

    private void initialize() {
        int textSize = getContext().getResources().getDimensionPixelSize(R.dimen.countdown_text_size);
        mTextPadding = getContext().getResources().getDimensionPixelSize(R.dimen.countdown_text_padding);
        mHandler = new Handler(Looper.getMainLooper());
        Resources resources = getResources();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(resources.getColor(R.color.md_green_500));

        mTextPaint = new Paint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(textSize);
    }

    private void drawCircle(Canvas canvas) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x, y);
        canvas.drawCircle(x, y, radius, mBackgroundPaint);
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
}
