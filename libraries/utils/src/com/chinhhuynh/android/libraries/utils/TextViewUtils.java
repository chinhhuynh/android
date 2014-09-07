package com.chinhhuynh.android.libraries.utils;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Helper methods for Text View.
 */
public class TextViewUtils {

  /**
   * Calculate maximum text size that fits the text into specified maximum width and height.
   * @param text Text string.
   * @param maxWidth Maximum text width in pixel.
   * @param maxHeight Maximum text height in pixel.
   * @param textBounds Text bounds rectangle.
   * @return Maximum text size.
   */
  public static int calculateTextSize(String text, int maxWidth, int maxHeight, Rect textBounds) {
    Paint paint = new Paint();
    int size, minSize, maxSize;

    maxSize = 1;
    do {
      minSize = maxSize;
      maxSize *= 2;
      paint.setTextSize(maxSize);
      paint.getTextBounds(text, 0, text.length(), textBounds);
    } while (textBounds.width() < maxWidth && textBounds.height() < maxHeight);

    do {
      size = (minSize + maxSize) / 2;
      paint.setTextSize(size);
      paint.getTextBounds(text, 0, text.length(), textBounds);
      if (textBounds.width() < maxWidth && textBounds.height() < maxHeight) {
        minSize = size + 1;
      } else {
        maxSize = size - 1;
      }
    } while (minSize < maxSize);
    return size;
  }
}
