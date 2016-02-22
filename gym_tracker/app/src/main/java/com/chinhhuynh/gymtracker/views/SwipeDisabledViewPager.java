package com.chinhhuynh.gymtracker.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * View page with swipe disabled.
 */
public final class SwipeDisabledViewPager extends ViewPager {

    public SwipeDisabledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeDisabledViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return ev.getActionMasked() != MotionEvent.ACTION_MOVE;
    }
}
