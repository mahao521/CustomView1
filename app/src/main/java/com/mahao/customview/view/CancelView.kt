package com.mahao.customview.view

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import java.util.concurrent.TimeUnit
import kotlin.math.log


//1、验证touchView被移除。
//1、验证move中触发了down事件。
//1、销毁当前页面

class CancelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val TAG = "CancelView"

    private var mIv: ImageView;

    init {
        mIv = ImageView(getContext())
        /* addView(
             mIv,
             RelativeLayout.LayoutParams(
                 DisplayUtil.dp2Px(context, 100f),
                 DisplayUtil.dp2Px(context, 100f)
             )
         )*/
        //  mIv.setImageDrawable(resources.getDrawable(R.drawable.green))

        var flag = 1;
        this.setOnClickListener {
            //验证调用刷新---下一帧16ms才开始执行绘制操作。
            /* Observable.create<Int> {
                 while (true) {
                     if (flag < 10) {
                         postInvalidate()
                         Thread.sleep(10)
                         flag++;
                         Log.d(TAG, ": flag =  " + flag)
                     } else {
                         break;
                     }
                 }
                 it.onNext(1)
                 it.onComplete()
             }.subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe({
                     Log.d(TAG, "任务执行完成，重置 ")
                     flag = it
                 })*/

            //从打印结果可以看出，调用requestLayout 下一帧16ms才会执行onMeasure和OnLayout。
            var flag = 1
            var disposable = Observable.interval(5, 5, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    io.reactivex.rxjava3.observers.DisposableObserver<Long>() {
                    override fun onNext(t: Long) {
                        flag++
                        requestLayout()
                        if (flag == 30) {
                            onComplete()
                            dispose()
                        }
                    }

                    override fun onError(e: Throwable) {}
                    override fun onComplete() {
                        Log.d(TAG, "onComplete:  ///// ")
                    }

                    override fun onStart() {
                        super.onStart()
                        Log.d(TAG, "onStart: +++++= ")
                    }
                })
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure: ")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    //
    var currentLayoutTime = System.currentTimeMillis();
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "onLayout: " + +(System.currentTimeMillis() - currentLayoutTime))
        currentLayoutTime = System.currentTimeMillis()
        super.onLayout(changed, l, t, r, b)


    }


    //验证了每16ms绘制一次。
    var currentTime = System.currentTimeMillis();
    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG, "onDraw: " + (System.currentTimeMillis() - currentTime))
        currentTime = System.currentTimeMillis();
        super.onDraw(canvas)

    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "dispatchTouchEvent: ")

        return super.dispatchTouchEvent(ev)
    }

    /*  override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
          Log.d(TAG, "onInterceptTouchEvent: ")
          return super.onInterceptTouchEvent(ev)
      }*/

    var downX = 0.0f
    var downY = 0.0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: ")
        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "onTouchEvent: ----down ")
                downX = event.getX()
                downY = event.getY()
                android.os.Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        //(this@CancelView.parent as ViewGroup).removeView(this@CancelView)
                        // Thread.sleep(15000)
                    }
                }, 2000)
            }

            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "onTouchEvent:  +++++ move++++++ ")
                var moveX = event.getX()
                var moveY = event.getY()
                var diffX = moveX - downX
                var diffY = moveY - downY
                offsetLeftAndRight(diffX.toInt())
                offsetTopAndBottom(diffY.toInt())
                // downX = moveX
                //downY = moveY
            }

            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "onTouchEvent:  0------ cancel ")
            }

            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "onTouchEvent:  up  ")
            }

        }
        return super.onTouchEvent(event);
    }


    override fun requestLayout() {
        Log.d(TAG, "requestLayout:  开始requestLayout ")
        super.requestLayout()
    }

    override fun invalidate() {
        Log.d(TAG, "invalidate: 开始绘制")
        super.invalidate()
    }

    override fun postInvalidate() {
        super.postInvalidate()
    }


}