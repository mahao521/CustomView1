package com.mahao.customview.recycler.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

object RefUtils {

    fun getCacheList(view: View, name: String): ArrayList<RecyclerView.ViewHolder>? {
        val recyclerField = view.javaClass.superclass.getDeclaredField("mRecycler")
        if (!recyclerField.isAccessible) {
            recyclerField.isAccessible = true
        }
        val recycler = recyclerField.get(view)
        val declaredField = recycler.javaClass.getDeclaredField(name)
        if (!declaredField.isAccessible) {
            declaredField.isAccessible = true
        }
        val get = declaredField.get(recycler) as? ArrayList<RecyclerView.ViewHolder>
        return get
    }
}