package com.gbksoft.debugview.xyrecycler.layoutmanagers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerView;
import com.gbksoft.debugview.xyrecycler.listeners.HorizontalScrollListener;

import static android.view.ViewGroup.LayoutParams.*;


public class CellLayoutManager extends BaseLayoutManager {

    private static final int IGNORE_LEFT = Integer.MIN_VALUE;

    private ColumnHeaderLayoutManager columnHeaderLayoutManager;
    private LinearLayoutManager rowHeaderLayoutManager;

    private CellRecyclerView rowHeaderRecyclerView;
    private CellRecyclerView cellRecyclerView;

    private HorizontalScrollListener horizontalListener;

    private int lastDy = 0;
    private boolean isNeedSetLeft;
    private boolean isNeedFit;

    public CellLayoutManager(Context context, IXYRecycler xyRecycler) {
        super(context);

        this.xyRecycler = xyRecycler;

        columnHeaderLayoutManager = xyRecycler.getColumnHeaderLayoutManager();
        rowHeaderLayoutManager = xyRecycler.getRowHeaderLayoutManager();
        rowHeaderRecyclerView = xyRecycler.getRowHeaderRecyclerView();
        cellRecyclerView = xyRecycler.getCellRecyclerView();

        setOrientation(VERTICAL);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);

