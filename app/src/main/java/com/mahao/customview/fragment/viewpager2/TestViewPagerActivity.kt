package com.mahao.customview.fragment.viewpager2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.RelativeLayout.ALIGN_PARENT_LEFT
import android.widget.RelativeLayout.CENTER_IN_PARENT
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mahao.customview.R
import com.mahao.customview.widget.CpHyRecyclerViewLayout
import com.mahao.customview.widget.VerticalViewPager

class TestViewPagerActivity : AppCompatActivity() {

    var viewPager: VerticalViewPager? = null
    var viewpagerDataList: MutableList<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view_pager)
        initData()
        viewPager = findViewById(R.id.viewpager1)
        viewPager?.adapter = ViewpPager1Adapter()

    }

    private fun initData() {
        if (viewpagerDataList == null) {
            viewpagerDataList = mutableListOf();
        }
        for (index in 0 until 10) {
           var cpLayout =  CpHyRecyclerViewLayout(this)
            var recyclerView = cpLayout.getRecyclerView()
            recyclerView?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerView?.adapter = RvAdapter()
            viewpagerDataList?.add(cpLayout)
        }

    }

    inner class ViewpPager1Adapter : PagerAdapter() {

        override fun getCount(): Int {
            return viewpagerDataList?.size ?: 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view.equals(`object`)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var itemView = viewpagerDataList?.get(position)!!
            container.addView(itemView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
         //   super.destroyItem(container, position, `object`)
            container.removeView(`object` as View)
        }
    }

    class RvAdapter : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_pager1_rv, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvPosition?.setText("当前位置 " + position)
            ( holder.tvPosition?.layoutParams as RelativeLayout.LayoutParams).addRule(
                CENTER_IN_PARENT)
            if(position == itemCount - 1){
                ( holder.tvPosition?.parent as View).setBackgroundColor(holder.itemView.context.resources.getColor(R.color.black))
            }else if(position % 2 == 0){
                ( holder.tvPosition?.parent as View).setBackgroundColor(holder.itemView.context.resources.getColor(R.color.white_1))
            }else{
                ( holder.tvPosition?.parent as View).setBackgroundColor(holder.itemView.context.resources.getColor(R.color.white_4))
            }

        }

        override fun getItemCount(): Int {
            return 15
        }

        class ViewHolder : RecyclerView.ViewHolder {

            var tvPosition: TextView? = null

            constructor(itemView: View) : super(itemView) {
                tvPosition = itemView.findViewById(R.id.tv_view_pager1)
            }
        }
    }


}