package com.mahao.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.mahao.customview.R;

public class MyCircleRelativelayout extends CircularRevealRelativeLayout {

    public boolean mOverFlag = true;

    public MyCircleRelativelayout(Context context) {
        super(context);
    }

    public MyCircleRelativelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOverViewFlag(boolean overViewFlag) {
        this.mOverFlag = overViewFlag;
        invalidate();
    }

    @Override
    public void actualDraw(Canvas canvas) {
        canvas.save();
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher_round);
        Rect bounds = drawable.getBounds();
        drawable.setBounds(0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        // canvas.translate(100, 100);
        drawable.draw(canvas);
        //  canvas.translate(-100, -100);
        canvas.restore();
        //去掉会绘制成一个图片。
        super.actualDraw(canvas);
    }
}
