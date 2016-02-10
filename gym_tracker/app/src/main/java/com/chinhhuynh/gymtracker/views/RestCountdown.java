package com.chinhhuynh.gymtracker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.chinhhuynh.gymtracker.R;

/**
 * Rest countdown view.
 */
public class RestCountdown extends View {

    private static final float SQRT_2 = (float) Math.sqrt(2);

    private Rect mMeasureBound;
    private Paint mGreyPaint;
    private Paint mWhitePaint;

    private int mDuration;
    private int mTextSize;
    private int mTextPadding;

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
        drawNumber(canvas, mDuration);
    }

    public void setRestDuration(int duration) {
        mDuration = duration;
    }

    private void initialize() {
        mTextSize = getContext().getResources().getDimensionPixelSize(R.dimen.countdown_text_size);
        mTextPadding = getContext().getResources().getDimensionPixelSize(R.dimen.countdown_text_padding);
        mMeasureBound = new Rect();

        mGreyPaint = new Paint();
        mGreyPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mGreyPaint.setStyle(Paint.Style.FILL);
        mGreyPaint.setColor(Color.GRAY);

        mWhitePaint = new Paint();
        mWhitePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setColor(Color.WHITE);
    }

    private void drawCircle(Canvas canvas) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x, y);
        canvas.drawCircle(x, y, radius, mGreyPaint);
    }

    private void drawNumber(Canvas canvas, int number) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x, y);
        float textWidth = SQRT_2 * radius - 2 * mTextPadding;
        float halfTextWidth = textWidth / 2;
        String text = Integer.toString(number);
        setTextSizeForWidth(mWhitePaint, text, textWidth);

        RectF textBound = new RectF(x - halfTextWidth, y - halfTextWidth, x + halfTextWidth, y - halfTextWidth);
        textBound.right = mWhitePaint.measureText(text, 0, text.length());
        textBound.bottom = mWhitePaint.descent() - mWhitePaint.ascent();
        textBound.left += (textWidth - textBound.right) / 2;
        textBound.top += (textWidth - textBound.bottom) / 2;

        canvas.drawText(text, textBound.left, textBound.top - mWhitePaint.ascent(), mWhitePaint);
    }

    private void setTextSizeForWidth(Paint paint, String text, float desiredWidth) {
        paint.setTextSize(mTextSize);
        paint.getTextBounds(text, 0, text.length(), mMeasureBound);

        float desiredTextSize = mTextSize * desiredWidth / mMeasureBound.width();
        paint.setTextSize(desiredTextSize);
    }
}
