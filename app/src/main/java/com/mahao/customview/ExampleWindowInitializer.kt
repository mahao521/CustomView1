package com.mahao.customview

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import androidx.window.embedding.SplitController

class ExampleWindowInitializer : Initializer<SplitController> {
    private  val TAG = "ExampleWindowInitialize"
    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return arrayListOf()
    }

    override fun create(context: Context): SplitController {
        SplitController.initialize(context,R.xml.multi_window)
        var splitRules = SplitController.getInstance().getSplitRules()
        Log.d(TAG, "create: " + splitRules.size)
        return SplitController.getInstance()
    }
}