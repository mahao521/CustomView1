package com.mahao.customview.livedata.livedatBus

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.mahao.customview.R
import hy.sohu.com.comm_lib.utils.livedatabus.BusEvent
import hy.sohu.com.comm_lib.utils.livedatabus.LiveDataBus

class LiveDataBusActivity : AppCompatActivity() {

    private val TAG = "LiveDataBusActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_bus)
        var tvSend = findViewById<TextView>(R.id.tv_send_message)
        var tvReceive = findViewById<TextView>(R.id.tv_receive_message)
        tvSend.setOnClickListener {
            LiveDataBus.post(MessageEvent("李四"))
        }
        LiveDataBus.get(MessageEvent::class.java).observe(this) {
            Log.d(TAG, "receive: " + it.name)
            tvReceive.setText("接收到消息 " + it.name)
        }

        tvReceive.setOnClickListener {
            var intent = Intent(this, LiveDataBusSubActivity::class.java);
            startActivity(intent)
        }
    }

    class MessageEvent : BusEvent {
        var name: String = ""

        constructor(name: String) {
            this.name = name
        }
    }
}