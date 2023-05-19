package com.mahao.customview.dispatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import com.mahao.customview.R

class DispatchActivity : AppCompatActivity() {
    private val TAG = "DispatchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch)
        findViewById<Button>(R.id.btn_dispatch).setOnClickListener {

        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "dispatchTouchEvent: " + ev?.action)
        return super.dispatchTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: ")
        return super.onTouchEvent(event)
    }
}