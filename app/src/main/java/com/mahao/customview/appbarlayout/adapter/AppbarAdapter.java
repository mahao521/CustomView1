package com.mahao.customview.appbarlayout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahao.customview.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppbarAdapter extends RecyclerView.Adapter<AppbarAdapter.ViewHolder> {

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_bar_layout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAppBar.setText("number = " + position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListener(holder.getAdapterPosition(), holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 300;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAppBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppBar = itemView.findViewById(R.id.tv_app_bar);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, ViewHolder viewHolder);
    }
}
