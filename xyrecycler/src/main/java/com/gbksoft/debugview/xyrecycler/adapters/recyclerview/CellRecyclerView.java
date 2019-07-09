package com.gbksoft.debugview.xyrecycler.adapters.recyclerview;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.R;
import com.gbksoft.debugview.xyrecycler.listeners.HorizontalScrollListener;
import com.gbksoft.debugview.xyrecycler.listeners.VerticalScrollListener;


public class CellRecyclerView extends RecyclerView {

    private int scrolledX = 0;
    private int scrolledY = 0;

    private boolean isHorizontalScrollListenerRemoved = true;
    private boolean isVerticalScrollListenerRemoved = true;

    public CellRecyclerView(Context context) {
        super(context);

        setHasFixedSize(false);
        setNestedScrollingEnabled(false);

        setItemViewCacheSize(context.getResources().getInteger(R.integer.item_cache_size));
        setDrawingCacheEnabled(true);
        setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        scrolledX += dx;
        scrolledY += dy;

        super.onScrolled(dx, dy);
    }

    public int getScrolledX() {
        return scrolledX;
    }

    public void clearScrolledX() {
        scrolledX = 0;
    }

    public int getScrolledY() {
        return scrolledY;
    }

    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        if (listener instanceof HorizontalScrollListener) {
            if (isHorizontalScrollListenerRemoved) {
                isHorizontalScrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            }
        } else if (listener instanceof VerticalScrollListener) {
            if (isVerticalScrollListenerRemoved) {
                isVerticalScrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            }
        } else {
            super.addOnScrollListener(listener);
        }
    }

    @Override
    public void removeOnScrollListener(OnScrollListener listener) {
        if (listener instanceof HorizontalScrollListener) {
            if (!isHorizontalScrollListenerRemoved) {
                isHorizontalScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else if (listener instanceof VerticalScrollListener) {
            if (!isVerticalScrollListenerRemoved) {
                isVerticalScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else {
            super.removeOnScrollListener(listener);
        }
    }

    public boolean isHorizontalScrollListenerRemoved() {
        return isHorizontalScrollListenerRemoved;
    }

    public boolean isScrollOthers() {
        return !isHorizontalScrollListenerRemoved;
    }
}
