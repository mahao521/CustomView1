package com.mahao.customview.recycler.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahao.customview.R;

import androidx.core.view.ViewCompat;

import static androidx.core.view.ViewCompat.TYPE_NON_TOUCH;
import static androidx.core.view.ViewCompat.TYPE_TOUCH;
import static androidx.customview.widget.ViewDragHelper.INVALID_POINTER;

/**
 * onMeasure(int widthMeasureSpec, int heightMeasureSpec)
 * <p>
 * widthMeasureSpec ： 父 和 delteLayout xml 共同决定 ，然后传递下来的mode和size
 * <p>
 * 顶层 ：viewRootImpl 是通过子xml，传递mode和大小的。。和View里面不一样。
 * <p>
 * private static int getRootMeasureSpec(int windowSize, int rootDimension) {
 * int measureSpec;
 * switch (rootDimension) {
 * <p>
 * case ViewGroup.LayoutParams.MATCH_PARENT:
 * // Window can't resize. Force root view to be windowSize.
 * measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.EXACTLY);
 * break;
 * case ViewGroup.LayoutParams.WRAP_CONTENT:
 * // Window can resize. Set max size for root view.
 * measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.AT_MOST);
 * break;
 * default:
 * // Window wants to be an exact size. Force root view to be that size.
 * measureSpec = MeasureSpec.makeMeasureSpec(rootDimension, MeasureSpec.EXACTLY);
 * break;
 * }
 * return measureSpec;
 * }
 */
public class TestRecyclerView extends ViewGroup {

    private static final String TAG = "DelteLayout";
    private static int touchSlop;
    private VelocityTracker mVelocityTracker;
    private int mScrollPointerId = INVALID_POINTER;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mLastTouchX;
    private int mLastTouchY;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;
    private final int[] mNestedOffsets = new int[2];
    private boolean isIntercepet = false;

    public TestRecyclerView(Context context) {
        this(context, null);
    }

