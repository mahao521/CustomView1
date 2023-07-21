package hy.sohu.com.comm_lib.utils.livedatabus

import android.util.Log
import androidx.lifecycle.*
import java.lang.Exception
import java.lang.NullPointerException
import java.util.HashMap

object LiveDataBus {
    private  val  bus: MutableMap<String, BusMutableLiveData<out BusEvent>>
    //out 表示 参数 extend BusEvent， 参数是子类
    //in  表示 参数 super BusEvent， 参数是父类

    init {
        bus = HashMap()
    }

    /**
     * 必须在非MainThread 调用
     *
     *  * 发送事件，真正使用的时候用法
     *  LiveDataBus.post(xxx.class)
     */
    fun <T:BusEvent> post(eventBus:T) {
        /*check(!AppExecutors.getInstance().isMainThread) {
            "LiveDataBus.post invoke in MainThread !!!"
        }*/
        val canonicalName = eventBus.javaClass.canonicalName!!;
        if (bus.containsKey(canonicalName)) {
            //如果存在创建好的livedata
            (bus[canonicalName] as? BusMutableLiveData<T>)?.postValue(eventBus)
        }
    }

    /**
     * 必须在 MainThread 调用，用set能减少线程切换
     * 发送事件，真正使用的时候用法
     *  LiveDataBus.set(xxx.class)
     */
    fun <T:BusEvent> set(eventBus:T) {
        val canonicalName = eventBus.javaClass.canonicalName!!;
        if (bus.containsKey(canonicalName)) {
            //如果存在创建好的livedata
            (bus[canonicalName] as? BusMutableLiveData<T>)?.setValue(eventBus)
        }
    }

    fun <T:BusEvent> get(clazz: Class<T>): BusMutableLiveData<T> {
        val canonicalName = clazz.canonicalName!!;
        if (!bus.containsKey(canonicalName)) {
            bus[canonicalName] = BusMutableLiveData<T>(canonicalName)
        }
        return bus[canonicalName] as BusMutableLiveData<T>
    }

    /**
     * 不指定线程的时候，默认为MainThread
     */
    private class ObserverThreadModeWrapper<T>(@BusThreadMode var threadMode : Int = 0, var observer: Observer<T>) : Observer<T> {
        override fun onChanged(value: T) {// 此方法 已经被LiveData 切换到UI线程 执行了
            interceptIfNeedSwitchThread(value)
        }
        /**
         * 切换线程
         */
        private fun interceptIfNeedSwitchThread(value: T) =
                when (threadMode) {
                    BusThreadMode.NEW_THREAD -> {//子线程
                     //   AppExecutors.getInstance().diskIO().execute({ observer.onChanged(value)})
                    }
                    else -> { // 默认是 MAIN_THREAD
                        observer.onChanged(value)
                    }
                }
    }


    private class ObserverCodeWrapper<T>(var code:Int = 0,var observer: Observer<T>): Observer<T>{

        override fun onChanged(t: T) {
            check(t is CodeBusEvent){
                "your event must extend CodeBusEvent"
            }
            if (t.code == code) {
                observer.onChanged(t)
            }
        }
    }


    private class ObserverForeverWrapper<T>(observer: Observer<T>) : Observer<T> {
        private val observer: Observer<T>

        init {
            this.observer = observer
        }

        override fun onChanged(t: T) {
            if (observer != null) {
                if (isCallOnObserve) {
                    return
                }
                observer.onChanged(t)
            }
        }

        /**
         * 通过observeForever方法 注册的obsever，会被立即回调一次onChange。
         * 但这不符合事件bus的场景，会在onChanged中跳过这一次回调。
         */
        private val isCallOnObserve: Boolean
            private get() {
                val stackTrace = Thread.currentThread().stackTrace
                if (stackTrace != null && stackTrace.size > 0) {
                    for (element in stackTrace) {
                        if ("androidx.lifecycle.LiveData" == element.className && "observeForever" == element.methodName) {
                            return true
                        }
                    }
                }
                return false
            }
    }

    class BusMutableLiveData<T>(var key :String) : MutableLiveData<T>() {

        fun observe(owner: LifecycleOwner, observer: Observer<in T>, @BusThreadMode threadMode: Int){
            observe(owner, ObserverThreadModeWrapper(threadMode,observer))
        }

        fun observe(code: Int,owner: LifecycleOwner, observer: Observer<in T>, @BusThreadMode threadMode: Int){
            observe(owner,ObserverCodeWrapper(code,ObserverThreadModeWrapper(threadMode,observer)))
        }

        fun observe(code: Int,owner: LifecycleOwner, observer: Observer<in T>){
            observe(owner,ObserverCodeWrapper(code,observer))
        }

        /**
         * 需要手动 removeObserver
         */
        fun observeForever(code: Int,observer: Observer<in T>, @BusThreadMode threadMode: Int){
            observeForever(ObserverCodeWrapper(code,ObserverThreadModeWrapper(threadMode,observer)))
        }

        /**
         * 需要手动 removeObserver
         */
        fun observeForever(code: Int,observer: Observer<in T>){
            observeForever(ObserverCodeWrapper(code,observer))
        }
        /**
         * 需要手动 removeObserver
         */
        fun observeForever(observer: Observer<in T>, @BusThreadMode threadMode: Int){
            observeForever(ObserverThreadModeWrapper(threadMode,observer))
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
           initMversion(observer)
            super.observe(owner, observer)
            try {
              //  hook(observer)
            } catch (e: Exception) {
            }
        }

        /**
         * 需要手动 removeObserver
         */
        override fun observeForever(observer: Observer<in T>) {
            super.observeForever(ObserverForeverWrapper(observer))
        }

        /**
         * 对于observeForever的观察者， 需要手动 removeObserver
         */
        override fun removeObserver(observer: Observer<in T>) {
            super.removeObserver(observer)
            //如果当前key所对应的LiveData都没有监听者的时候，移除当前的key
            if(!this.hasObservers() && bus.containsKey(key)){
                bus.remove(key)
            }
        }


        private fun hook(observer: Observer<in T>) {
            //get wrapper's version
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
                throw NullPointerException("Wrapper can not be bull!")
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
        }


        fun initMversion(observer: Observer<in T>) {
            val fieldVersion = LiveData::class.java.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion[this]
            //set wrapper's version
            fieldVersion[this] = -1
            Log.d("LiveDataAdapter", "hook: " + objectVersion)
        }


    }
}