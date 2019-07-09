package com.gbksoft.debugview.xyrecycler.handlers;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.CellLayoutManager;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.ColumnHeaderLayoutManager;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.ColumnLayoutManager;


public class ScrollHandler {

    private IXYRecycler xyRecycler;

    private ColumnHeaderLayoutManager columnHeaderLayoutManager;
    private LinearLayoutManager rowHeaderLayoutManager;
    private CellLayoutManager cellLayoutManager;

    public ScrollHandler(IXYRecycler xyRecycler) {
        this.xyRecycler = xyRecycler;
        columnHeaderLayoutManager = xyRecycler.getColumnHeaderLayoutManager();
        rowHeaderLayoutManager = xyRecycler.getRowHeaderLayoutManager();
        cellLayoutManager = xyRecycler.getCellLayoutManager();
    }

    public void scrollToColumnPosition(int columnPosition) {
        if (!((View) xyRecycler).isShown()) {
            xyRecycler.getHorizontalScrollListener().setScrollPosition(columnPosition);
        }
        scrollColumnHeader(columnPosition, 0);
        scrollCellHorizontally(columnPosition, 0);
    }

    public void scrollToColumnPosition(int columnPosition, int offset) {
        if (!((View) xyRecycler).isShown()) {
            xyRecycler.getHorizontalScrollListener().setScrollPosition(columnPosition);
            xyRecycler.getHorizontalScrollListener().setScrollPositionOffset(offset);
        }
        scrollColumnHeader(columnPosition, offset);
        scrollCellHorizontally(columnPosition, offset);
    }

    public void scrollToRowPosition(int rowPosition) {
        rowHeaderLayoutManager.scrollToPosition(rowPosition);
        cellLayoutManager.scrollToPosition(rowPosition);
    }

    public void scrollToRowPosition(int rowPosition, int offset) {
        rowHeaderLayoutManager.scrollToPositionWithOffset(rowPosition, offset);
        cellLayoutManager.scrollToPositionWithOffset(rowPosition, offset);
    }

    private void scrollCellHorizontally(int columnPosition, int offset) {
        CellLayoutManager cellLayoutManager = xyRecycler.getCellLayoutManager();

        for (int i = cellLayoutManager.findFirstVisibleItemPosition(); i < cellLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            RecyclerView cellRowRecyclerView = (RecyclerView) cellLayoutManager.findViewByPosition(i);

            if (cellRowRecyclerView != null) {
                ColumnLayoutManager columnLayoutManager = (ColumnLayoutManager) cellRowRecyclerView.getLayoutManager();
                columnLayoutManager.scrollToPositionWithOffset(columnPosition, offset);
            }
        }
    }

    private void scrollColumnHeader(int columnPosition, int offset) {
        xyRecycler.getColumnHeaderLayoutManager().scrollToPositionWithOffset(columnPosition, offset);
    }

    public int getColumnPosition() {
        return columnHeaderLayoutManager.findFirstVisibleItemPosition();
    }

    public int getColumnPositionOffset() {
        View child = columnHeaderLayoutManager.findViewByPosition(columnHeaderLayoutManager.findFirstVisibleItemPosition());
        if (child != null) {
            return child.getLeft();
        }
        return 0;
    }
}
