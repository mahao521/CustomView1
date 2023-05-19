package com.mahao.customview.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mahao.customview.R;
import com.wang.avi.AVLoadingIndicatorView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragLayout extends ViewGroup {

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
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: " + sizeW +"  ------- " + sizeH);
        int finalSizeH = 0;
        int finalSizeW = 0;
        if(modeH == MeasureSpec.EXACTLY){
            finalSizeH = sizeH;
        }else if(modeH == MeasureSpec.AT_MOST){
            finalSizeH = contentView.getMeasuredHeight();
        }else {
            finalSizeH = 0;
        }
        if(modeW == MeasureSpec.EXACTLY){
            finalSizeW = sizeW;
        }else if(modeW == MeasureSpec.AT_MOST){
            finalSizeW = contentView.getMeasuredHeight();
        }else {
            finalSizeW = 0;
        }
        Log.d(TAG, "onMeasure: " + finalSizeW +" " + finalSizeH);
        int width = MeasureSpec.makeMeasureSpec(finalSizeW, widthMeasureSpec);
        int height = MeasureSpec.makeMeasureSpec(finalSizeH, heightMeasureSpec);
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
            footView.layout(0, footView.getTop(), footView.getMeasuredWidth(), getMeasuredHeight() + footView.getBottom());
            headerView.layout(0, headerView.getTop(), headerView.getMeasuredWidth(), headerView.getBottom());
            contentView.layout(0, contentView.getTop(), contentView.getMeasuredWidth(), contentView.getBottom());
        }
    }

    float initY = 0f;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean interceptFlag = mDragHelper.shouldInterceptTouchEvent(ev);
        boolean canScrollUp = contentView.canScrollVertically(-1);
        boolean canScrollDown = contentView.canScrollVertically(1);
        Log.d(TAG, "onInterceptTouchEvent: down " + canScrollDown + " up = " + canScrollUp);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currY = ev.getY();
                //footer的多点触摸时候，强制拦截事件给viewDraghelper处理
                if (currY - initY < 0 && !canScrollDown && Math.abs(currY - initY) > mDragHelper.getTouchSlop() && !interceptFlag) {
                    Log.d(TAG, "onInterceptTouchEvent: --------- " + interceptFlag);
                    return true;
                } /*else if (currY - initY > 0 && canScrollUp && Math.abs(currY - initY) > mDragHelper.getTouchSlop()) { //footer
                    Log.d(TAG, "onInterceptTouchEvent: ++++++++" + interceptFlag);
                    return interceptFlag;
                } else if (canScrollUp && Math.abs(currY - initY) > mDragHelper.getTouchSlop()) {
                    return false;
                }*/
                break;
        }
        Log.d(TAG, "onInterceptTouchEvent: " + interceptFlag);
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
            boolean up = child.canScrollVertically(-1);
            boolean down = child.canScrollVertically(1);
            Log.d(TAG, "clampViewPositionVertical: " + up + " " + down + "   " + dy + " " + top);
            if (up == false && down == true ||( !up && !down)) {
                int finalTop = top;
                if (dy > 0) {
                    finalTop = (int) (finalTop - dy * dampingRatio(top, headerView.getMeasuredHeight()));
                    //  Log.d(TAG, "clampViewPositionVertical: " + finalTop + " " + top + " " + (dampingRatio(top)));
                }
                Log.d(TAG, "clampViewPositionVertical:  0    ");
                return Math.min(headerView.getMeasuredHeight(), Math.max(0, finalTop));
            } else if (up && !down) {
                int finalTop = top;
                if (dy < 0) {
                    //   finalTop = (int) (finalTop + dy * dampingRatio(top,footView.getMeasuredHeight()));
                    //   Log.d(TAG, "clampViewPositionVertical: " + finalTop + " " + top + " " + (dampingRatio(top)));
                }
                int max = Math.max(-footView.getMeasuredHeight(), Math.min(0, finalTop));
                Log.d(TAG, "clampViewPositionVertical:  1  " + max + " " + top);
                return max;
            }else {
                Log.d(TAG, "clampViewPositionVertical: 3  ");
                return child.getTop();
            }
        }

        public float dampingRatio(int top, int totalHeight) {
            float diffRatio = top * 1.0f / totalHeight;
            return diffRatio;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            Log.d(TAG, "onViewCaptured: ");
            super.onViewCaptured(capturedChild, activePointerId);
            removeCallbacks(mHeaderRunnable);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            ViewCompat.offsetTopAndBottom(headerView, dy);
            ViewCompat.offsetTopAndBottom(footView, dy);
            Log.d(TAG, "onViewPositionChanged: " + changedView.getTop() + " " + changedView.getBottom());
            Log.d(TAG, "onViewPositionChanged: " + top + "  1 " + dy);
            if (top == headerVisiable.getMeasuredHeight() && dy < 0) {
                Log.d(TAG, "onViewPositionChanged: " + top + " " + dy);
                skipLayout = true;
                headerVisiableLoadView.show();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.d(TAG, "onViewReleased: " + yvel);
            if (contentView.getTop() >= headerVisiable.getMeasuredHeight()) {
                Log.d(TAG, "onViewReleased: " + headerVisiable.getMeasuredHeight());
                mDragHelper.smoothSlideViewTo(releasedChild, 0, headerVisiable.getMeasuredHeight());
            } else if (contentView.getTop() > 0 && contentView.getTop() < headerVisiable.getMeasuredHeight()) {
                mDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
            } else if (contentView.getTop() < 0 && contentView.getTop() < -footView.getMeasuredHeight() / 2) {
                mDragHelper.smoothSlideViewTo(releasedChild, 0, -footView.getMeasuredHeight());
            } else if (contentView.getTop() > -footView.getMeasuredHeight() / 2) {
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
            if (state == ViewDragHelper.STATE_IDLE && contentView.getTop() == headerVisiable.getMeasuredHeight()) {
                skipLayout = true;
                headerVisiableLoadView.setVisibility(View.VISIBLE);
                headerVisiableLoadView.show();
                postDelayed(mHeaderRunnable, 2000);
            } else if (state == ViewDragHelper.STATE_IDLE && contentView.getTop() == -footView.getMeasuredHeight()) {
                postDelayed(mHeaderRunnable, 2000);
            }
        }
    }

    private final Runnable mHeaderRunnable = new Runnable() {
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