    public TestRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        touchSlop = viewConfiguration.getScaledTouchSlop();
        mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        initView(context);
        setWillNotDraw(false);  // 绘制自身，ondraw()
    }

    int heightCount = 3;

    private void initView(Context context) {
        if (heightCount == 3) {
            heightCount = 10;
        } else {
            heightCount = 3;
        }
        for (int i = 0; i < heightCount; i++) {
            TextView textView = new TextView(context);
            textView.setText("position = " + 0);
            if (textView.getParent() != null) {
                removeView(textView);
            }
            textView.setText("position = " + i);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18f);
            if (i % 2 == 0) {
                textView.setBackgroundColor(getResources().getColor(R.color.purple_200));
            } else {
                textView.setBackgroundColor(getResources().getColor(R.color.white_2));
            }
            textView.setTextColor(getResources().getColor(R.color.white_1));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAllViews();
                    initView(getContext());
                    requestLayout();
                }
            });
            addView(textView, new ViewGroup.MarginLayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dp2Px(context, 100)));
        }
        Log.d(TAG, "initView:  执行了 ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        if (modeW == MeasureSpec.EXACTLY) {
            Log.d(TAG, "onMeasure: mode w  exactly");
        } else if (modeW == MeasureSpec.AT_MOST) {
            Log.d(TAG, "onMeasure: mode w  At most");
        }
        if (modeH == MeasureSpec.EXACTLY) {
            Log.d(TAG, "onMeasure: mode h  exactly");
        } else if (modeH == MeasureSpec.AT_MOST) {
            Log.d(TAG, "onMeasure: mode h  At most");
        }
        //这一行的目的就是仿照recyclerView
        defaultOnMeasure(widthMeasureSpec, heightMeasureSpec);

        int caculateWidth = 0;
        int caculateHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            measureChildWithMargins(childAt, widthMeasureSpec, 0, heightMeasureSpec, 0);
            caculateWidth = Math.max(caculateWidth, childAt.getMeasuredWidth());
            caculateHeight += childAt.getMeasuredHeight();
        }
        if (modeW == MeasureSpec.EXACTLY && modeH == MeasureSpec.EXACTLY) {
            return;
        }
        int finalWidth = chooseSize(widthMeasureSpec, caculateWidth, getSuggestedMinimumWidth());
        int finalHeight = chooseSize(heightMeasureSpec, caculateHeight, getSuggestedMinimumHeight());
        setMeasuredDimension(finalWidth, finalHeight);
        Log.d(TAG, "onMeasure: " + finalWidth + " " + finalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int itemTop = getPaddingTop();
        int itemLeft = getPaddingLeft();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.layout(itemLeft, itemTop, itemLeft + childAt.getMeasuredWidth(), itemTop + childAt.getMeasuredHeight());
            itemTop += childAt.getHeight();
            Log.d(TAG, "onLayout:  " + childAt.getLeft() + " " + childAt.getTop() + " " + childAt.getRight() + " " + childAt.getBottom());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(e);
        final int action = e.getActionMasked();
        final int actionIndex = e.getActionIndex();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5f);
          /*      if (mScrollState == SCROLL_STATE_SETTLING) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setScrollState(SCROLL_STATE_DRAGGING);
                    stopNestedScroll(TYPE_NON_TOUCH);
                }*/
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mScrollPointerId = e.getPointerId(actionIndex);
                mInitialTouchX = mLastTouchX = (int) (e.getX(actionIndex) + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY(actionIndex) + 0.5f);
                break;

            case MotionEvent.ACTION_MOVE:
                final int index = e.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    Log.e(TAG, "Error processing scroll; pointer index for id "
                            + mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }

                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);

                if (!isIntercepet) {
                    final int dx = x - mInitialTouchX;
                    final int dy = y - mInitialTouchY;
                    boolean startScroll = false;
                    if (Math.abs(dx) > touchSlop) {
                        mLastTouchX = x;
                        startScroll = true;
                    }
                    if (Math.abs(dy) > touchSlop) {
                        mLastTouchY = y;
                        startScroll = true;
                    }
                    if (startScroll) {
                        isIntercepet = true;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(e);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.clear();
                break;
            case MotionEvent.ACTION_CANCEL:
                cancelScroll();
        }
        return isIntercepet;
    }

    private void cancelScroll() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == mScrollPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mScrollPointerId = e.getPointerId(newIndex);
            mInitialTouchX = mLastTouchX = (int) (e.getX(newIndex) + 0.5f);
            mInitialTouchY = mLastTouchY = (int) (e.getY(newIndex) + 0.5f);
        }
    }


    boolean isDrag = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

     /*   final boolean canScrollHorizontally = mLayout.canScrollHorizontally();
        final boolean canScrollVertically = mLayout.canScrollVertically();*/

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;

        final int action = e.getActionMasked();
        final int actionIndex = e.getActionIndex();

        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsets[0] = mNestedOffsets[1] = 0;
        }
        final MotionEvent vtev = MotionEvent.obtain(e);
        vtev.offsetLocation(mNestedOffsets[0], mNestedOffsets[1]);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5f);

  /*              int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
                if (canScrollHorizontally) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_HORIZONTAL;
                }
                if (canScrollVertically) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_VERTICAL;
                }
                startNestedScroll(nestedScrollAxis, TYPE_TOUCH);*/
            }
            break;

            case MotionEvent.ACTION_POINTER_DOWN: {
                mScrollPointerId = e.getPointerId(actionIndex);
                mInitialTouchX = mLastTouchX = (int) (e.getX(actionIndex) + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY(actionIndex) + 0.5f);
            }
            break;

            case MotionEvent.ACTION_MOVE: {
                final int index = e.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    Log.e(TAG, "Error processing scroll; pointer index for id "
                            + mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                int dx = mLastTouchX - x;
                int dy = mLastTouchY - y;
                if (!isDrag) {
                    if (dy > 0) {
                        dy = Math.max(0, dy - touchSlop);
                    } else {
                        dy = Math.min(0, dy + touchSlop);
                    }
                    if (dy != 0) {
                        isDrag = true;
                    }
                }
                if (isDrag) {
                    mLastTouchX = x;
                    mLastTouchY = y;
                    if (dy != 0) {
                        offsetChild(-dy);
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
            }
            break;

            case MotionEvent.ACTION_POINTER_UP: {
                onPointerUp(e);
            }
            break;

            case MotionEvent.ACTION_UP: {
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
            }
            break;

            case MotionEvent.ACTION_CANCEL: {
                cancelScroll();
            }
            break;
        }

        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();

        return true;
    }

    public void offsetChild(int dy) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).offsetTopAndBottom(dy);
        }
    }

    void defaultOnMeasure(int widthSpec, int heightSpec) {
        final int width = chooseSize(widthSpec,
                getPaddingLeft() + getPaddingRight(),
                ViewCompat.getMinimumWidth(this));
        final int height = chooseSize(heightSpec,
                getPaddingTop() + getPaddingBottom(),
                ViewCompat.getMinimumHeight(this));
        setMeasuredDimension(width, height);
    }

    public static int chooseSize(int spec, int desired, int min) {
        final int mode = View.MeasureSpec.getMode(spec);
        final int size = View.MeasureSpec.getSize(spec);
        switch (mode) {
            case View.MeasureSpec.EXACTLY:
                return size;
            case View.MeasureSpec.AT_MOST:
                return Math.min(size, Math.max(desired, min));
            case View.MeasureSpec.UNSPECIFIED:
            default:
                return Math.max(desired, min);
        }
    }


    @Override
    public void requestLayout() {
        super.requestLayout();
        Log.d(TAG, "requestLayout: ");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d(TAG, "dispatchDraw: ");
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Log.d(TAG, "drawChild: ");
        return super.drawChild(canvas, child, drawingTime);
    }

    Paint paint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(DisplayUtil.dp2Px(getContext(), 18f));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(Color.RED);
        }
        String drawText = "我的内容了";
        canvas.save();
        canvas.drawText(drawText, (getMeasuredWidth() - paint.measureText(drawText)) / 2, (getMeasuredHeight() / 2 - 10), paint);
        canvas.restore();
    }
}