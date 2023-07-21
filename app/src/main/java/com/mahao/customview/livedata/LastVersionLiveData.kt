package com.mahao.customview.livedata

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.NullPointerException

class LastVersionLiveData<T> : MutableLiveData<T>() {


    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        // hook(observer)
    //    initMversion(observer)
        super.observe(owner, observer)
        // hook(observer)
    }


    //这个方法一定是在onCreate执行的，是因为onCreate的时候，不会走Livedata的onChange方法。
    //但是onStart或者onResume会调用liveData的onChange方法。
    //所以此时修改lastVersion ，之后调用就能拦截
    //更好的做法是，无论何时调用，都可以让他有效，即使onResume，那么需要super之前记录mVersion。
    //然后设置为-1,super之后再恢复这个值，并把lastversion也设置和mversion一样。
    private fun hook(observer: Observer<in T>) {
        //get wrapper's version
        Log.d("LiveDataAdapter", "hook: 开始  ")
        val classLiveData = LiveData::class.java
        val fieldObservers = classLiveData.getDeclaredField("mObservers")
        fieldObservers.isAccessible = true
        val objectObservers = fieldObservers[this]
        val classObservers: Class<*> = objectObservers.javaClass
        val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
        methodGet.isAccessible = true
        val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
        var objectWrapper: Any? = null
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = objectWrapperEntry.value
        }
        if (objectWrapper == null) {

            return
            //throw NullPointerException("Wrapper can not be bull!")
        }
        val classObserverWrapper: Class<*> = objectWrapper.javaClass.superclass
        val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
        fieldLastVersion.isAccessible = true
        //get livedata's version
        val fieldVersion = classLiveData.getDeclaredField("mVersion")
        fieldVersion.isAccessible = true
        val objectVersion = fieldVersion[this]
        //set wrapper's version
        fieldLastVersion[objectWrapper] = objectVersion
        Log.d("LiveDataAdapter", "hook: " + objectVersion)
    }

    //这个是为了解决。同一个livedata，新创建的holder，立刻调用了onChanged方法，
    //再observer之前把这个值改成-1 之后比较version就被拦截了。
    //这么做的弊端就是 把livedata版本改小了，比如之前已经在LiveDataBusActivity的version为1，那么你再其他页面post就
    //接收不到消息了
    fun initMversion(observer: Observer<in T>) {
        //get wrapper's version
        /*val classLiveData = LiveData::class.java
        val fieldObservers = classLiveData.getDeclaredField("mObservers")
        fieldObservers.isAccessible = true
        val objectObservers = fieldObservers[this]
        val classObservers: Class<*> = objectObservers.javaClass
        val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
        methodGet.isAccessible = true
        val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
        var objectWrapper: Any? = null
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = objectWrapperEntry.value
        }
        if (objectWrapper == null) {
            return
            //throw NullPointerException("Wrapper can not be bull!")
        }
        val classObserverWrapper: Class<*> = objectWrapper.javaClass.superclass*/
        // val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
        //  fieldLastVersion.isAccessible = true
        //get livedata's version
        val fieldVersion = LiveData::class.java.getDeclaredField("mVersion")
        fieldVersion.isAccessible = true
        val objectVersion = fieldVersion[this]
        //set wrapper's version
        fieldVersion[this] = -1
        Log.d("LiveDataAdapter", "hook: " + objectVersion + " " + fieldVersion.get(this));

    }
}