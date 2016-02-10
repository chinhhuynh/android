package com.chinhhuynh.gymtracker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Play button view.
 */
public class StartButton extends View {

    private Paint mGreyPaint;
    private Paint mWhitePaint;

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
        drawPlayButton(canvas);
    }

    private void initialize() {
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

    private void drawPlayButton(Canvas canvas) {
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float height = getWidth() / 5;
        float halfHeight = height / 2;
        float shiftRight = height / 4;

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

        canvas.drawPath(path, mWhitePaint);
    }
}
