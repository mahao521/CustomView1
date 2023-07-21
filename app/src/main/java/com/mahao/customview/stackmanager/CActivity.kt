package com.mahao.customview.stackmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mahao.customview.R

class CActivity : BaseActivity() {

    private val TAG = "CActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)
        Log.d(TAG, "onCreate: -------- ")
        ActivityStackManager.getInstance().print(TAG + "create")
        findViewById<Button>(R.id.btn_c).setOnClickListener {
            startActivity(Intent(this, AActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: -------- ")
        ActivityStackManager.getInstance().print(TAG + "onresume")
        var topActivity = ActivityStackManager.getInstance().topActivity
        Log.d(TAG, "onStart: +x+x+x  " + topActivity)
        var topactivity1 = ActivityStackManager.getInstance().topActivity1;
        Log.d(TAG, "onStart: =-----=  " + topactivity1)
        Thread.sleep(200)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:--------  ")
        ActivityStackManager.getInstance().print(TAG + "onresume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause:-------- ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop:-------- ")
        ActivityStackManager.getInstance().print(TAG + "onresume")
        var topActivity = ActivityStackManager.getInstance().topActivity
        Log.d(TAG, "onStop: +x+x+x  " + topActivity)
        var topactivity1 = ActivityStackManager.getInstance().topActivity1;
        Log.d(TAG, "onStop: =-----=  " + topactivity1)
        Thread.sleep(2000)
    }

    override fun onDestroy() {
        Thread.sleep(2000)
        super.onDestroy()
        Log.d(TAG, "onDestroy: ------- ")
    }
}