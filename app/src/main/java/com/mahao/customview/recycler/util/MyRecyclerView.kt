package com.mahao.customview.recycler.util

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View.MeasureSpec
import androidx.recyclerview.widget.RecyclerView
import com.mahao.customview.widget.CustomLayout

class MyRecyclerView : RecyclerView {

    private  val TAG = "MyRecyclerView"

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
       // getCacheList("mChangedScrap", myChangeList)
        getCacheList("mAttachedScrap", myAttachList)
        getCacheList("mCachedViews", myCacheList)
        super.onLayout(changed, l, t, r, b)
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        getCacheList("mAttachedScrap", myAttachList)
        getCacheList("mCachedViews", myCacheList)
        val modeW = MeasureSpec.getMode(widthSpec)
        val sizeW = MeasureSpec.getSize(widthSpec)
        val modeH = MeasureSpec.getMode(heightSpec)
        val sizeH = MeasureSpec.getSize(heightSpec)
        if (modeW == MeasureSpec.EXACTLY) {
            Log.d(TAG, "onMeasure: mode w  exactly")
        } else if (modeW == MeasureSpec.AT_MOST) {
            Log.d(TAG, "onMeasure: mode w  At most")
        }
        if (modeH == MeasureSpec.EXACTLY) {
            Log.d(TAG, "onMeasure: mode h  exactly")
        } else if (modeH == MeasureSpec.AT_MOST) {
            Log.d(TAG, "onMeasure: mode h  At most")
        }
        Log.d(TAG, "onMeasure: $sizeW ----- $sizeH")
        super.onMeasure(widthSpec, heightSpec)
        Log.d(TAG, "onMeasure: $measuredWidth ----- $measuredHeight")
    }

    private var myAttachList = MyArrayList<RecyclerView.ViewHolder>("attach",this)
    private var myCacheList = MyArrayList<RecyclerView.ViewHolder>("cache",this)
    private var myChangeList = MyArrayList<RecyclerView.ViewHolder>("change",this)

    private fun getCacheList(name: String, list: MyArrayList<RecyclerView.ViewHolder>) {
        var recyclerField = Class.forName("androidx.recyclerview.widget.RecyclerView").getDeclaredField("mRecycler")
        if (!recyclerField.isAccessible) {
            recyclerField.isAccessible = true
        }
        var recycler = recyclerField.get(this)
        var declaredField = recycler.javaClass.getDeclaredField(name)
        if (!declaredField.isAccessible) {
            declaredField.isAccessible = true
        }
        //   var get = declaredField.get(recycler) as? ArrayList<RecyclerView.ViewHolder>
        //  var hookArrayList = HookClass().hookArrayList()
        declaredField.set(recycler, list)
    }
}