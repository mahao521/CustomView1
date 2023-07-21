package com.mahao.customview.stackmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mahao.customview.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.log

class BActivity : BaseActivity() {

    private val TAG = "BActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bactivity)
        Log.d(TAG, "onCreate:++ ")
        ActivityStackManager.getInstance().print(TAG + "create")
        findViewById<Button>(R.id.btn_B).setOnClickListener {
            startActivity(Intent(this, CActivity::class.java))
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ++")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart:++")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:+++")
        ActivityStackManager.getInstance().print(TAG + "onresume")
        var topActivity = ActivityStackManager.getInstance().topActivity
        Log.d(TAG, "onResume: +x+x+x  " + topActivity)
        var topactivity1 = ActivityStackManager.getInstance().topActivity1;
        Log.d(TAG, "onResume: =-----=  " + topactivity1)

        Observable.create<Int> {
            Thread.sleep(400)
            ActivityStackManager.getInstance().print(TAG + "onresume")
            var topActivity = ActivityStackManager.getInstance().topActivity
            Log.d(TAG, "onResume: +x+x+x  " + topActivity)
            var topactivity1 = ActivityStackManager.getInstance().topActivity1;
            Log.d(TAG, "onResume: =-----=  " + topactivity1)
            it.onNext(1)
            it.onComplete()
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d(TAG, "onResume:  完成")
            }
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause:++ ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop:++ ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy:+++ ")
    }
}