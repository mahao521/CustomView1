package com.mahao.customview.livedata.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mahao.customview.livedata.LastVersionLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.random.Random

class HolderViewModel : ViewModel() {

    var liveData = MutableLiveData<Int>()
    var lastVersionLiveData = LastVersionLiveData<DataBean>();

    fun getIntData() {
        Observable.create<Int> {
            Thread.sleep(7000)
            it.onNext(100 * Random.nextInt(100))
            it.onComplete()
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("LiveDataAdapter", "getIntData: 发射数据")
                liveData.value = it
            }
    }

    fun getIntData(liveData: MutableLiveData<Int>) {
        Observable.create<Int> {
            Thread.sleep(8000)
            it.onNext(100 * Random.nextInt(100))
            it.onComplete()
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("LiveDataAdapter", "getIntData: 发射数据" + it)
                liveData.value = it
            }
    }

    fun getLastVersionIntData(position: Int) {
        Observable.create<Int> {
            if (position == 8) {
                Thread.sleep(4000)
            } else {
                Thread.sleep(7000)
            }
            it.onNext(100 * Random.nextInt(100))
            it.onComplete()
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("LiveDataAdapter", "getIntData: 发射数据" + it)
                lastVersionLiveData.value = DataBean(position, it)
            }
    }


    data class DataBean(var position: Int = 0, var value: Int? = null) {
    }
}