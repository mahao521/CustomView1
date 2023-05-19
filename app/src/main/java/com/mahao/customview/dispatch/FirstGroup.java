package com.mahao.customview.dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mahao.customview.R;

public class FirstGroup extends ViewGroup {

    private static final String TAG = "FirstGroup-----";

    public FirstGroup(Context context) {
        this(context, null);
    }

    public FirstGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FirstGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(getResources().getColor(R.color.white_2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: " + getMeasuredWidth() + "  " + getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(getPaddingLeft(), top, getPaddingLeft() + child.getMeasuredWidth(), child.getMeasuredHeight() + top);
            top = top + child.getMeasuredHeight();
        }
        Log.d(TAG, "onLayout: " + getWidth() + " " + getHeight());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent: ");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: ");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: "+ System.currentTimeMillis());
        return super.onTouchEvent(event);
    }


}
