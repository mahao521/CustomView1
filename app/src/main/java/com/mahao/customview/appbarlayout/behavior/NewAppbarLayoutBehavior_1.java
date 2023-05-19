package com.mahao.customview.appbarlayout.behavior;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.mahao.customview.R;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static androidx.core.view.ViewCompat.TYPE_TOUCH;

/**
 * 将偏移量放在头部。
 */
public class NewAppbarLayoutBehavior_1 extends AppBarLayout.Behavior {

    private static final String TAG = "NewAppbarLayoutBehavior";
    private static final int INVALIDEHEIGHT = -1;
    private ImageView mIvHeader;
    private int mIvHeaderHeight = INVALIDEHEIGHT;
    private static int STATE_IDIL = 1;
    private static int STATE_DRAG = 2;
    private static int STATE_FLING = 3;
    private static int mCurrentState = STATE_IDIL;
    private static float mMaxRatio = 2.4f;
    private static float mRefreshRatio = 1.4f;
    private ValueAnimator offsetAnimator;
    public static final TimeInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final int MAX_OFFSET_ANIMATION_DURATION = 600; // ms
    //第一次下拉 不希望走pre,因为会抖动，只希望走onNested。 \
    //第二次 上拉： 不希望走onNested
    private VelocityTracker velocityTracker;

    private View placeHolder;
    private float[] ivMatrixArr = new float[9];
    private float[] ivMatrixPreArr = new float[9];

    private int STATE_EXPAND = 2;
    private int STATE_DEFAULT = 0;
    private int STATE_REFRESH = 1;

    private float originScale = 1.0f;

    public NewAppbarLayoutBehavior_1() {
    }

