package com.mahao.customview.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.mahao.customview.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "ItemAdapter";
    LinkedList<String> mStringList = new LinkedList<>();
    private boolean visiableFlag = false;
    private boolean isRemove = false;
    private int removeStart = -1;
    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(final ItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }

    public void setStringList(List<String> stringList) {
        mStringList.clear();
        mStringList.addAll(stringList);
        //     setHasStableIds(true);
    }

    public void setVisiableFlag(boolean flag) {
        this.visiableFlag = flag;
    }

    public void setRemove(boolean remove, int removeStart) {
        isRemove = remove;
        this.removeStart = removeStart;
    }

  /*  @Override
    public long getItemId(int position) {
        return position;
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(view, viewHolder);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.purple_200));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white_1));

        }
        holder.mTvPosition.setText("" + mStringList.get(position));
        /*if(position == 3){
            ViewParent parent = holder.itemView.getParent();
            if( parent instanceof  RecyclerView){
                RecyclerView recyclerView = (RecyclerView) parent;
                recyclerView.removeView(holder.itemView);
                recyclerView.getRecycledViewPool().putRecycledView(holder);
                recyclerView.requestLayout();
            }else{

            }
        }*/
        //  Log.d(TAG, "onBindViewHolder: " + holder.itemView.getMeasuredWidth());
        if (!visiableFlag) {
            holder.itemView.setVisibility(View.INVISIBLE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        //  holder.itemView.setTranslationX(500);
        // holder.itemView.setLeft(500);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        Log.d(TAG, "onBindViewHolder: part----- " + mStringList.get(position));
    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        //    Log.d(TAG, "onViewRecycled: " + holder.getAdapterPosition());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG, "onAttachedToRecyclerView:  adapter on Attach");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        //  Log.d(TAG, "onViewDetachedFromWindow: " + holder.getAdapterPosition());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //  Log.d(TAG, "onViewAttachedToWindow: " + holder.getAdapterPosition());
    }

    public interface ItemClickListener {
        void onClick(View view, ViewHolder holder);
    }
}



