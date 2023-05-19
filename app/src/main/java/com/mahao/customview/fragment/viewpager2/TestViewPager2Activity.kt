package com.mahao.customview.fragment.viewpager2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mahao.customview.R

class TestViewPager2Activity : AppCompatActivity() {

    private val TAG = "TestViewPager2Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view_pager2)
        var viewPager = findViewById<ViewPager2>(R.id.view_pager2)
        viewPager.adapter = PagerAdapter()
     //   viewPager.offscreenPageLimit = 2
        // var recyclerView1 = viewPager.getChildAt(0) as RecyclerView
        // recyclerView1.setItemViewCacheSize(2)
          viewPager.adapter = FragmentPagerAdapter(this.supportFragmentManager, lifecycle)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.d(
                    TAG,
                    "onPageScrolled: " + position + "   ======  " + positionOffset + " ------- " + positionOffsetPixels
                )
                var childAt = viewPager.getChildAt(0)
                if (childAt is RecyclerView) {
                    var recyclerView = childAt
                    var linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    var findFirstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    var firstItemView =
                        linearLayoutManager.findViewByPosition(findFirstVisibleItemPosition)
                    if (firstItemView != null) {
                        Log.d(
                            TAG,
                            "onPageScrolled:  left-- " + firstItemView.left + " --top--- " + firstItemView.top
                        )
                        //   Log.d(TAG, "onPageScrolled: ------ " + position )
                    }
                }
            }
        })
    }
}