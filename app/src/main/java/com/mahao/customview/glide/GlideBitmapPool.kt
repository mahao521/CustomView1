package com.mahao.customview.glide

import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool

class GlideBitmapPool(maxSize: Long) : LruBitmapPool(maxSize) {

    private val TAG = "ImageLayout"

    override fun put(bitmap: Bitmap?) {

        super.put(bitmap)
        Log.d(TAG, "put:  存入BitmaPool" + bitmap)
        Thread.sleep(1000)
        Log.d(TAG, "put: ---- " + bitmap?.isRecycled)

    }


    override fun get(width: Int, height: Int, config: Bitmap.Config?): Bitmap {
        Log.d(TAG, "get: " + "获取bitmapPool")
        return super.get(width, height, config)
    }

    override fun getDirty(width: Int, height: Int, config: Bitmap.Config?): Bitmap {
        Log.d(TAG, "getDirty: 获取bitmap之前")
        var dirty = super.getDirty(width, height, config)
        Log.d(TAG, "getDirty: " + "获取bitmapPool   " + dirty)

        return dirty
    }


}