        if (cellRecyclerView == null) {
            cellRecyclerView = xyRecycler.getCellRecyclerView();
        }
        if (horizontalListener == null) {
            horizontalListener = xyRecycler.getHorizontalScrollListener();
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (rowHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && !rowHeaderRecyclerView.isScrollOthers()) {
            rowHeaderRecyclerView.scrollBy(0, dy);
        }
        int scroll = super.scrollVerticallyBy(dy, recycler, state);
        lastDy = dy;

        return scroll;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            lastDy = 0;
        }
    }

    public void fitWidthSize(boolean scrollingUp) {
        int left = columnHeaderLayoutManager.getFirstItemLeft();
        for (int i = columnHeaderLayoutManager.findFirstVisibleItemPosition(); i < columnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            left = fitSize(i, left, scrollingUp);
        }
        isNeedSetLeft = false;
    }

    public void fitWidthSize(int position, boolean scrollingLeft) {
        fitSize(position, IGNORE_LEFT, false);

        if (isNeedSetLeft & scrollingLeft) {
            Handler handler = new Handler();
            handler.post(() -> fitWidthSize2(true));
        }
    }

    private int fitSize(int position, int left, boolean scrollingUp) {
        int cellRight = -1;

        int columnCacheWidth = columnHeaderLayoutManager.getCacheWidth(0, position);
        View column = columnHeaderLayoutManager.findViewByPosition(position);

        if (column != null) {
            cellRight = column.getLeft() + columnCacheWidth + 1;

            if (scrollingUp) {
                for (int i = findLastVisibleItemPosition(); i >= findFirstVisibleItemPosition(); i--) {
                    cellRight = fit(position, i, left, cellRight, columnCacheWidth);
                }
            } else {
                for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() + 1; j++) {
                    cellRight = fit(position, j, left, cellRight, columnCacheWidth);
                }
            }
        } else {
            Log.e("QQQ", "Column could not found for " + position);
        }
        return cellRight;
    }

    private int fit(int xPosition, int yPosition, int left, int right, int columnCachedWidth) {
        CellRecyclerView child = (CellRecyclerView) findViewByPosition(yPosition);

        if (child != null) {
            ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child.getLayoutManager();
            int cellCacheWidth = getCacheWidth(yPosition, xPosition);
            View cell = childLayoutManager.findViewByPosition(xPosition);

            if (cell != null) {

                if (cellCacheWidth != columnCachedWidth || isNeedSetLeft) {

                    if (cellCacheWidth != columnCachedWidth) {
                        cellCacheWidth = columnCachedWidth;
                        setWidth(cell, cellCacheWidth);
                        setCacheWidth(yPosition, xPosition, cellCacheWidth);
                    }

                    if (left != IGNORE_LEFT && cell.getLeft() != left) {
                        int scrollX = Math.max(cell.getLeft(), left) - Math.min(cell.getLeft(), left);
                        cell.setLeft(left);
                        int offset = horizontalListener.getScrollPositionOffset();

                        if (offset > 0 && xPosition == childLayoutManager.findFirstVisibleItemPosition() && cellRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                            int scrollPosition = horizontalListener.getScrollPosition();
                            offset = horizontalListener.getScrollPositionOffset() + scrollX;
                            horizontalListener.setScrollPositionOffset(offset);
                            childLayoutManager.scrollToPositionWithOffset(scrollPosition, offset);
                        }
                    }

                    if (cell.getWidth() != cellCacheWidth) {
                        if (left != IGNORE_LEFT) {
                            right = cell.getLeft() + cellCacheWidth + 1;
                            cell.setRight(right);
                            childLayoutManager.layoutDecoratedWithMargins(cell, cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
                        }
                        isNeedSetLeft = true;
                    }
                }
            }
        }
        return right;
    }

    public void fitWidthSize2(boolean scrollingLeft) {
        columnHeaderLayoutManager.customRequestLayout();

        int columnHeaderScrollPosition = xyRecycler.getColumnHeaderRecyclerView().getScrolledX();
        int columnHeaderOffset = columnHeaderLayoutManager.getFirstItemLeft();
        int columnHeaderFirstItem = columnHeaderLayoutManager.findFirstVisibleItemPosition();

        for (int i = columnHeaderLayoutManager.findFirstVisibleItemPosition(); i < columnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            fitSize2(i, scrollingLeft, columnHeaderScrollPosition, columnHeaderOffset, columnHeaderFirstItem);
        }

        isNeedSetLeft = false;
    }

    private void fitSize2(int position, boolean scrollingLeft, int columnHeaderScrollPosition, int columnHeaderOffset, int columnHeaderFirstItem) {
        int columnCacheWidth = columnHeaderLayoutManager.getCacheWidth(0, position);
        View column = columnHeaderLayoutManager.findViewByPosition(position);

        if (column != null) {
            for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() + 1; j++) {
                CellRecyclerView child = (CellRecyclerView) findViewByPosition(j);

                if (child != null) {
                    ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child.getLayoutManager();

                    if (!scrollingLeft && columnHeaderScrollPosition != child.getScrolledX()) {
                        childLayoutManager.scrollToPositionWithOffset(columnHeaderFirstItem, columnHeaderOffset);
                    }
                    fit2(position, j, columnCacheWidth, column, childLayoutManager);
                }
            }
        }
    }

    private void fit2(int xPosition, int yPosition, int columnCachedWidth, View column, ColumnLayoutManager childLayoutManager) {
        int cellCacheWidth = getCacheWidth(yPosition, xPosition);
        View cell = childLayoutManager.findViewByPosition(xPosition);

        if (cell != null) {

            if (cellCacheWidth != columnCachedWidth || isNeedSetLeft) {

                if (cellCacheWidth != columnCachedWidth) {
                    cellCacheWidth = columnCachedWidth;
                    setWidth(cell, cellCacheWidth);
                    setCacheWidth(yPosition, xPosition, cellCacheWidth);
                }

                if (column.getLeft() != cell.getLeft() || column.getRight() != cell.getRight()) {
                    cell.setLeft(column.getLeft());
                    cell.setRight(column.getRight() + 1);
                    childLayoutManager.layoutDecoratedWithMargins(cell, cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
                    isNeedSetLeft = true;
                }
            }
        }
    }

    public boolean shouldFitColumns(int yPosition) {
        if (cellRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisiblePosition = findLastVisibleItemPosition();
            CellRecyclerView lastCellRecyclerView = (CellRecyclerView) findViewByPosition(lastVisiblePosition);

            if (lastCellRecyclerView != null) {
                if (yPosition == lastVisiblePosition) {
                    return true;
                } else if (lastCellRecyclerView.isScrollOthers() && yPosition == lastVisiblePosition - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        if (!xyRecycler.hasFixedWidth()) {
            int position = getPosition(child);
            ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) ((CellRecyclerView) child).getLayoutManager();

            if (cellRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                if (childLayoutManager.isNeedFit()) {

                    if (lastDy < 0) {
                        fitWidthSize(true);
                    } else {
                        fitWidthSize(false);
                    }
                    childLayoutManager.clearNeedFit();
                }
                childLayoutManager.setInitialPrefetchItemCount(childLayoutManager.getChildCount());

            } else if (childLayoutManager.getLastDx() == 0 && cellRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (childLayoutManager.isNeedFit()) {
                    isNeedFit = true;
                    childLayoutManager.clearNeedFit();
                }

                if (isNeedFit) {
                    if (rowHeaderLayoutManager.findLastVisibleItemPosition() == position) {
                        fitWidthSize2(false);
                        isNeedFit = false;
                    }
                }
            }
        }
    }

    public void remeasureAllChild() {
        for (int j = 0; j < getChildCount(); j++) {
            CellRecyclerView recyclerView = (CellRecyclerView) getChildAt(j);
            recyclerView.getLayoutParams().width = WRAP_CONTENT;
            recyclerView.requestLayout();
        }
    }

    public void setCacheWidth(int column, int width) {
        for (int i = 0; i < rowHeaderRecyclerView.getAdapter().getItemCount(); i++) {
            setCacheWidth(i, column, width);
        }
    }
}
