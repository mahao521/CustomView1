package com.mahao.customview.glide

import android.util.Log
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.cache.LruResourceCache

class GlideLruResourceCache(size: Long) : LruResourceCache(size) {

    private val TAG = "GlideActivity"

    override fun put(key: Key, item: Resource<*>?): Resource<*>? {
        Log.d(TAG, "put: 添加到了内存缓存 " + item.toString())
        return super.put(key, item)
    }


    override fun onItemEvicted(key: Key, item: Resource<*>?) {
        Log.d(TAG, "onItemEvicted: 从内存缓存中移除 " + item.toString())
        super.onItemEvicted(key, item)
    }

    override fun remove(key: Key): Resource<*>? {
        Log.d(TAG, "remove: 移除")
        return super.remove(key)

    }

    override fun get(key: Key): Resource<*>? {
        Log.d(TAG, "get: 从内存缓存获取")
        return super.get(key)
    }


}