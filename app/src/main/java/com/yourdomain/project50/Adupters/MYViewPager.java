package com.yourdomain.project50.Adupters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by apple on 11/15/18.
 */

public class MYViewPager extends ViewPager {

    private boolean enabled;

    public MYViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() == MotionEvent.EDGE_LEFT) {
                return true;   //disable swipe
            }
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
