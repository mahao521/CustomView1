package com.mahao.customview.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mahao.customview.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.Flow

class TransLateActivity : AppCompatActivity() {
    private val TAG = "TransLateActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_late2)
        var btnTransLate = findViewById<Button>(R.id.btn_translate)
        Log.d(TAG, "onCreate: " + btnTransLate.toString())

        runBlocking<Unit> {
            withTimeoutOrNull(250) {
                foo().collect { value ->
                    println(value)
                }
            }
            println("done")
        }
    }

    fun foo() = flow<Int> {
        for (i in 1..3) {
            delay(100)
            println("emitting $i")
            emit(i)
        }
    }


}