package com.mahao.customview.livedata.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahao.customview.R;
import com.mahao.customview.livedata.LifeLiveData;
import com.mahao.customview.livedata.viewmodel.HolderViewModel;
import com.mahao.customview.livedata.viewmodel.ViewModelActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

//将observer放在了onClick中。
public class LiveDataAdapter3 extends RecyclerView.Adapter<LiveDataAdapter3.ViewHolder> {

    private static final String TAG = "LiveDataAdapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_live_data_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setTag(position);
        holder.mTextView.setText("position = " + position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        LifeLiveData mLiveData;
        private Observer<Integer> mObserver;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_live_data);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTextView.getText().toString().contains("position = 4")) {
                        Intent intent = new Intent(itemView.getContext(), ViewModelActivity.class);
                        itemView.getContext().startActivity(intent);
                        return;
                    }
                    ViewModelStoreOwner storeOwner = (ViewModelStoreOwner) itemView.getContext();
                    HolderViewModel model = new ViewModelProvider(storeOwner).get(HolderViewModel.class);
                    model.getIntData(mLiveData);
                }
            });
        }

        public void initHolder() {
            mObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    Log.d(TAG, "onChanged: " + integer);
                    mTextView.setText("change = " + integer);
                }
            };
            LifecycleOwner lifecycleOwner = (LifecycleOwner) itemView.getContext();
            if (mLiveData != null) {
                boolean b = mLiveData.hasActiveObservers();
         //       Log.d(TAG, "onClick: " + b);
                //解决item被点击多次，上一次注册的observer没有取消掉注册。
                mLiveData.removeObservers(lifecycleOwner);
            }
            //解决掉每一次lastVersion小于mVersion的问题。
            mLiveData = new LifeLiveData();
         //   Log.d(TAG, "onClick:------------------ 被点击了");
            // mLiveData.removeObservers(lifecycleOwner);
            mLiveData.observe(lifecycleOwner, mObserver);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.initHolder();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d(TAG, "onViewDetached  hodler " + holder.mTextView.getTag() + "加入缓存");
      /*  ViewModelStoreOwner storeOwner = (ViewModelStoreOwner) holder.itemView.getContext();
        HolderViewModel model = new ViewModelProvider(storeOwner).get(HolderViewModel.class);
        LifecycleOwner lifecycleOwner = (LifecycleOwner) holder.itemView.getContext();
        model.getLiveData().removeObservers(lifecycleOwner);*/
        LifecycleOwner lifecycleOwner = (LifecycleOwner) holder.itemView.getContext();
        ViewHolder viewHolder = ((ViewHolder) holder);
        if (viewHolder != null && viewHolder.mLiveData != null) {
          //  viewHolder.mLiveData.removeObservers(lifecycleOwner);
            viewHolder.mLiveData.removeObserver(holder.mObserver);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(TAG, "onViewRecycled:////缓存池   " + holder.mTextView.getTag());
    }

}
