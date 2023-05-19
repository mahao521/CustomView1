package com.mahao.customview.fragment.viewpager2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

public class MyInterceptLayout extends RelativeLayout {

    private static final String TAG = "MyInterceptLayout";
    int touchSlop = 0;
    int orientation = RecyclerView.HORIZONTAL;

    public MyInterceptLayout(Context context) {
        this(context, null);
    }

    public MyInterceptLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View childAt = getChildAt(0);
        if (childAt instanceof ViewPager2) {
            ViewPager2 pager2 = (ViewPager2) childAt;
            orientation = pager2.getOrientation();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, "onTouchEvent: 我是拦截的数据");
        return super.onTouchEvent(event);
    }

    float initX, initY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initX = ev.getX();
                initY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = ev.getX();
                float currentY = ev.getY();
                if(isCanIntercept(currentX,currentY)){
                    return true;
                }
                initX = currentX;
                initY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isCanIntercept(float x, float y) {
        float diffX = initX - x;
        float diffY = initY - y;
        if (orientation == RecyclerView.HORIZONTAL) {
            if (Math.abs(diffY) > Math.abs(diffX) && Math.abs(diffY) > touchSlop) {
                return true;
            }
        } else if (orientation == RecyclerView.VERTICAL) {
            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > touchSlop) {
                return true;
            }
        }
        return false;
    }

}
