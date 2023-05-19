package com.mahao.customview.multiwindow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.core.util.Consumer
import androidx.window.embedding.SplitController
import androidx.window.embedding.SplitInfo
import com.mahao.customview.R
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ListActivity : AppCompatActivity() {
    private val TAG = "ListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<TextView>(R.id.tv_list).setOnClickListener {
            this.startActivity(Intent(this, DetailActivity::class.java))
        }
        Log.d(TAG, "onCreate: " + SplitController.getInstance().isSplitSupported())

        SplitController.getInstance()
            .addSplitListener(
                this, SplitExecutor()
            ) {
                if (it.size > 0) {
                    it.forEach {
                        Log.d(
                            TAG, "onCreate: " + it.toString()
                        )
                    }
                    findViewById<TextView>(R.id.tv_list).setText("List分屏中")
                }
            }
    }


    inner class SplitExecutor : Executor {
        override fun execute(command: Runnable?) {
            //  runOnUiThread(command)
            Log.d(TAG, "execute:-------- ")
        }
    }
}