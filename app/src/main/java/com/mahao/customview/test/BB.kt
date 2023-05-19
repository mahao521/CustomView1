package com.mahao.customview.test

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BB(function: () -> Array<Int>) {

    fun main() {
        GlobalScope.launch {

        }
    }

    var flow: Flow<Int> = flowOf(1, 2, 4)
}

class CC {
    var bb: Flow<Int> = BB() {
        arrayOf(1, 2, 3, 4)
    }.flow.map {
        it * it
    }


}