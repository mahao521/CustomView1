package com.mahao.customview.fragment.viewpager2

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mahao.customview.R

class PagerAdapter : RecyclerView.Adapter<PagerAdapter.ViewHolder>() {

    private val TAG = "PagerAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager2, parent, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: " + position)
        holder.tvPosition?.setText("item = " + position)
        holder.tvPosition?.setOnClickListener {
            if (holder.itemView.parent is RecyclerView) {
                var childCount = (holder.itemView?.parent as RecyclerView)?.childCount
                holder.tvPosition?.setText("item = " + position + " --childCount " + childCount)
                var intent = Intent(holder.itemView.context, TestViewPagerActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvPosition: TextView? = null

        init {
            tvPosition = this.itemView.findViewById<TextView>(R.id.tv_view_pager2);
        }
    }
}