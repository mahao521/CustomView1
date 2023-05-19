package com.mahao.customview.appbarlayout.behavior;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.google.android.material.appbar.AppBarLayout;
import com.mahao.customview.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import static androidx.core.view.ViewCompat.TYPE_NON_TOUCH;
import static androidx.core.view.ViewCompat.TYPE_TOUCH;

/**
 * 将nested去掉，全部放在pre里面
 */
public class NewAppbarLayoutBehavior2 extends AppBarLayout.Behavior {

    private static final String TAG = "NewAppbarLayoutBehavior";
    private static final int INVALIDEHEIGHT = -1;
    private ImageView mIvHeader;
    private int mIvHeaderHeight = INVALIDEHEIGHT;
    private int mIvHeaderTargetHeight;
    private int mCurrentHeaderHeight;
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
    private int touchSlop = -1;
    OverScroller scroller;
    private Runnable flingRunnable;

    public NewAppbarLayoutBehavior2() {
    }

    public NewAppbarLayoutBehavior2(Context context, AttributeSet attrs) {
        super(context, attrs);
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


    private int lastY = 0;
    private int totalNested = 0;
    private boolean isChangedNested = false;
    private int totalPre = 0;
    private boolean isChangePre = false;


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        Log.d(TAG, "onNestedPreScroll: " + dy);
        if (dy < 0 && child.getTop() == 0 && mCurrentHeaderHeight < mIvHeaderTargetHeight && type == TYPE_TOUCH) { // 向下放大
            if (lastY > 0 && !isChangePre) {
                isChangedNested = true;
                totalNested = dy;
            } else {
                totalNested += dy;
            }
            int delta = Math.abs(dy);
            if (mCurrentHeaderHeight + delta > mIvHeaderTargetHeight) {
                delta = mIvHeaderTargetHeight - mCurrentHeaderHeight;
            }
            //计算阻尼
            delta = (int) (delta * (1 - dampingRatio(mCurrentHeaderHeight, mIvHeaderTargetHeight)));
            Log.d(TAG, "onNestedPreScroll: ---- " + delta);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvHeader.getLayoutParams();
            if (!isChangedNested) {
                mCurrentHeaderHeight += delta;
                layoutParams.height = mCurrentHeaderHeight;
            } else if (Math.abs(totalNested) >= touchSlop && lastY < 0) {
                mCurrentHeaderHeight += delta;
                layoutParams.height = mCurrentHeaderHeight;
                isChangedNested = false;
            } else {
                //  Log.d(TAG, "onNestedScroll: 跳过 --1  " + dy);
            }
            mIvHeader.setLayoutParams(layoutParams);
            consumed[1] += delta;
            //Log.d(TAG, "onNestedScroll: " + layoutParams.height + " " + delta);
            setState(STATE_DRAG);
        } else if (dy > 0 && mCurrentHeaderHeight > mIvHeaderHeight && type == TYPE_TOUCH) { //向上缩小
            //停止动画
            //回复图片原始高度。
            if (lastY < 0 && !isChangedNested) {
                totalPre = dy;
                isChangePre = true;
            } else {
                totalPre += dy;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvHeader.getLayoutParams();
            if (mCurrentHeaderHeight - dy < mIvHeaderHeight) {
                dy = mCurrentHeaderHeight - mIvHeaderHeight;
            }
            if (!isChangePre) {
                mCurrentHeaderHeight -= dy;
                layoutParams.height = mCurrentHeaderHeight;
            } else if (Math.abs(totalPre) > touchSlop && lastY > 0) {
                mCurrentHeaderHeight -= dy;
                layoutParams.height = mCurrentHeaderHeight;
                isChangePre = false;
            } else {
                //   Log.d(TAG, "onNestedScroll: 跳过 --2 " + dy);
            }
            //      Log.d(TAG, "onNestedScroll: ----1 " + mCurrentHeaderHeight);
            mIvHeader.setLayoutParams(layoutParams);
            consumed[1] = dy;
            setState(STATE_DRAG);
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
        lastY = dy;
        //  Log.d(TAG, "onNestedScroll:  --0 " + dy + " type = " + type + " consume[1] = " + consumed[1] + " state " + mCurrentState);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        //   Log.d(TAG, "onNestedScroll:----3 " + consumed[1] + " " + dyUnconsumed);
       /* if (dyUnconsumed < 0 && child.getTop() == 0 && type == TYPE_TOUCH && !isReCoverHederHeight) {  //向下，因为up操作，top值为负数。
            // Log.d(TAG, "onNestedScroll: " + mIvHeaderHeight);
            if (mCurrentHeaderHeight < mIvHeaderTargetHeight) {
                int delta = Math.abs(dyUnconsumed);
                if (mCurrentHeaderHeight + delta > mIvHeaderTargetHeight) {
                    delta = mIvHeaderTargetHeight - mCurrentHeaderHeight;
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvHeader.getLayoutParams();
                mCurrentHeaderHeight += delta;
                Log.d(TAG, "onNestedScroll: ----2 " + mCurrentHeaderHeight);
                layoutParams.height = mCurrentHeaderHeight;
                mIvHeader.setLayoutParams(layoutParams);
                consumed[1] += delta;
                //Log.d(TAG, "onNestedScroll: " + layoutParams.height + " " + delta);
                setState(STATE_DRAG);
            }
        }*/
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        Log.d(TAG, "onStopNestedScroll:  " + mIvHeader.getMeasuredHeight());
        float refreshHeight = getRefreshHeight();
        if (mCurrentHeaderHeight >= refreshHeight) {  //大于等于，刷洗
            float diffAnimHeight = mCurrentHeaderHeight - refreshHeight;
            animateOffsetTo(mIvHeader, diffAnimHeight, 0, refreshHeight);
        } else if (mCurrentHeaderHeight < refreshHeight) {    //执行复原
            float diffAnimHeight = mCurrentHeaderHeight - mIvHeaderHeight;
            animateOffsetTo(mIvHeader, diffAnimHeight, 0, mIvHeaderHeight);
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
        if (mIvHeaderHeight == INVALIDEHEIGHT) {
            mIvHeader = child.findViewById(R.id.iv_header);
            mIvHeaderHeight = mIvHeader.getMeasuredHeight();
            mIvHeaderTargetHeight = (int) (mIvHeaderHeight * mMaxRatio);
            mCurrentHeaderHeight = mIvHeaderHeight;
        }
        if (offsetAnimator != null) {
            offsetAnimator.cancel();
        }
        updateParentLastMotionY = false;
        final ViewConfiguration vc = ViewConfiguration.get(mIvHeader.getContext());
        touchSlop = vc.getScaledTouchSlop();
    }

    private int activePointerId = INVALID_POINTER;
    private static final int INVALID_POINTER = -1;
    private int lastMotionY = 0;
    private boolean isBeingDragged = false;
    final int[] mReusableIntPair = new int[2];
    private boolean updateParentLastMotionY = false;
    // private final int[] mScrollOffset = new int[2];

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull MotionEvent ev) {
        Log.d(TAG, "onTouchEvent: " + child.getTop());
        if (child.getTop() < 0) {
       /*     switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    final int activePointerIndex = ev.findPointerIndex(activePointerId);
                    if (activePointerIndex == -1) {
                        Log.d(TAG, "onTouchEvent1: " + false);
                        return false;
                    }
                    final int y = (int) ev.getY(activePointerIndex);
                    int dy = lastMotionY - y;
                    Log.d(TAG, "onTouchEvent1: " + activePointerIndex + " y  = " + y + "  dy = " + dy + " lastMotionY = " + lastMotionY);
            }*/
            if (!updateParentLastMotionY) {
                super.onInterceptTouchEvent(parent, child, ev);
                updateParentLastMotionY = true;
            }
            return super.onTouchEvent(parent, child, ev);
        } else {
            boolean consumeUp = false;
            isBeingDragged = true;
            final int actionIndex = ev.getActionIndex();
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    activePointerId = ev.getPointerId(0);
                    lastMotionY = (int) (ev.getY() + 0.5f);
                    Log.d(TAG, "onTouchEvent: --- down" + lastMotionY);
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
                    final int y = (int) (ev.getY(activePointerIndex) + 0.5f);
                    int dy = lastMotionY - y;
                    Log.d(TAG, "onTouchEvent:  -- move" + dy);
                    //   scroll(parent, child, dy, getMaxDragOffset(child), 0);
                    mReusableIntPair[1] = 0;
                    onNestedPreScroll(parent, child, mIvHeader, 0, dy, mReusableIntPair, TYPE_TOUCH);
                    lastMotionY = y;
                    int unConsumedY = dy - mReusableIntPair[1];
                    mReusableIntPair[1] = 0;
                    onNestedScroll(parent, child, mIvHeader, 0, 0, 0, unConsumedY, TYPE_TOUCH, mReusableIntPair);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    int newIndex = ev.getActionIndex() == 0 ? 1 : 0;
                    activePointerId = ev.getPointerId(newIndex);
                    lastMotionY = (int) (ev.getY(newIndex) + 0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouchEvent: --- up");
                    if (velocityTracker != null) {
                        consumeUp = true;
                        velocityTracker.addMovement(ev);
                        velocityTracker.computeCurrentVelocity(1000);
                        float yvel = velocityTracker.getYVelocity(activePointerId);
                        fling(parent, child, mIvHeaderHeight, mIvHeaderTargetHeight, yvel);
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
    }

    private void ensureVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull MotionEvent ev) {
        initView(child);
        boolean handle = super.onInterceptTouchEvent(parent, child, ev);
        Log.d(TAG, "onInterceptTouchEvent: " + handle);
        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE && isBeingDragged) {
            Log.d(TAG, "onInterceptTouchEvent: --- move");
            if (activePointerId == INVALID_POINTER) {
                // If we don't have a valid id, the touch down wasn't on content.
                return false;
            }
            int pointerIndex = ev.findPointerIndex(activePointerId);
            if (pointerIndex == -1) {
                return false;
            }
            int y = (int) ev.getY(pointerIndex);
            int yDiff = Math.abs(y - lastMotionY);
            if (yDiff > touchSlop) {
                lastMotionY = y;
                return true;
            }
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "onInterceptTouchEvent:  -- down");
            activePointerId = INVALID_POINTER;
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            isBeingDragged = parent.isPointInChildBounds(child, x, y);
            if (isBeingDragged) {
                lastMotionY = y;
                activePointerId = ev.getPointerId(0);
                ensureVelocityTracker();

                // There is an animation in progress. Stop it and catch the view.
                if (scroller != null && !scroller.isFinished()) {
                    scroller.abortAnimation();
                    return true;
                }
            }
        }
        if (velocityTracker != null) {
            velocityTracker.addMovement(ev);
        }
        return false;
    }

    public void setState(int state) {
        mCurrentState = state;
        if (state == STATE_IDIL && mCurrentHeaderHeight == getRefreshHeight()) {
            // 网络请求， 加载动画。
            //动画执行完成 再
            mIvHeader.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //这里应该考虑再加载动画中，又下拉了，此时距离不是刷新距离。
                    //   Log.d(TAG, "run: " + state + " " + mCurrentState);
                    if (mCurrentState == STATE_IDIL) { //假如网络执行完成了，此时在拖拽状态，不恢复了，也可以在drag时候取消anim
                        animateOffsetTo(mIvHeader, mCurrentHeaderHeight - mIvHeaderHeight, 0f, mIvHeaderHeight);
                    }
                }
            }, 1500);
        }
    }

    public int getRefreshHeight() {
        if (mIvHeaderHeight != INVALIDEHEIGHT) {
            return (int) Math.floor(mIvHeaderHeight * mRefreshRatio);
        }
        return 0;
    }

    private void animateOffsetTo(
            @NonNull final View child,
            final float distance,
            float velocity,
            float endIvHeaderHeight) {
        final int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 3 * Math.round(1000 * (distance / velocity));
        } else {
            final float distanceRatio = (float) distance / child.getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }
        animateOffsetWithDuration(child, endIvHeaderHeight, duration);
    }

    private void animateOffsetWithDuration(
            final View child,
            final float endIvHeaderHeight,
            final int duration) {
        if (mCurrentHeaderHeight == endIvHeaderHeight) {
            if (offsetAnimator != null && offsetAnimator.isRunning()) {
                offsetAnimator.cancel();
                Log.d(TAG, "animateOffsetWithDuration: cancel");
            }
            return;
        }

        if (offsetAnimator == null) {
            Log.d(TAG, "animateOffsetWithDuration: start ");
            offsetAnimator = new ValueAnimator();
            offsetAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
            offsetAnimator.addUpdateListener(
                    new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(@NonNull ValueAnimator animator) {
                            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                            mCurrentHeaderHeight = (int) animator.getAnimatedValue();
                            layoutParams.height = (int) animator.getAnimatedValue();
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
        offsetAnimator.setIntValues(mCurrentHeaderHeight, (int) endIvHeaderHeight);
        offsetAnimator.start();
    }

    public float dampingRatio(int top, int totalHeight) {
        float diffRatio = top * 1.0f / totalHeight;
        return diffRatio;
    }

    final boolean fling(
            CoordinatorLayout coordinatorLayout,
            @NonNull AppBarLayout layout,
            int minOffset,
            int maxOffset,
            float velocityY) {
        if (flingRunnable != null) {
            layout.removeCallbacks(flingRunnable);
            flingRunnable = null;
        }
        if (scroller == null) {
            scroller = new OverScroller(layout.getContext());
        }
        if (offsetAnimator != null && offsetAnimator.isRunning()) {
            offsetAnimator.cancel();
        }
        scroller.fling(
                0,
                mCurrentHeaderHeight,
                0,
                Math.round(velocityY), // velocity.
                0,
                0, // x
                minOffset,
                maxOffset); // y

        if (scroller.computeScrollOffset()) {
            flingRunnable = new FlingRunnable(coordinatorLayout, layout);
            ViewCompat.postOnAnimation(layout, flingRunnable);
            return true;
        } else {
            onStopNestedScroll(coordinatorLayout, layout, mIvHeader, TYPE_NON_TOUCH);
            return false;
        }
    }

    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout parent;
        private final AppBarLayout layout;
        private float lastScrollerY = -1;

        FlingRunnable(CoordinatorLayout parent, AppBarLayout layout) {
            this.parent = parent;
            this.layout = layout;
        }

        @Override
        public void run() {
            if (layout != null && scroller != null) {
                if (scroller.computeScrollOffset()) {
                    if (scroller.getCurrY() >= mIvHeaderHeight) {
                        ViewGroup.LayoutParams layoutParams = mIvHeader.getLayoutParams();
                        mCurrentHeaderHeight = scroller.getCurrY();
                        layoutParams.height = mCurrentHeaderHeight;
                        mIvHeader.setLayoutParams(layoutParams);
                    } else {
                        // Log.d(TAG, "run: " + scroller.getCurrY() + " " + mIvHeaderHeight + " " + layout.getTop());
                     /* if (lastScrollerY == -1) {
                            lastScrollerY = mIvHeaderHeight;
                        }
                        float diffscroller = scroller.getCurrY() - lastScrollerY;
                        Log.d(TAG, "run: " + diffscroller + "  " + scroller.getCurrY() + "   == " + layout.getTop());
                        if (layout.getTop() > -mIvHeaderHeight && layout.getTop() + diffscroller <= -mIvHeaderHeight) {
                            diffscroller = -(mIvHeaderHeight + layout.getTop());
                            Log.d(TAG, "run : ------ " + diffscroller);
                        } else if (layout.getTop() == -mIvHeaderHeight) {
                            diffscroller = 0;
                        }
                        Log.d(TAG, "run: ------------------ " + diffscroller);
                        layout.offsetTopAndBottom((int) diffscroller);*/
                    }
                    lastScrollerY = scroller.getCurrY();
                    ViewCompat.postOnAnimation(layout, this);
                } else {
                    onStopNestedScroll(parent, layout, mIvHeader, TYPE_NON_TOUCH);
                }
            }
        }
    }
}





























