package com.mahao.customview.fragment.viewpager2

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mahao.customview.fragment.OneFragment

class FragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 10
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = getFragment(position)
        return fragment
    }

    fun getFragment(position: Int): Fragment {
        var oneFragment = OneFragment.newInstance(position.toString(), "");
        return oneFragment
    }

}