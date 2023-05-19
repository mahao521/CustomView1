package com.mahao.customview.drag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahao.customview.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DragAdapter extends RecyclerView.Adapter<DragAdapter.ViewHolder> {

    private List<String> dataList;
    private View.OnClickListener mOnClickListener;

    public void setData(List<String> list) {
        this.dataList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_drag, parent, false);
        return new ViewHolder(inflate);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setTag(position);
        if (position < dataList.size()) {
            holder.mTextView.setText(dataList.get(position));
        } else {
            holder.mTextView.setText("number = " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_number);
            mTextView.setOnClickListener(mOnClickListener);
        }
    }
}
