package com.chinhhuynh.gymtracker.views;

import com.chinhhuynh.gymtracker.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Play button view.
 */
public final class StartButton extends View {

    private Paint mBackgroundPaint;
    private Paint mIconPaint;

    private int mIconHeight;
    private int mState;

    public static final int STATE_START = 1;
    public static final int STATE_STOP = 2;

    public StartButton(Context context) {
        super(context);
        initialize();

    }

    public StartButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public StartButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        switch (mState) {
            case STATE_START:
                drawStartButton(canvas);
                break;
            case STATE_STOP:
                drawStopButton(canvas);
                break;
        }
    }

    public void setState(int state) {
        mState = state;
        invalidate();
    }

    public int getState() {
        return mState;
    }

    private void initialize() {
        mIconHeight = getResources().getDimensionPixelOffset(R.dimen.start_icon_height);
        mState = STATE_START;

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(Color.GRAY);

        mIconPaint = new Paint();
        mIconPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mIconPaint.setStyle(Paint.Style.FILL);
        mIconPaint.setColor(Color.WHITE);
    }

    private void drawCircle(Canvas canvas) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x, y);
        canvas.drawCircle(x, y, radius, mBackgroundPaint);
    }

    private void drawStopButton(Canvas canvas) {
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float halfHeight = mIconHeight / 2;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(centerX - halfHeight, centerY - halfHeight);
        path.lineTo(centerX + halfHeight, centerY - halfHeight);
        path.lineTo(centerX + halfHeight, centerY + halfHeight);
        path.lineTo(centerX - halfHeight, centerY + halfHeight);
        path.lineTo(centerX - halfHeight, centerY - halfHeight);
        path.close();

        canvas.drawPath(path, mIconPaint);
    }

    private void drawStartButton(Canvas canvas) {
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float halfHeight = mIconHeight / 2;
        float shiftRight = mIconHeight / 4;

        float topX = centerX - halfHeight;
        float topY = centerY - halfHeight;

        float rightX = centerX + halfHeight;
        float rightY = centerY;

        float bottomX = centerX - halfHeight;
        float bottomY = centerY + halfHeight;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(topX + shiftRight, topY);
        path.lineTo(rightX + shiftRight, rightY);
        path.lineTo(bottomX + shiftRight, bottomY);
        path.lineTo(topX + shiftRight, topY);
        path.close();

        canvas.drawPath(path, mIconPaint);
    }
}
