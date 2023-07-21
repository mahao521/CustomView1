package com.mahao.customview.livedata

import android.util.Log
import androidx.lifecycle.MutableLiveData

class LifeLiveData<T> : MutableLiveData<T>() {

    private val TAG = "LiveDataAdapter"

    override fun onActive() {
        super.onActive()
     //   Log.d(TAG, "onActive: 111   " + hasActiveObservers())
    }


    override fun onInactive() {
        super.onInactive()
       // Log.d(TAG, "onInactive: 222  " + hasActiveObservers())
    }
}