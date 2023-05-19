package com.mahao.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 验证父wrap_content   子match_parent  大小是父亲决定的。
 *
 * 比如linearLayout中处理了最终的高度。使用方法如下
 * resolveSizeAndState(int size, int measureSpec, int childMeasuredState)
 *
 * 这个方法和MeasureSpec.makeMeasureSpec(resultSize, resultMode)的结果相同
 */
public class MyLinearLayout extends LinearLayout {

    private static final String TAG = "myLinearLayout";

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        Log.d(TAG, "onMeasure:  one " + sizeW + " " + sizeH);
        Log.d(TAG, "onMeasure: " + (modeW == MeasureSpec.EXACTLY) + " ... " + (modeH == MeasureSpec.EXACTLY));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int afterSizeW = MeasureSpec.getSize(widthMeasureSpec);
        int afterSizeH = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: two " + afterSizeW + " " + afterSizeH);
        Log.d(TAG, "onMeasure: three " + getMeasuredWidth() + " " + getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout: " + getWidth() + " " + getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
