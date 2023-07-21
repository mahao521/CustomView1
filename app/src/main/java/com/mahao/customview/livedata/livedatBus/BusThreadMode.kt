package hy.sohu.com.comm_lib.utils.livedatabus

import androidx.annotation.IntDef

@MustBeDocumented
@IntDef(BusThreadMode.NEW_THREAD, BusThreadMode.MAIN_THREAD)
//表示注解作用范围，参数注解，成员注解，方法注解
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
//表示注解所存活的时间在运行时
@Retention(AnnotationRetention.RUNTIME)
annotation class BusThreadMode {
    companion object {
        /**
         * 子线程
         */
        const val NEW_THREAD = 1

        /**
         * UI线程, 默认Observer onchange执行线程
         */
        const val MAIN_THREAD = 2


    }
}