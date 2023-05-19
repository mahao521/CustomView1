package com.mahao.customview.recycler.util;

import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Field;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerPool extends RecyclerView.RecycledViewPool {

    private static final String TAG = "MyRecyclerPool";
    private MyRecyclerView mMyRecyclerView;

    public MyRecyclerPool(MyRecyclerView recyclerView) {
        mMyRecyclerView = recyclerView;
    }

    @Override
    public void putRecycledView(RecyclerView.ViewHolder scrap) {
        Log.d(TAG, "putPool: 存位置   " + scrap.getLayoutPosition());
        super.putRecycledView(scrap);
        final int viewType = scrap.getItemViewType();
        RecyclerView.RecycledViewPool recycledViewPool = mMyRecyclerView.getRecycledViewPool();
        try {
            Field mScrap = recycledViewPool.getClass().getSuperclass().getDeclaredField("mScrap");
            if (!mScrap.isAccessible()) {
                mScrap.setAccessible(true);
            }
            Object pool = mScrap.get(recycledViewPool);
            if (pool != null && pool instanceof SparseArray) {
                SparseArray array = (SparseArray) pool;
                for (int i = 0; i < array.size(); i++) {
                    Object object = array.get(i);
                    Field mScrapHeap = object.getClass().getDeclaredField("mScrapHeap");
                    if (!mScrapHeap.isAccessible()) {
                        mScrapHeap.setAccessible(true);
                    }
                    Object viewholders = mScrapHeap.get(object);
                    if (viewholders != null && viewholders instanceof ArrayList) {
                        ArrayList<RecyclerView.ViewHolder> viewHolderArrayList = (ArrayList<RecyclerView.ViewHolder>) viewholders;
                        Log.d(TAG, "put: pooL大小为  " + viewHolderArrayList.size());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public RecyclerView.ViewHolder getRecycledView(int viewType) {
        RecyclerView.ViewHolder recycledView = super.getRecycledView(viewType);
        if (recycledView != null) {
            Log.d(TAG, "removePool:  找到了" );
        } else {
            Log.d(TAG, "removePool:  没找到" );
        }
        return recycledView;
    }


    @Override
    public void clear() {
        super.clear();
        Log.d(TAG, "clear : ");
    }
}
