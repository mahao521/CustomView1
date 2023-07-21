package com.mahao.customview.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.forEachIndexed

class WrapCancelGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val TAG = "WrapCancelGroup"


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var childCount = childCount
        for (index in 0..childCount - 1) {
            var childAt = getChildAt(index)
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childAt = getChildAt(0)
        Log.d(TAG, "onLayout: " + childAt.left + "... " + childAt.top)
        if (childAt != null) {
            childAt.layout(
                childAt.left,
                childAt.top,
                childAt.left + childAt.measuredWidth,
                childAt.top + childAt.measuredHeight
            )
        }
    }


    var downX: Float = 0.0f
    var downY: Float = 0.0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "onTouchEvent:  down ")

            }

            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "onTouchEvent:   move ")
            }

            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "onTouchEvent:   cancel ")
            }

            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "onTouchEvent:   up  ")

            }
        }
        return super.onTouchEvent(event);
    }

}