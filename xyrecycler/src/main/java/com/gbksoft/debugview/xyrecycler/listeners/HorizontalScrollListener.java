package com.gbksoft.debugview.xyrecycler.listeners;

import android.util.Log;
import android.view.MotionEvent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerView;


public class HorizontalScrollListener extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {

    private RecyclerView.LayoutManager cellLayoutManager;

    private CellRecyclerView columnHeaderRecyclerView;
    private RecyclerView lastTouchedRecyclerView;

    private int columnPosition;
    private boolean isMoved;

    private int scrollPosition;
    private int scrollPositionOffset;

    private RecyclerView currentRecyclerViewTouched;

    private VerticalScrollListener verticalScrollListener;

    public HorizontalScrollListener(IXYRecycler xyRecycler) {
        cellLayoutManager = xyRecycler.getCellRecyclerView().getLayoutManager();
        columnHeaderRecyclerView = xyRecycler.getColumnHeaderRecyclerView();
        verticalScrollListener = xyRecycler.getVerticalRecyclerViewListener();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (currentRecyclerViewTouched != null && recyclerView != currentRecyclerViewTouched) {
            return true;
        }

        Log.d("QQQ", "onInterceptTouchEvent: Horizontal");

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            currentRecyclerViewTouched = recyclerView;
            if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (lastTouchedRecyclerView != null && recyclerView != lastTouchedRecyclerView) {
                    if (lastTouchedRecyclerView == columnHeaderRecyclerView) {
                        columnHeaderRecyclerView.removeOnScrollListener(this);
                        columnHeaderRecyclerView.stopScroll();
                    } else {
                        int lastTouchedIndex = getIndex(lastTouchedRecyclerView);

                        if (lastTouchedIndex >= 0 && lastTouchedIndex < cellLayoutManager.getChildCount()) {
                            if (!((CellRecyclerView) lastTouchedRecyclerView).isHorizontalScrollListenerRemoved()) {
                                ((RecyclerView) cellLayoutManager.getChildAt(lastTouchedIndex)).removeOnScrollListener(this);
                                ((RecyclerView) cellLayoutManager.getChildAt(lastTouchedIndex)).stopScroll();
                            }
                        }
                    }
                }

                columnPosition = ((CellRecyclerView) recyclerView).getScrolledX();
                recyclerView.addOnScrollListener(this);
            }

        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            currentRecyclerViewTouched = recyclerView;
            isMoved = true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            currentRecyclerViewTouched = null;
            int nScrollX = ((CellRecyclerView) recyclerView).getScrolledX();
            if (columnPosition == nScrollX && !isMoved) {
                recyclerView.removeOnScrollListener(this);
            }

            lastTouchedRecyclerView = recyclerView;

        } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
            renewScrollPosition(recyclerView);
            recyclerView.removeOnScrollListener(this);
            isMoved = false;
            lastTouchedRecyclerView = recyclerView;
            currentRecyclerViewTouched = null;
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
        if (recyclerView == columnHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);

            for (int i = 0; i < cellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) cellLayoutManager.getChildAt(i);
                child.scrollBy(dx, 0);
            }
        } else {
            super.onScrolled(recyclerView, dx, dy);

            for (int i = 0; i < cellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) cellLayoutManager.getChildAt(i);
                if (child != recyclerView) {
                    child.scrollBy(dx, 0);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            renewScrollPosition(recyclerView);
            recyclerView.removeOnScrollListener(this);
            isMoved = false;
            boolean isNeeded = lastTouchedRecyclerView != columnHeaderRecyclerView;
            verticalScrollListener.removeLastTouchedRecyclerViewScrollListener(isNeeded);
        }
    }

    private int getIndex(RecyclerView rv) {
        for (int i = 0; i < cellLayoutManager.getChildCount(); i++) {
            if (cellLayoutManager.getChildAt(i) == rv) {
                return i;
            }
        }
        return -1;
    }

    private void renewScrollPosition(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        scrollPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

        if (scrollPosition == -1) {
            scrollPosition = layoutManager.findFirstVisibleItemPosition();

            if (scrollPosition != layoutManager.findLastVisibleItemPosition()) {
                scrollPosition = scrollPosition + 1;
            }
        }

        scrollPositionOffset = layoutManager.findViewByPosition(scrollPosition).getLeft();
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    public int getScrollPositionOffset() {
        return scrollPositionOffset;
    }

    public void setScrollPositionOffset(int offset) {
        scrollPositionOffset = offset;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }
}
