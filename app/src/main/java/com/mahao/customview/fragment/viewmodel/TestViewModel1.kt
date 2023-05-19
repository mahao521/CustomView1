package com.mahao.customview.fragment.viewmodel

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistry

class TestViewModel1 : ViewModel {

    private val TAG = "TestViewModel1"

    private var handle: SavedStateHandle? = null

    private var liveData: MutableLiveData<String>? = null

    constructor(handle: SavedStateHandle) {
        this.handle = handle
        initData()
    }

    fun initData() {
        liveData = handle?.getLiveData<String>("key", "1223")
        liveData?.value = "1224"
        var myValue = handle?.get<String>("key")
        Log.d(TAG, ": key  = " + myValue)
    }

    fun testResore() {
        var bundle = handle?.get<Bundle>("mahao")
        var age = bundle?.getString("age")
        Log.d(TAG, "testResore: " + age)
        var liveData = handle?.getLiveData<Bundle>("mahao")
        Log.d(TAG, "testResore: >>>> " + liveData?.value?.get("age"))
        liveData?.observeForever {
            var stringAge = it.get("age")
            Log.d(TAG, "testResore: ----- " + stringAge)
        }
    }

    fun testSave() {
        handle?.setSavedStateProvider("mahao", object : SavedStateRegistry.SavedStateProvider {
            override fun saveState(): Bundle {
                var bundle = Bundle()
                bundle.putString("age", "27")
                Log.d(TAG, "saveState:   viewmodel 数据保存了")
                return bundle
            }
        })
    }

    fun testModify() {
        var liveData = handle?.getLiveData<Bundle>("mahao")
        var bundle = Bundle()
        bundle.putString("age", "23")
        liveData?.postValue(bundle)
    }
}