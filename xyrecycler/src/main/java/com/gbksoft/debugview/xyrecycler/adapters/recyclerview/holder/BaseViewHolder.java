package com.gbksoft.debugview.xyrecycler.adapters.recyclerview.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void onViewRecycled() {
    }

    public boolean onFailedToRecycleView() {
        return false;
    }
}
