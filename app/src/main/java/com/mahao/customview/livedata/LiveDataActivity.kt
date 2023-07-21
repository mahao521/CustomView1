package com.mahao.customview.livedata

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahao.customview.R
import com.mahao.customview.livedata.adapter.LiveDataAdapter
import com.mahao.customview.livedata.adapter.LiveDataAdapter1
import com.mahao.customview.livedata.adapter.LiveDataAdapter2
import com.mahao.customview.livedata.adapter.LiveDataAdapter22
import com.mahao.customview.livedata.adapter.LiveDataAdapter3
import com.mahao.customview.livedata.livedatBus.LiveDataBusActivity
import com.mahao.customview.livedata.livedatBus.LiveDataBusSubActivity

class LiveDataActivity : AppCompatActivity() {

    var rv: RecyclerView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)
        rv = findViewById(R.id.rv_liveData)
        var adapter = LiveDataAdapter2()
        rv?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv?.adapter = adapter
        findViewById<Button>(R.id.btn_live_bus).setOnClickListener {
            var intent = Intent(this, LiveDataBusActivity::class.java);
            startActivity(intent)
        }

    }
}