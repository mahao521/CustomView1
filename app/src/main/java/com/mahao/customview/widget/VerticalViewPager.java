package com.mahao.customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mahao.customview.recycler.util.DisplayUtil;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class VerticalViewPager extends ViewPager {
    private boolean mCanSlide = true;
    private boolean mIsSmoothScroll = true;
    private boolean mHeightCanChange = false;
    private static final String TAG = "VerticalViewPager";
    private ViewGroup child;

    /**
     * 垂直滑动的ViewPager 构造函数
     *
     * @param context 上下文
     */
    public VerticalViewPager(Context context) {
        this(context, null);
    }

    /**
     * 垂直滑动的ViewPager 构造函数
     *
     * @param context 上下文
     * @param attrs   自定义属性
     */
    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置viewpage的切换动画,这里设置才能真正实现垂直滑动的viewpager
        setPageTransformer(true, new DefaultTransformer());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    //分别记录上次滑动的坐标
    private float mDownPosX = 0;
    private float mDownPosY = 0;

    /**
     * 拦截touch事件
     *
     * @param ev 获取事件类型的封装类MotionEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        child = (ViewGroup) getChildAt(0);
        Log.d(TAG, "onFinishInflate: " + child.toString());
       /* if (mCanSlide) {
            boolean intercept = super.onInterceptTouchEvent(swapEvent(ev));
            return intercept;
        } else {
            Log.d(TAG, "onInterceptTouchEvent: ");
            return false;
        }*/
        Log.d(TAG, "onInterceptTouchEvent: " + child.onInterceptTouchEvent(ev));
        if (child.onInterceptTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 触摸点击触发该方法
     *
     * @param ev 获取事件类型的封装类MotionEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCanSlide) {
            return super.onTouchEvent(swapEvent(ev));
        } else {
            return false;
        }
    }


    /**
     * 交换x轴和y轴的移动距离
     *
     * @param event 获取事件类型的封装类MotionEvent
     */
    private MotionEvent swapEvent(MotionEvent event) {
        //获取宽高
        float width = getWidth();
        float height = getHeight();
        //将Y轴的移动距离转变成X轴的移动距离
        float swappedX = (event.getY() / height) * width;
        //将X轴的移动距离转变成Y轴的移动距离
        float swappedY = (event.getX() / width) * height;
        //重设event的位置
        event.setLocation(swappedX, swappedY);
        return event;
    }

    public void setCanSlide(boolean canSlide) {
        mCanSlide = canSlide;
    }

    public void setHeightCanChange(boolean heightCanChange) {
        mHeightCanChange = heightCanChange;
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
//        LogUtil.d("bigcatduan1111", "set mIsSmoothScroll: " + smoothScroll + " item: " + item);
        mIsSmoothScroll = smoothScroll;
        if (mHeightCanChange) {
            //如果竖向viewpager的高度是可变化的，目前只能平滑的划过，不然在为true的时候，高度变化，会影响显示
            mIsSmoothScroll = true;
        }
        super.setCurrentItem(item, mIsSmoothScroll);
    }

    /**
     * 自定义 ViewPager 切换动画
     * 如果不设置切换动画，还会是水平方向的动画
     */
    public class DefaultTransformer implements PageTransformer {
        public static final String TAG = "simple";

        @Override
        public void transformPage(@NonNull View view, float position) {
//            LogUtil.d("bigcatduan111", "transformPage: " + position);
//            float alpha = 0;
//            if (0 <= position && position <= 1) {
//                alpha = 1 - position;
//            } else if (-1 < position && position < 0) {
//                alpha = position + 1;
//            }
//            view.setAlpha(alpha);
            float transX = view.getWidth() * -position;
            view.setTranslationX(transX);
            float transY;
//            LogUtil.d("bigcatduan1111", "mIsSmoothScroll: " + mIsSmoothScroll);
            if (mIsSmoothScroll) {
                transY = position * (view.getHeight() + DisplayUtil.dp2Px(getContext(), 64));
            } else {
                transY = position * view.getHeight();
            }
            view.setTranslationY(transY);
        }
    }
}
