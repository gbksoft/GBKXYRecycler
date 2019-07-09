package com.gbksoft.debugview.xyrecycler.listeners;

import android.util.Log;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerView;


public class VerticalScrollListener extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {

    private CellRecyclerView rowHeaderRecyclerView;
    private CellRecyclerView cellRecyclerView;
    private RecyclerView lastTouchedRecyclerView;
    private RecyclerView currentRecyclerViewTouched;

    private int rowPosition;
    private boolean isMoved;

    private float dx = 0f;
    private float dy = 0f;


    public VerticalScrollListener(IXYRecycler xyRecycler) {
        rowHeaderRecyclerView = xyRecycler.getRowHeaderRecyclerView();
        cellRecyclerView = xyRecycler.getCellRecyclerView();
    }

    private boolean verticalDirection(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (dx == 0) {
                dx = ev.getX();
            }
            if (dy == 0) {
                dy = ev.getY();
            }
            float xDiff = Math.abs(dx - ev.getX());
            float yDiff = Math.abs(dy - ev.getY());
            dx = ev.getX();
            dy = ev.getY();

            if (xDiff > yDiff) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if ((currentRecyclerViewTouched != null && recyclerView != currentRecyclerViewTouched)) {
            return true;
        }

        Log.d("QQQ", "onInterceptTouchEvent: Vertical");

        if (!verticalDirection(motionEvent)) {
            currentRecyclerViewTouched = null;
            return false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            currentRecyclerViewTouched = recyclerView;
            if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (lastTouchedRecyclerView != null && recyclerView != lastTouchedRecyclerView) {
                    removeLastTouchedRecyclerViewScrollListener(false);
                }
                rowPosition = ((CellRecyclerView) recyclerView).getScrolledY();
                recyclerView.addOnScrollListener(this);
                isMoved = false;
            }

        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            currentRecyclerViewTouched = recyclerView;
            isMoved = true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            currentRecyclerViewTouched = null;
            int nScrollY = ((CellRecyclerView) recyclerView).getScrolledY();
            if (rowPosition == nScrollY && !isMoved && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                recyclerView.removeOnScrollListener(this);
            }
            lastTouchedRecyclerView = recyclerView;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView == cellRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);
        } else if (recyclerView == rowHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);
            cellRecyclerView.scrollBy(0, dy);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this);
            isMoved = false;
            currentRecyclerViewTouched = null;
        }
    }

    public void removeLastTouchedRecyclerViewScrollListener(boolean isNeeded) {
        if (lastTouchedRecyclerView == cellRecyclerView) {
            cellRecyclerView.removeOnScrollListener(this);
            cellRecyclerView.stopScroll();
        } else {
            rowHeaderRecyclerView.removeOnScrollListener(this);
            rowHeaderRecyclerView.stopScroll();
            if (isNeeded) {
                cellRecyclerView.removeOnScrollListener(this);
                cellRecyclerView.stopScroll();
            }
        }
    }
}
