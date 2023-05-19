package com.mahao.customview.recycler;

import android.view.View;
import android.widget.TextView;

import com.mahao.customview.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTvPosition;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mTvPosition = itemView.findViewById(R.id.tv_recycler_position);
    }
}