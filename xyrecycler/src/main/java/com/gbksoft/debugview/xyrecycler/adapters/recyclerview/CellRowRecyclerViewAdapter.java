package com.gbksoft.debugview.xyrecycler.adapters.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.holder.BaseViewHolder;
import com.gbksoft.debugview.xyrecycler.template.models.CellModel;


public class CellRowRecyclerViewAdapter<C> extends BaseRecyclerViewAdapter<C> {

    private int yPosition;

    public CellRowRecyclerViewAdapter(Context context, IXYRecycler xyRecycler) {
        super(context, null);
        recyclerAdapter = xyRecycler.getAdapter();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return recyclerAdapter.onCellCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int xPosition) {
        recyclerAdapter.onCellBindViewHolder(holder, (CellModel) getItem(xPosition), xPosition, yPosition);
    }

    public void setYPosition(int rowPosition) {
        yPosition = rowPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return recyclerAdapter.getCellItemViewType(position);
    }

    @Override
    public boolean onFailedToRecycleView(BaseViewHolder holder) {
        return holder.onFailedToRecycleView();
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}
