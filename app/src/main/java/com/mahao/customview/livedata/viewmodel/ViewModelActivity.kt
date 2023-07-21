package com.mahao.customview.livedata.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mahao.customview.R

class ViewModelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)

        var model = ViewModelProvider(this).get(HolderViewModel::class.java)
        model.liveData.observe(this, Observer {
            Log.d("LiveDataAdapter", "onCreate: ++++++++ " + it)
        })
        model.getIntData()
    }
}