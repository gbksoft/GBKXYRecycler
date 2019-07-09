package com.gbksoft.debugview.xyrecycler.layoutmanagers;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerView;


public class ColumnLayoutManager extends BaseLayoutManager {

    private ColumnHeaderLayoutManager columnHeaderLayoutManager;
    private CellLayoutManager cellLayoutManager;

    private CellRecyclerView columnHeaderRecyclerView;
    private CellRecyclerView cellRowRecyclerView;

    private boolean isNeedFitForVerticalScroll;
    private boolean isNeedFitForHorizontalScroll;

    private int isLastDx = 0;
    private int yPosition;

    public ColumnLayoutManager(Context context, IXYRecycler xyRecycler) {
        super(context);

        this.xyRecycler = xyRecycler;
        columnHeaderRecyclerView = xyRecycler.getColumnHeaderRecyclerView();
        columnHeaderLayoutManager = xyRecycler.getColumnHeaderLayoutManager();
        cellLayoutManager = xyRecycler.getCellLayoutManager();

        setOrientation(ColumnLayoutManager.HORIZONTAL);

        setRecycleChildrenOnDetach(true);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        cellRowRecyclerView = (CellRecyclerView) view;
        yPosition = getRowPosition();
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        if (!xyRecycler.hasFixedWidth()) {
            measureChild(child, widthUsed, heightUsed);
        }
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {

        int columnPosition = getPosition(child);

        int cacheWidth = cellLayoutManager.getCacheWidth(yPosition, columnPosition);
        int columnCacheWidth = columnHeaderLayoutManager.getCacheWidth(0, columnPosition);

        if (cacheWidth != -1 && cacheWidth == columnCacheWidth) {
            if (child.getMeasuredWidth() != cacheWidth) {
                setWidth(child, cacheWidth);
            }
        } else {
            View columnHeaderChild = columnHeaderLayoutManager.findViewByPosition(columnPosition);
            if (columnHeaderChild != null) {
                fitWidthSize(child, yPosition, columnPosition, cacheWidth, columnCacheWidth, columnHeaderChild);
            }
        }

        // Control all of the rows which has same column position.
        if (shouldFitColumns(columnPosition, yPosition)) {
            cellLayoutManager.fitWidthSize(columnPosition, isLastDx < 0);
            isNeedFitForVerticalScroll = false;
        }

        isNeedFitForHorizontalScroll = false;
    }

    private void fitWidthSize(View child, int row, int column, int cellWidth, int columnHeaderWidth, View columnHeaderChild) {

        if (cellWidth == -1) {
            cellWidth = child.getMeasuredWidth();
        }

        if (columnHeaderWidth == -1) {
            columnHeaderWidth = columnHeaderChild.getMeasuredWidth();
        }

        if (cellWidth != 0) {

            if (columnHeaderWidth > cellWidth) {
                cellWidth = columnHeaderWidth;

            } else if (cellWidth > columnHeaderWidth) {
                columnHeaderWidth = cellWidth;
            }

            if (columnHeaderWidth != columnHeaderChild.getWidth()) {
                setWidth(columnHeaderChild, columnHeaderWidth);
                isNeedFitForVerticalScroll = true;
                isNeedFitForHorizontalScroll = true;
            }

            columnHeaderLayoutManager.setCacheWidth(0, column, columnHeaderWidth);
        }

        setWidth(child, cellWidth);
        cellLayoutManager.setCacheWidth(row, column, cellWidth);
    }

    private boolean shouldFitColumns(int xPosition, int yPosition) {
        if (isNeedFitForHorizontalScroll) {
            if (!cellRowRecyclerView.isScrollOthers() && cellLayoutManager.shouldFitColumns(yPosition)) {
                if (isLastDx > 0) {
                    int last = findLastVisibleItemPosition();
                    if (xPosition == last) {
                        return true;
                    }
                } else if (isLastDx < 0) {
                    int first = findFirstVisibleItemPosition();
                    if (xPosition == first) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (columnHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && cellRowRecyclerView.isScrollOthers()) {
            columnHeaderRecyclerView.scrollBy(dx, 0);
        }
        isLastDx = dx;

        setInitialPrefetchItemCount(2);

        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private int getRowPosition() {
        return cellLayoutManager.getPosition(cellRowRecyclerView);
    }

    public int getLastDx() {
        return isLastDx;
    }

    public boolean isNeedFit() {
        return isNeedFitForVerticalScroll;
    }

    public void clearNeedFit() {
        isNeedFitForVerticalScroll = false;
    }

}
