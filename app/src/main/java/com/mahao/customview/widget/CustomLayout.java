package com.mahao.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * View最终的大小，由父view和子view共同决定的。
 */
public class CustomLayout extends ViewGroup {

    private static final String TAG = "CustomLayout";
    private static float radius = 0f;

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * spec : parent尺寸
     * childDimension : xml的标识
     * getChildMeasureSpec(int spec, int padding, int childDimension)  = withsMeasureSpec
     * <p>
     * 举例 ： child : 20dp  ===》 可以直接通过MeasureSpec.getSize(widthMeasureSpec)获取。
     * 也可以通过getLayoutParams.height获取
     * child ： wrap_content 或者 match_parent 最终大小都是父View的大小。
     * MeasureSpec.getSize(widthMeasure)获取。
     *
     * 父布局和子布局layoutParams公用决定这个mode和size
     *
     * 比如外层布局高度： wrap  ，customLayout = match——parent
     * 这里的heightMeasureSpec为 mode = wrap ，size为parentsize ,也就是遵守
     * ViewGroup.getChildMeasureSpec() 这一套规则。
     * 结论： 传入的着2个参数不能单纯依靠xml里面来判断mode和size.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: " + sizeW + " " + sizeH);
        for (int i = 0; i < getChildCount(); i++) {
            //    measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
            measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        //    setMeasuredDimension(MeasureSpec.makeMeasureSpec(100, modeW), MeasureSpec.makeMeasureSpec(100, modeH));
    }

/*
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }*/

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * l 是相对于parent的 left;
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: " + l + " " + t + " " + r + " " + b);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != null) {
                CustomLayoutLayoutParams layoutParams = (CustomLayoutLayoutParams) childAt.getLayoutParams();
                Log.d(TAG, "onLayout: " + childAt.getMeasuredWidth() + "  child  " + childAt.getMeasuredHeight() + " margin " + getLeft());
                childAt.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + childAt.getMeasuredWidth(), layoutParams.topMargin + childAt.getMeasuredHeight());
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Log.d(TAG, "drawChild: " + child);
        int save = canvas.save();
        Rect rectF = new Rect();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
        paint.setColor(Color.GREEN);
        child.getHitRect(rectF);
        canvas.drawLine(100, 100, 400, 400, paint);
        // canvas.clipRect(200,400,500,800); //用于裁剪相同，相异部分。
        Canvas canvas1 = new Canvas();
        RectF rect = new RectF(100, 200, 400, 800);
        Path path = new Path();
        path.moveTo(100,290);
        path.lineTo(200,400);
        path.addRoundRect(rect,120,120, Path.Direction.CW);
        canvas1.clipPath(path);
        child.draw(canvas1);
        canvas.drawRoundRect(new RectF(rectF.left, rectF.top, rectF.right, rectF.bottom), rectF.width() / 2, rectF.height() / 2, paint);
        canvas.restoreToCount(save);
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof CustomLayoutLayoutParams;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new CustomLayoutLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutLayoutParams(getContext(), attrs);
    }

    class CustomLayoutLayoutParams extends MarginLayoutParams {

        public CustomLayoutLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public CustomLayoutLayoutParams(int width, int height) {
            super(width, height);
        }

        public CustomLayoutLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public CustomLayoutLayoutParams(LayoutParams source) {
            super(source);
        }
    }

}
