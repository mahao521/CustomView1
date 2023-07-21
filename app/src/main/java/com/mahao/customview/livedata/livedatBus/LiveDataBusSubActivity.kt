package com.mahao.customview.livedata.livedatBus

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mahao.customview.R
import hy.sohu.com.comm_lib.utils.livedatabus.LiveDataBus

class LiveDataBusSubActivity : AppCompatActivity() {

    private val TAG = "LiveDataBusActivity2222"

    lateinit  var btnReceive : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_bus_sub)
        Log.d(TAG, "onCreate: ")
        var btnSend = findViewById<Button>(R.id.btn_send_message)
         btnReceive = findViewById<Button>(R.id.btn_receive_message)


        btnSend.setOnClickListener {
            LiveDataBus.post(LiveDataBusActivity.MessageEvent("张三"))
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        super.onResume()
        LiveDataBus.get(LiveDataBusActivity.MessageEvent::class.java).observe(this) {
            Log.d(TAG, "receive: " + it.name)
            btnReceive.setText("接收到消息 " + it.name)
        }
    }
}