package com.mahao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class DragLayout extends RelativeLayout {

    private static final String TAG = "DragLayout";

    public int defaultHeader = R.layout.default_header;
    public int defaultFooter = R.layout.default_footer;
    public View headerView, footView, contentView;

    ViewDragHelper mDragHelper;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == contentView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                headerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                        , MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                Log.d(TAG, "clampViewPositionVertical: " + headerView.getMeasuredHeight());
                return Math.min(headerView.getMeasuredHeight(), Math.max(0, top));
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                ViewCompat.offsetTopAndBottom(headerView, dy);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }


        });
    }


    private void initLayout(Context context) {
        headerView = getHeaderView(context);
        footView = getFootView(context);
        addView(headerView, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(footView, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Log.d(TAG, "initLayout: ");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate: ");
        contentView = getChildAt(2);
    }

    public View getHeaderView(Context context) {
        return LayoutInflater.from(context).inflate(defaultHeader, null);
    }


    public View getFootView(Context context) {
        return LayoutInflater.from(context).inflate(defaultFooter, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            Log.d(TAG, "onMeasure: " + getChildAt(i).getMeasuredWidth() + " height " + getChildAt(i).getMeasuredHeight());
        }
        int width = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        int height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        headerView.layout(0, -headerView.getMeasuredHeight(), headerView.getMeasuredWidth(), 0);
        footView.layout(0, getMeasuredHeight(), footView.getMeasuredWidth(), getMeasuredHeight() + footView.getMeasuredHeight());
        contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
