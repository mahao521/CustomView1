package com.mahao.customview.appbarlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.mahao.customview.R
import com.mahao.customview.appbarlayout.adapter.AppbarAdapter

class AppbarLayout2Activity : AppCompatActivity() {

    private val TAG = "AppBarLayoutActivity"
    private var appBar: AppBarLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_layout)
        var recyclerView = findViewById<RecyclerView>(R.id.rv_app_bar)
        var llHeader = findViewById<LinearLayout>(R.id.ll_header)
        var ivHeader = findViewById<ImageView>(R.id.iv_header)
        // ivHeader.adjustViewBounds = true
        //  var dodgeView = findViewById<TextView>(R.id.dodgeView)
        var coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator)
        //    var tvKeyline = findViewById<TextView>(R.id.tv_keyline)
        //  var collapsLayout = findViewById<CollapsingToolbarLayout>(R.id.collaps_layout);
        appBar = findViewById(R.id.app_bar)
        //  var toolbar = findViewById<Toolbar>(R.id.toolbar)
        resources.getIntArray(R.array.key_line_1)
        var layoutParams = recyclerView.layoutParams as CoordinatorLayout.LayoutParams
        /* dodgeView.setOnClickListener {
             (layoutParams.behavior as AppBarLayout.ScrollingViewBehavior).overlayTop = DisplayUtil.dp2Px(this,100f)
         }*/
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = AppbarAdapter()
        var params = llHeader.layoutParams as AppBarLayout.LayoutParams
        params.setScrollInterpolator {
            Log.d(TAG, "onCreate:  " + it);
            it
        }
        appBar?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                Log.d(TAG, "onOffsetChanged: " + verticalOffset)
                //       var alpha : Float = Math.abs(verticalOffset)*1.0f / (appBar!!.measuredHeight- collapsLayout.minimumHeight)
                //      toolbar.alpha = alpha
            }
        })
        // supportFragmentManager.beginTransaction().add(R.id.fragment_container, BackpressedFragment()).commit()
        Log.d(TAG, "onCreate:  activity")
    }
}