package com.mahao.customview.glide.second

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.ImageViewTarget
import com.mahao.customview.R
import com.mahao.customview.glide.GlideActivity

class GlideAdapter : RecyclerView.Adapter<GlideAdapter.ViewHolder>() {

    private val TAG = "GlideRecyclerViewActivi"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cache_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return GlideActivity.getList().size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setTag(position)
        Glide.with(holder.itemView.context)
            .load(GlideActivity.getList().get(position))
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            /*.into(object : ImageViewTarget<Drawable>(holder.ivGlide) {
                override fun setResource(resource: Drawable?) {
                    //      Log.d(TAG, "setResource:  finished")
                    holder.ivGlide?.setImageDrawable(resource)
                }
            })*/
            .into(holder.ivGlide!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivGlide: ImageView? = null

        init {
            ivGlide = itemView.findViewById(R.id.iv_glide_show)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d(TAG, "onViewAttachedToWindow:---- " + holder.itemView.getTag())
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d(TAG, "onViewDetachedFromWindow:===== " + holder.itemView.getTag())
    }
}