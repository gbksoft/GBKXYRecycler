package com.gbksoft.debugview.xyrecycler.layoutmanagers;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;


public class BaseLayoutManager extends LinearLayoutManager {

    protected IXYRecycler xyRecycler;
    protected final SparseArray<SparseArray<Integer>> cachedWidthList = new SparseArray<>();


    public BaseLayoutManager(Context context) {
        super(context);
    }


    protected static void setWidth(View view, int width) {
        ((RecyclerView.LayoutParams) view.getLayoutParams()).width = width;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY);

        view.measure(widthMeasureSpec, heightMeasureSpec);

        view.requestLayout();
    }


    public void setCacheWidth(int row, int column, int width) {
        SparseArray<Integer> cellRowCache = cachedWidthList.get(row);
        if (cellRowCache == null) {
            cellRowCache = new SparseArray<>();
        }

        cellRowCache.put(column, width);
        cachedWidthList.put(row, cellRowCache);
    }

    public int getCacheWidth(int row, int column) {
        SparseArray<Integer> cellRowCaches = cachedWidthList.get(row);
        if (cellRowCaches != null) {
            Integer cachedWidth = cellRowCaches.get(column);
            if (cachedWidth != null) {
                return cachedWidth;
            }
        }
        return -1;
    }

    public void removeCachedWidth(int position) {
        cachedWidthList.get(0).remove(position);
    }

    public void clearCachedWidths() {
        cachedWidthList.clear();
    }
}
