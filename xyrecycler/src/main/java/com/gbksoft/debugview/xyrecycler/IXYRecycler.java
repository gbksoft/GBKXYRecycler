package com.gbksoft.debugview.xyrecycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gbksoft.debugview.xyrecycler.adapters.BaseRecyclerAdapter;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerView;
import com.gbksoft.debugview.xyrecycler.handlers.ScrollHandler;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.CellLayoutManager;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.ColumnHeaderLayoutManager;
import com.gbksoft.debugview.xyrecycler.listeners.HorizontalScrollListener;
import com.gbksoft.debugview.xyrecycler.listeners.VerticalScrollListener;


public interface IXYRecycler {

    void addView(View child, ViewGroup.LayoutParams params);

    boolean hasFixedWidth();

    boolean isShowHorizontalDividers();
    
    boolean isShowVerticalDividers();

    CellRecyclerView getCellRecyclerView();

    CellRecyclerView getColumnHeaderRecyclerView();

    CellRecyclerView getRowHeaderRecyclerView();

    ColumnHeaderLayoutManager getColumnHeaderLayoutManager();

    CellLayoutManager getCellLayoutManager();

    LinearLayoutManager getRowHeaderLayoutManager();

    HorizontalScrollListener getHorizontalScrollListener();

    VerticalScrollListener getVerticalRecyclerViewListener();

    DividerItemDecoration getHorizontalItemDecoration();
    
    DividerItemDecoration getVerticalItemDecoration();

    void scrollToColumnPosition(int column);

    void scrollToColumnPosition(int column, int offset);

    void scrollToRowPosition(int row);

    void scrollToRowPosition(int row, int offset);

    int getDividerColor();

    void remeasureColumnWidth(int column);

    int getRowHeaderWidth();

    void setRowHeaderWidth(int rowHeaderWidth);

    BaseRecyclerAdapter getAdapter();

    ScrollHandler getScrollHandler();
}
