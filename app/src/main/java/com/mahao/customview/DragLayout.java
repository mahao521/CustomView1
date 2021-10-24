package com.mahao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wang.avi.AVLoadingIndicatorView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragLayout extends RelativeLayout {

    private static final String TAG = "DragLayout";
    public int defaultHeader = R.layout.default_header;
    public int defaultFooter = R.layout.default_footer;
    public View headerView, footView, contentView, headerVisiable;
    public AVLoadingIndicatorView headerVisiableLoadView;
    ViewDragHelper mDragHelper;
    boolean skipLayout = false;
    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragCallBack());
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
        headerVisiable = ((LinearLayout) headerView).getChildAt(1);
        headerVisiableLoadView = (AVLoadingIndicatorView) (((RelativeLayout) headerVisiable).getChildAt(1));
        headerVisiableLoadView.show();
    }

    public View getHeaderView(Context context) {
        return LayoutInflater.from(context).inflate(defaultHeader, null);
    }


    public View getFootView(Context context) {
        return LayoutInflater.from(context).inflate(defaultFooter, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
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
        Log.d(TAG, "onLayout:" + skipLayout);
        if (!skipLayout) {
            headerView.layout(0, -headerView.getMeasuredHeight(), headerView.getMeasuredWidth(), 0);
            footView.layout(0, getMeasuredHeight(), footView.getMeasuredWidth(), getMeasuredHeight() + footView.getMeasuredHeight());
            contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        } else {
            skipLayout = false;
            Log.d(TAG, "onLayout: " + headerView.getTop() + " " + contentView.getTop());
        //    footView.layout(0, getMeasuredHeight(), footView.getMeasuredWidth(), getMeasuredHeight() + footView.getMeasuredHeight());
            headerView.layout(0, headerView.getTop(), headerView.getMeasuredWidth(), headerView.getBottom());
            contentView.layout(0, contentView.getTop(), contentView.getMeasuredWidth(), contentView.getBottom());
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean interceptFlag = mDragHelper.shouldInterceptTouchEvent(ev);
        boolean canScrollUp = false, canScroolDown;
        if (contentView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) contentView;
            canScrollUp = recyclerView.canScrollVertically(-1);
            boolean b = recyclerView.canScrollVertically(1);
            Log.d(TAG, "onInterceptTouchEvent: down " + b);
             Log.d(TAG, "onInterceptTouchEvent:  up " + canScrollUp);
            if (canScrollUp) {
                boolean interceptTouchEvent = super.onInterceptTouchEvent(ev);
                Log.d(TAG, "onInterceptTouchEvent: =========== " + interceptTouchEvent);
                if(b == false){
            //        return interceptFlag;
                }
                return false;
            }


        }
        return interceptFlag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    class DragCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            skipLayout = true;
            headerVisiableLoadView.setVisibility(View.GONE);
            headerVisiableLoadView.hide();
            return child == contentView;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return headerView.getMeasuredHeight();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            headerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    , MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int finalTop = top;
            if (dy > 0) {
                finalTop = (int) (finalTop - dy * dampingRatio(top));
                Log.d(TAG, "clampViewPositionVertical: " + finalTop + " " + top + " " + (dampingRatio(top)));
            }
            return Math.min(headerView.getMeasuredHeight(), Math.max(0, finalTop));
        }

        public float dampingRatio(int top) {
            float diffRatio = top * 1.0f / headerView.getMeasuredHeight();
            return diffRatio;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            Log.d(TAG, "onViewCaptured: ");
            super.onViewCaptured(capturedChild, activePointerId);
            removeCallbacks(mRunnable);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            ViewCompat.offsetTopAndBottom(headerView, dy);
          //  ViewCompat.offsetTopAndBottom(footView,-dy);
            Log.d(TAG, "onViewPositionChanged: " + changedView.getTop() +" " + changedView.getBottom());
            float ratio = dampingRatio(changedView.getTop());
            // headerVisiable.setScaleX(1 + ratio * 0.5f);
            //  headerVisiable.setScaleY(1 + ratio * 2);
            Log.d(TAG, "onViewPositionChanged: " + top + "  1 " + dy);
            if (top == headerVisiable.getMeasuredHeight() && dy < 0) {
                Log.d(TAG, "onViewPositionChanged: " + top + " " + dy);
                //  headerVisiable.setScaleX(1);
                //   headerVisiable.setScaleY(1);
                skipLayout = true;
                headerVisiableLoadView.show();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (contentView.getTop() >= headerVisiable.getMeasuredHeight()) {
                Log.d(TAG, "onViewReleased: " + headerVisiable.getMeasuredHeight());
                mDragHelper.smoothSlideViewTo(releasedChild, 0, headerVisiable.getMeasuredHeight());
            } else if (contentView.getTop() < headerVisiable.getMeasuredHeight()) {
                mDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
            }
            invalidate();
        }


        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            Log.d(TAG, "onViewDragStateChanged: " + contentView.getTop() + "..." + headerVisiable.getMeasuredHeight());
            Log.d(TAG, "onViewDragStateChanged: " + state);
            if (state == ViewDragHelper.STATE_IDLE && contentView.getTop() == headerVisiable.getMeasuredHeight()) {
                skipLayout = true;
                headerVisiableLoadView.setVisibility(View.VISIBLE);
                headerVisiableLoadView.show();
                postDelayed(mRunnable, 3000);
            }
        }
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mDragHelper.smoothSlideViewTo(contentView, 0, 0);
            invalidate();
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            Log.d(TAG, "computeScroll: " + true);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}
