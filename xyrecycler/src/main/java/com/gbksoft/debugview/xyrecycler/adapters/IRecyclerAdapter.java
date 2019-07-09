package com.gbksoft.debugview.xyrecycler.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.holder.BaseViewHolder;
import com.gbksoft.debugview.xyrecycler.template.models.CellModel;
import com.gbksoft.debugview.xyrecycler.template.models.ColumnHeaderModel;
import com.gbksoft.debugview.xyrecycler.template.models.RowHeaderModel;


public interface IRecyclerAdapter {

    View onCreateCornerView();

    BaseViewHolder onColumnHeaderCreateViewHolder(ViewGroup parent, int viewType);
    void onColumnHeaderBindViewHolder(BaseViewHolder holder, ColumnHeaderModel columnHeaderItemModel, int columnPosition);
    int getColumnHeaderItemViewType(int position);

    BaseViewHolder onRowHeaderCreateViewHolder(ViewGroup parent, int viewType);
    void onRowHeaderBindViewHolder(BaseViewHolder holder, RowHeaderModel rowHeaderItemModel, int rowPosition);
    int getRowHeaderItemViewType(int position);

    BaseViewHolder onCellCreateViewHolder(ViewGroup parent, int viewType);
    void onCellBindViewHolder(BaseViewHolder holder, CellModel cellItemModel, int columnPosition, int rowPosition);
    int getCellItemViewType(int position);
}
