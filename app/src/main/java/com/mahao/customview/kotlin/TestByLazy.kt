package com.mahao.customview.kotlin

import com.google.errorprone.annotations.concurrent.LazyInit
import kotlin.reflect.KProperty

class TestByLazy {
    val data by MyLazy{ 1 }

}

class MyLazy<T>(init: () -> T) {

    companion object {
        val UN_INIT_VALUE = Object()
    }

    private var init: (() -> T)? = init

    private var value: Any? = UN_INIT_VALUE


    operator fun getValue(thisRef: Any?, prperty: KProperty<*>): T {
        if (value == UN_INIT_VALUE) {
            value = init?.invoke()
            init = null
        }
        return value as T
    }


}