    public NewAppbarLayoutBehavior_1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout abl, int layoutDirection) {
        Log.d(TAG, "onLayoutChild: ");
        boolean b = super.onLayoutChild(parent, abl, layoutDirection);
        if (mIvHeader == null) {
            placeHolder = abl.findViewById(R.id.iv_place_holder);
            mIvHeader = abl.findViewById(R.id.iv_header);
            Matrix matrix = mIvHeader.getImageMatrix();
            matrix.getValues(ivMatrixArr);
            mIvHeaderHeight = mIvHeader.getHeight();
            if (hideHeight == -1) {
                hideHeight = placeHolder.getHeight() - mIvHeader.getHeight();
                this.setTopAndBottomOffset(-hideHeight);
            }
        }
        Log.d(TAG, "onLayoutChild: " + mIvHeader.getHeight());
        return b;
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull View directTargetChild, View target, int nestedScrollAxes, int type) {
        initView(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    private int hideHeight = -1;

    //是不是之前的behavior 也只需要設置ivheader的top值就可以了
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        Log.d(TAG, "onNestedPreScroll:   currH " + mIvHeaderHeight);
        float ration = Math.max(0, 1 - Math.abs(child.getTop()) * 1.0f / hideHeight);
        Log.d(TAG, "onNestedPreScroll: dy pre " + dy);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvHeader.getLayoutParams();
        if (child.getTop() >= -hideHeight && dy > 0 && consumed[1] > 0) {
            layoutParams.height = layoutParams.height - consumed[1];
        } else if (child.getTop() < -hideHeight) {
            layoutParams.height = mIvHeaderHeight;
        }
        Log.d(TAG, "onNestedScroll: height 2  " + ration + "    " + layoutParams.height + "   " + mIvHeaderHeight);
        layoutParams.height = Math.min(mIvHeader.getHeight(), layoutParams.height);
        mIvHeader.setLayoutParams(layoutParams);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        Log.d(TAG, "onNestedScroll:----3 " + dyConsumed + " " + dyUnconsumed + " " + consumed[1]);
        float ration = 1 - Math.abs(child.getTop()) * 1.0f / hideHeight;
        ration = Math.max(0, ration);
        dyUnconsumed = (int) (dyUnconsumed * (1 - ration));
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        Log.d(TAG, "onNestedScroll: " + originScale * (1 + ration) + "//// " + ration);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvHeader.getLayoutParams();
        if (dyUnconsumed < 0 && child.getTop() <= 0 && child.getTop() >= -hideHeight && consumed[1] < 0) {
            //   consumed[1] = (int) (consumed[1] * (1-ration));
            layoutParams.height = layoutParams.height - consumed[1];
        } else if (child.getTop() < -hideHeight) {
            layoutParams.height = mIvHeaderHeight;
        }
        layoutParams.height = Math.min(placeHolder.getHeight(), layoutParams.height);
        mIvHeader.setLayoutParams(layoutParams);
        Log.d(TAG, "onNestedScroll: height 1 " + mIvHeader.getHeight() + " " + placeHolder.getHeight());
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        Log.d(TAG, "onStopNestedScroll:  " + mIvHeader.getMeasuredHeight());
        int refreshHeight = getRefreshHeight();

        if (mIvHeader.getHeight() >= refreshHeight) {
            int distance = (int) (mIvHeader.getHeight() - refreshHeight);
            Log.d(TAG, "onStopNestedScroll: " + distance + " " + mIvHeader.getHeight() + " " + refreshHeight);
            animateOffsetTo(mIvHeader, distance, 0, (float) refreshHeight, child);
        } else if (mIvHeader.getHeight() < refreshHeight) {
            int distance = mIvHeader.getHeight() - mIvHeaderHeight;
            //   animateOffsetTo(mIvHeader, distance, 0, mIvHeaderHeight);
            //    setTopAndBottomOffset(-distance);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    public void initView(AppBarLayout child) {

    }

    private int activePointerId = INVALID_POINTER;
    private static final int INVALID_POINTER = -1;
    private int lastMotionY = 0;
    private boolean isBeingDragged = false;
    final int[] mReusableIntPair = new int[2];
    // private final int[] mScrollOffset = new int[2];

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull MotionEvent ev) {
        boolean consumeUp = false;
        isBeingDragged = true;
        final int actionIndex = ev.getActionIndex();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                activePointerId = ev.getPointerId(0);
                lastMotionY = (int) (ev.getY() + 0.5f);
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                activePointerId = ev.getPointerId(actionIndex);
                lastMotionY = (int) (ev.getY(actionIndex) + 0.5f);
            }
            break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(activePointerId);
                if (activePointerIndex == -1) {
                    return false;
                }
                final int y = (int) ev.getY(activePointerIndex);
                int dy = lastMotionY - y;
                lastMotionY = y;
                Log.d(TAG, "onTouchEvent:  " + dy);
                if (dy < 0) {  //下拉
                    onNestedScroll(parent, child, mIvHeader, 0, 0, 0, dy, TYPE_TOUCH, mReusableIntPair);
                } else if (dy > 0) { //向上缩小
                    onNestedPreScroll(parent, child, mIvHeader, 0, dy, mReusableIntPair, TYPE_TOUCH);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int newIndex = ev.getActionIndex() == 0 ? 1 : 0;
                activePointerId = ev.getPointerId(newIndex);
                lastMotionY = (int) (ev.getY(newIndex) + 0.5f);
                break;
            case MotionEvent.ACTION_UP:
                if (velocityTracker != null) {
                    consumeUp = true;
                    velocityTracker.addMovement(ev);
                    velocityTracker.computeCurrentVelocity(1000);
                    float yvel = velocityTracker.getYVelocity(activePointerId);
                    Log.d(TAG, "onTouchEvent:    yvel  ---- " + yvel);
                    onStopNestedScroll(parent, child, mIvHeader, TYPE_TOUCH);
                    //fling(parent, child, -getScrollRangeForDragFling(child), 0, yvel);
                }
            case MotionEvent.ACTION_CANCEL:
                isBeingDragged = false;
                activePointerId = INVALID_POINTER;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        if (velocityTracker != null) {
            velocityTracker.addMovement(ev);
        }
        return isBeingDragged || consumeUp;
    }


    private void ensureVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull MotionEvent ev) {
        ensureVelocityTracker();
        velocityTracker.addMovement(ev);
        initView(child);
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    public void setState(int state) {
        mCurrentState = state;

    }


    private void animateOffsetTo(
            @NonNull final View child,
            final float distance,
            float velocity,
            float endIvHeaderHeight,
            final AppBarLayout appBarLayout) {
        final int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 3 * Math.round(1000 * (distance / velocity));
        } else {
            final float distanceRatio = (float) distance / child.getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }
        animateOffsetWithDuration(child, distance, endIvHeaderHeight, duration, appBarLayout);
    }

    private void animateOffsetWithDuration(
            final View child,
            final float distance,
            final float endIvHeaderHeight,
            final int duration, final AppBarLayout appBarLayout) {
        if (mIvHeader.getHeight() == endIvHeaderHeight) {
            if (offsetAnimator != null && offsetAnimator.isRunning()) {
                offsetAnimator.cancel();
                Log.d(TAG, "animateOffsetWithDuration: cancel");
            }
            return;
        }
        final int[] aa = {-1};
        if (offsetAnimator == null) {
            Log.d(TAG, "animateOffsetWithDuration: start ");
            offsetAnimator = new ValueAnimator();
            offsetAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
            offsetAnimator.addUpdateListener(
                    new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(@NonNull ValueAnimator animator) {

                            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                            layoutParams.height = (int) animator.getAnimatedValue();
                            int offset1 = placeHolder.getHeight() - (int) animator.getAnimatedValue();
                            Log.d(TAG, "onAnimationUpdate: " + "   " + "   " + offset1);
                            setTopAndBottomOffset(-offset1);
                            child.setLayoutParams(layoutParams);
                        }
                    });
            offsetAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setState(STATE_FLING);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setState(STATE_IDIL);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    setState(STATE_IDIL);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            offsetAnimator.cancel();
        }
        offsetAnimator.setDuration(Math.min(duration, MAX_OFFSET_ANIMATION_DURATION));
        offsetAnimator.setIntValues(mIvHeader.getHeight(), (int) endIvHeaderHeight);
        offsetAnimator.start();
    }

    private int getRefreshHeight() {
        return (int) (placeHolder.getHeight() * 0.6f);
    }

}