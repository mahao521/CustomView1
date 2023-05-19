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

import com.google.android.material.appbar.AppBarLayout;
import com.mahao.customview.R;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static androidx.core.view.ViewCompat.TYPE_TOUCH;

/**
 * 将下拉操作放在onNested中。
 */
public class NewAppbarLayoutBehavior2_1 extends AppBarLayout.Behavior {

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
    private boolean isReCoverHederHeight = false;
    private VelocityTracker velocityTracker;

    public NewAppbarLayoutBehavior2_1() {
    }

    public NewAppbarLayoutBehavior2_1(Context context, AttributeSet attrs) {
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

    //过滤lastDy
    private boolean isChangePre = false;
    private int lastY = 0;
    private int totalDy = 0;

    private int totalDyNested = 0;
    private boolean isChangedNested = false;

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        final ViewConfiguration vc = ViewConfiguration.get(mIvHeader.getContext());
        int touchSlop = vc.getScaledTouchSlop();
        // 每次都会执行这里，会给我减掉,造成抖动，这里不好解决。
        if (dy > 0 && mCurrentHeaderHeight > mIvHeaderHeight) { //向上缩小
            if (lastY < 0 && !isChangedNested) {
                totalDy = dy;
                isChangePre = true;
            }else {
                totalDy += dy;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvHeader.getLayoutParams();
            if (mCurrentHeaderHeight - dy < mIvHeaderHeight) {
                dy = mCurrentHeaderHeight - mIvHeaderHeight;
            }
            if (!isChangePre) {
                mCurrentHeaderHeight -= dy;
                layoutParams.height = mCurrentHeaderHeight;
            } else if (totalDy > touchSlop) {
                totalDy = 0;
                mCurrentHeaderHeight -= dy;
                layoutParams.height = mCurrentHeaderHeight;
                isChangePre = false;
            } else {
                Log.d(TAG, "onNestedScroll:  --0  过滤了 " + dy);
            }
            mIvHeader.setLayoutParams(layoutParams);
            consumed[1] = dy;
            setState(STATE_DRAG);
        } else if (dy < 0 && child.getTop() == 0 && mCurrentHeaderHeight < mIvHeaderTargetHeight) { //下拉放大
            if (lastY > 0 && !isChangePre) {
                totalDyNested = dy;
                isChangedNested = true;
            }else {
                totalDyNested += dy;
            }
            if (!isChangedNested) {
            } else if (Math.abs(totalDyNested) > touchSlop) {
                isChangedNested = false;
                totalDyNested = 0;
            } else {
                Log.d(TAG, "onNestedScroll:----3-----  过滤了 " + dy);
                consumed[1] = dy;
            }
        } else {
            if (dy < 0) {
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
            } else if (dy >= 0) {
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
            }
        }
        lastY = dy;
        Log.d(TAG, "onNestedScroll:  --0 " + dy + " type = " + type + " consume[1] = " + consumed[1] + " state " + mCurrentState);
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        Log.d(TAG, "onNestedScroll:----3 " + dyConsumed + " " + dyUnconsumed);
        if (dyUnconsumed < 0 && child.getTop() == 0 && type == TYPE_TOUCH) {  //向下，因为up操作，top值为负数。
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
        }
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
        isReCoverHederHeight = false;
    }

    private int activePointerId = INVALID_POINTER;
    private static final int INVALID_POINTER = -1;
    private int lastMotionY = 0;
    private boolean isBeingDragged = false;
    final int[] mReusableIntPair = new int[2];
    // private final int[] mScrollOffset = new int[2];

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull MotionEvent ev) {
        if (child.getTop() < 0) {
            return super.onTouchEvent(parent, child, ev);
        } else if (false) {
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
                    //   scroll(parent, child, dy, getMaxDragOffset(child), 0);
                    mReusableIntPair[1] = 0;
                    //     mIvHeader.getLocationInWindow(mScrollOffset);
                    //        int startY = mScrollOffset[1];
                    onNestedPreScroll(parent, child, mIvHeader, 0, dy, mReusableIntPair, TYPE_TOUCH);
                    //    mIvHeader.getLocationInWindow(mScrollOffset);
                    //    mScrollOffset[1] -= startY;
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
        return false;
    }

   /* private void scrollHeaderView(CoordinatorLayout parent, AppBarLayout child, int dy) {
        if (mCurrentHeaderHeight < mIvHeaderTargetHeight) {
            int delta = Math.abs(dy);
            if (mCurrentHeaderHeight + delta > mIvHeaderTargetHeight) {
                delta = mIvHeaderTargetHeight - mCurrentHeaderHeight;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvHeader.getLayoutParams();
            mCurrentHeaderHeight += delta;
            Log.d(TAG, "scrollHeaderView: ---- " + mCurrentHeaderHeight);
            layoutParams.height = mCurrentHeaderHeight;
            mIvHeader.setLayoutParams(layoutParams);
            setState(STATE_DRAG);
        }
    }*/

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
        if (state == STATE_IDIL && mCurrentHeaderHeight == getRefreshHeight()) {
            // 网络请求， 加载动画。
            //动画执行完成 再
            mIvHeader.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //这里应该考虑再加载动画中，又下拉了，此时距离不是刷新距离。
                    Log.d(TAG, "run: " + state + " " + mCurrentState);
                    if (mCurrentState == STATE_IDIL) { //假如网络执行完成了，此时在拖拽状态，不恢复了，也可以在drag时候取消anim
                        animateOffsetTo(mIvHeader, mCurrentHeaderHeight - mIvHeaderHeight, 0f, mIvHeaderHeight);
                    }
                }
            }, 2000);
        }
    }

    public float getRefreshHeight() {
        if (mIvHeaderHeight != INVALIDEHEIGHT) {
            return mIvHeaderHeight * mRefreshRatio;
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
}