package com.mahao.customview.recycler

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.android.material.circularreveal.CircularRevealCompat
import com.mahao.customview.R
import com.mahao.customview.recycler.util.MyRecyclerView
import com.mahao.customview.widget.MyCircleRelativelayout
import java.util.*

class GridViewActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "GridViewActivity"
    lateinit var recyclerView: MyRecyclerView
    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var adapter: ItemAdapter
    lateinit var manager: GridLayoutManager
    lateinit var circleView: MyCircleRelativelayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_view)
        recyclerView = findViewById(R.id.grv)
        btn1 = findViewById(R.id.btn_1)
        btn2 = findViewById(R.id.btn_2)
        circleView = findViewById(R.id.crrl)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        adapter = ItemAdapter()
        adapter.setVisiableFlag(true)
        adapter.setStringList(getStringList1())
        manager = GridLayoutManager(this, 7, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        Log.d(TAG, "onCreate: " + this.resources.displayMetrics.density)  //3.5
        circleView.setCircularRevealOverlayDrawable(resources.getDrawable(R.drawable.item_recycler_drawable))
        circleView.post {
            var animator = CircularRevealCompat.createCircularReveal(circleView, 0f, 0f, 100f, 1700f)
            animator.duration = 3000
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    circleView.setOverViewFlag(false)
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationRepeat(p0: Animator) {

                }


            })
            animator.start()
        }
        //可以通过数据，按照数据的长度，设置size，达到流式布局的效果。
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position > 12 && position % 4 == 0) {
                    return 2
                } else if (position > 7 && position % 5 == 0) {
                    return 3
                }
                return 1
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_1 -> {
                circleView.setCircularRevealOverlayDrawable(resources.getDrawable(R.mipmap.drag2))
                circleView.setCircularRevealScrimColor(resources.getColor(R.color.white_3))
                var animator = CircularRevealCompat.createCircularReveal(circleView, 0f, 0f, 0f, 1700f)
                animator.duration = 3000
                animator.start()
                /*  manager = GridLayoutManager(this, 7, GridLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = manager*/
            }
            R.id.btn_2 -> {
                for (index in 0..recyclerView.childCount - 1) {
                    var childAt = recyclerView.getChildAt(index)
                    var viewHolder1 = recyclerView.getChildViewHolder(childAt)
                    var viewHolder2 = recyclerView.findContainingViewHolder(childAt) as ViewHolder
                    Log.d(TAG, "onClick: " + (viewHolder1.equals(viewHolder2)) + viewHolder1.adapterPosition + " " + viewHolder2?.adapterPosition)
                    viewHolder2.mTvPosition.setText("w=" + manager.getDecoratedMeasuredWidth(childAt))
                }
            }
        }
    }

    private fun getStringList1(): List<String> {
        var list = LinkedList<String>()
        for (i in 0..100) {
            list.add(i.toString())
        }
        Log.d(TAG, "onCreate: " + list.toString())
        return list
    }

}