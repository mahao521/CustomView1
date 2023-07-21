package com.mahao.customview.glide.second

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key
import com.mahao.customview.R
import java.lang.ref.WeakReference

class GlideRecyclerViewActivity : AppCompatActivity() {

    private val TAG = "GlideRecyclerViewActivi"

    var rvCache: RecyclerView? = null

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_recycler_view)
        rvCache = findViewById<RecyclerView>(R.id.rv_cache)
        rvCache?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCache?.adapter = GlideAdapter()

        rvCache?.postDelayed({
            printActivityLog()
        }, 1000)

    }

    var oldSize = 0;
    @RequiresApi(Build.VERSION_CODES.N)
    fun printActivityLog() {
        var glide = Glide.get(this)
        var declaredField = Glide::class.java.getDeclaredField("engine")
        if (!declaredField.isAccessible) {
            declaredField.isAccessible = true
        }
        var engine = declaredField.get(glide)
        var activityResource = engine.javaClass.getDeclaredField("activeResources")
        if (!activityResource.isAccessible) {
            activityResource.isAccessible = true
        }
        var activityResources = activityResource.get(engine)
        var mapResourceField = activityResources.javaClass.getDeclaredField("activeEngineResources")
        if(!mapResourceField.isAccessible){
            mapResourceField.isAccessible = true
        }
        var map = mapResourceField.get(activityResources)
        if (map is Map<*, *>) {
            if( oldSize != map.size){
                oldSize = map.size
                map.forEach { any, u ->
                    if (u is WeakReference<*>) {
                        Log.d(TAG, "printActivityLog: " + "  " + map.size + " " + u.get())
                    }
                }
            }
        }
        rvCache?.postDelayed({
            printActivityLog()
        }, 1000)

    }

    override fun onDestroy() {
        super.onDestroy()
        rvCache?.handler?.removeCallbacksAndMessages(null)
    }

}