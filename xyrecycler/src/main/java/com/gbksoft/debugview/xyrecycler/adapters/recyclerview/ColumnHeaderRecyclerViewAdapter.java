package com.gbksoft.debugview.xyrecycler.adapters.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.gbksoft.debugview.xyrecycler.adapters.IRecyclerAdapter;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.holder.BaseViewHolder;
import com.gbksoft.debugview.xyrecycler.template.models.ColumnHeaderModel;

import java.util.List;


public class ColumnHeaderRecyclerViewAdapter<CH> extends BaseRecyclerViewAdapter<CH> {

    public ColumnHeaderRecyclerViewAdapter(Context context, List<CH> items, IRecyclerAdapter recyclerAdapter) {
        super(context, items);
        this.recyclerAdapter = recyclerAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return recyclerAdapter.onColumnHeaderCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        recyclerAdapter.onColumnHeaderBindViewHolder(holder, (ColumnHeaderModel) getItem(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return recyclerAdapter.getColumnHeaderItemViewType(position);
    }
}
