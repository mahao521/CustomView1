package com.mahao.customview.livedata.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahao.customview.R;
import com.mahao.customview.livedata.LastVersionLiveData;
import com.mahao.customview.livedata.viewmodel.HolderViewModel;
import com.mahao.customview.livedata.viewmodel.ViewModelActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

//使用修改lastVersion
public class LiveDataAdapter2 extends RecyclerView.Adapter<LiveDataAdapter2.ViewHolder> {

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
        private Observer<HolderViewModel.DataBean> mObserver;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_live_data);
           /* mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTextView.getText().toString().contains("position = 4")) {
                        Intent intent = new Intent(itemView.getContext(), ViewModelActivity.class);
                        itemView.getContext().startActivity(intent);
                        return;
                    }
                    Log.d(TAG, "onClick:------------------ 被点击了");
                    ViewModelStoreOwner storeOwner = (ViewModelStoreOwner) v.getContext();
                    LifecycleOwner lifecycleOwner = (LifecycleOwner) v.getContext();
                    HolderViewModel model = new ViewModelProvider(storeOwner).get(HolderViewModel.class);
                    model.getLiveData().removeObservers(lifecycleOwner);
                    model.getLiveData().observe(lifecycleOwner, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            Log.d(TAG, "onChanged: " + integer);
                            mTextView.setText("change = " + integer);
                        }
                    });
                    model.getIntData(mLiveData);
                }
            });*/

            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTextView.getText().toString().contains("position = 4")) {
                        Intent intent = new Intent(itemView.getContext(), ViewModelActivity.class);
                        itemView.getContext().startActivity(intent);
                        return;
                    }

                    LifecycleOwner lifecycleOwner = (LifecycleOwner) itemView.getContext();
                    ViewModelStoreOwner storeOwner = (ViewModelStoreOwner) itemView.getContext();
                    HolderViewModel model = new ViewModelProvider(storeOwner).get(HolderViewModel.class);

                    if (mObserver == null) {
                        //  model.getLastVersionLiveData().removeObserver(mObserver);
                        mObserver = new Observer<HolderViewModel.DataBean>() {
                            @Override
                            public void onChanged(HolderViewModel.DataBean dataBean) {
                                Log.d(TAG, "onChanged: " + dataBean.getValue() + "   " + mTextView.getTag());
                                int tag = (int) mTextView.getTag();
                                if (dataBean.getPosition() == tag) {
                                    mTextView.setText("change = " + dataBean.getValue());
                                }
                            }
                        };
                    }
                    Log.d(TAG, "onClick:------------------ 被点击了" + mTextView.getTag() + " " + mObserver.toString());
                    LastVersionLiveData<HolderViewModel.DataBean> liveData = model.getLastVersionLiveData();
                    liveData.observe(lifecycleOwner, mObserver);
                    model.getLastVersionIntData((Integer) mTextView.getTag());
                }
            });

        }

        public void initHolder() {
            LifecycleOwner lifecycleOwner = (LifecycleOwner) itemView.getContext();
            ViewModelStoreOwner storeOwner = (ViewModelStoreOwner) itemView.getContext();
            HolderViewModel model = new ViewModelProvider(storeOwner).get(HolderViewModel.class);
            Log.d(TAG, "initHolder: ");


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
        ViewModelStoreOwner storeOwner = (ViewModelStoreOwner) holder.itemView.getContext();
        HolderViewModel model = new ViewModelProvider(storeOwner).get(HolderViewModel.class);
        LifecycleOwner lifecycleOwner = (LifecycleOwner) holder.itemView.getContext();
        model.getLastVersionLiveData().removeObserver(holder.mObserver);
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

    private void setLastVersion(Observer observer, LiveData liveData) {
        Class classLiveData = LiveData.class;
        Field fieldObservers = null;
        try {
            fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object objectObservers = fieldObservers.get(liveData);
            Class classObservers = objectObservers.getClass();
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry<?, ?>) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                return;
            }
            Class classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
            fieldLastVersion.setAccessible(true);
            Field fieldVersion = classLiveData.getDeclaredField("mVersion");
            fieldVersion.setAccessible(true);
            Object objectVersion = fieldVersion.get(liveData);
            fieldLastVersion.set(objectWrapper, objectVersion);
            Log.d(TAG, "setLastVersion: " + objectVersion);
        } catch (Exception e) {
            Log.d(TAG, "setLastVersion: " + e.toString());
            throw new RuntimeException(e);
        }

    }

}
