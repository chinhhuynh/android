package com.chinhhuynh.android.libraries.wordgames.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import static com.chinhhuynh.android.libraries.utils.TextViewUtils.calculateTextSize;

/**
 * A tile which displays text in the center of the tile.
 */
public class TextTile extends View {
  private static final String TAG = "TextTile";

  private int backgroundColor;
  private int selectedBackgroundColor;
  private int textColor;
  private int selectedTextColor;
  private int padding;
  private int textSize;
  private String text;
  private Rect textBounds;
  private Paint textPaint;
  private float textStartX;
  private float textStartY;

  public TextTile(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    readAttributes(context, attributeSet);
    textBounds = new Rect();
    textPaint = new Paint();
    textPaint.setColor(textColor);
    textStartX = 0f;
    textStartY = 0f;
    setBackgroundColor(backgroundColor);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawText(text, textStartX, textStartY, textPaint);
  }

  @Override
  protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    textSize = calculateTextSize(text, width - padding * 2, height - padding * 2, textBounds);
    textPaint.setTextSize(textSize);
    textStartX = (width - textBounds.width()) / 2 - textBounds.left;
    textStartY = height - ((height - textBounds.height()) / 2) - textBounds.bottom;
    invalidate();
  }

  public void onSelected() {
    setBackgroundColor(selectedBackgroundColor);
    textPaint.setColor(selectedTextColor);
    invalidate();
  }

  public void onUnselected() {
    setBackgroundColor(backgroundColor);
    textPaint.setColor(textColor);
    invalidate();
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
    invalidate();
    requestLayout();
  }

  public int getPadding() {
    return padding;
  }

  public void setPadding(int padding) {
    this.padding = padding;
    invalidate();
    requestLayout();
  }

  private void readAttributes(Context context, AttributeSet attributeSet) {
    TypedArray a = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TextTile, 0, 0);
    try {
      backgroundColor = a.getColor(R.styleable.TextTile_backgroundColor, getResources().getColor(R.color.Orange));
      selectedBackgroundColor = a.getColor(R.styleable.TextTile_selectedBackgroundColor, Color.WHITE);
      textColor = a.getColor(R.styleable.TextTile_textColor, Color.WHITE);
      selectedTextColor = a.getColor(R.styleable.TextTile_selectedTextColor, Color.BLACK);
      text = a.getString(R.styleable.TextTile_text);
      padding = a.getDimensionPixelSize(R.styleable.TextTile_padding, 0);
      textSize = a.getInt(R.styleable.TextTile_textSize, 0);
    } finally {
      a.recycle();
    }
  }
}
