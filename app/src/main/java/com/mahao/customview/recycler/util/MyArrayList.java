package com.mahao.customview.recycler.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyArrayList<T> extends ArrayList<T> {

    private static final String TAG = "myArrayList";
    private String type = "";
    private MyRecyclerView mMyRecyclerView;

    public MyArrayList(String type, MyRecyclerView recyclerView) {
        this.type = type;
        this.mMyRecyclerView = recyclerView;
    }

    @Override
    public boolean add(T o) {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) o;
        //    Log.d(TAG, type + " add:  position = " + viewHolder.getAdapterPosition());
        boolean add = super.add(o);
        getCacheList("mCachedViews");
        getCacheList("mAttachedScrap");
        return add;
    }

    @Override
    public void add(int index, T element) {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) element;
        //   Log.d(TAG, type + " add:  position = " + viewHolder.getAdapterPosition());
        super.add(index, element);
        getCacheList("mCachedViews");
        getCacheList("mAttachedScrap");
    }

    @Override
    public T remove(int index) {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) get(index);
        //  Log.d(TAG, type + " remove: position " + viewHolder.getAdapterPosition());
        T remove = super.remove(index);
        getCacheList("mCachedViews");
        getCacheList("mAttachedScrap");
        return remove;
    }


    @Override
    public void clear() {
        Log.d(TAG, type + "------------clear: ");
        super.clear();
    }

    public void getCacheList(String name) {
        ArrayList<RecyclerView.ViewHolder> mCachedViews = RefUtils.INSTANCE.getCacheList(mMyRecyclerView, name);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < mCachedViews.size(); i++) {
            list.add(mCachedViews.get(i).getLayoutPosition());
        }
        Log.d(TAG, "add:   " + name + "   " + Arrays.toString(list.toArray()));
    }


}

