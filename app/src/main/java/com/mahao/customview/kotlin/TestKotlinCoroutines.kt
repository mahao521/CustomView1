package com.mahao.customview.kotlin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TestKotlinCoroutines {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            GlobalScope.launch {
                println("code run coroutine scope")
                delay(1500)
                println("code run coroutine finish")
            }
            runBlocking {
                println("code run coroutine scope---run")
              //  delay(1000)
                println("code run coroutine finish---run")
            }
            Thread.sleep(1000)
        }

    }


}