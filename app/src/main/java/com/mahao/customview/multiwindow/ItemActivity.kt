package com.mahao.customview.multiwindow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.window.java.layout.WindowInfoTrackerCallbackAdapter
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowInfoTrackerDecorator
import androidx.window.layout.WindowLayoutInfo
import com.mahao.customview.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.asFlowable
import kotlin.random.Random

class ItemActivity : AppCompatActivity() {
    private val TAG = "ItemActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        var random = Random.nextInt(100)
        findViewById<TextView>(R.id.tv_center).setText("center position = " + random)
        /* runBlocking {
             coroutineScope{
                 WindowInfoTracker.getOrCreate(this@ItemActivity).windowLayoutInfo(this@ItemActivity)
                     .collect(object : FlowCollector<WindowLayoutInfo> {
                         override suspend fun emit(value: WindowLayoutInfo) {
                             Log.d(TAG, "emit: " + value.toString())
                         }
                     })
             }
         }*/
    }


}