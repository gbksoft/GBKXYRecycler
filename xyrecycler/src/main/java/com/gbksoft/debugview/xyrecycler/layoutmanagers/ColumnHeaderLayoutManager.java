package com.gbksoft.debugview.xyrecycler.layoutmanagers;

import android.content.Context;
import android.view.View;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;


public class ColumnHeaderLayoutManager extends BaseLayoutManager {

    public ColumnHeaderLayoutManager(Context context, IXYRecycler xyRecycler) {
        super(context);

        this.xyRecycler = xyRecycler;
        setOrientation(ColumnHeaderLayoutManager.HORIZONTAL);
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
        if (xyRecycler.hasFixedWidth()) {
            super.measureChild(child, widthUsed, heightUsed);
            return;
        }

        int position = getPosition(child);
        int cacheWidth = getCacheWidth(0, position);

        if (cacheWidth != -1) {
            setWidth(child, cacheWidth);
        } else {
            super.measureChild(child, widthUsed, heightUsed);
        }
    }

    public int getFirstItemLeft() {
        View firstColumnHeader = findViewByPosition(findFirstVisibleItemPosition());
        return firstColumnHeader.getLeft();
    }

    public void customRequestLayout() {
        int left = getFirstItemLeft();
        int right;
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {

            right = left + getCacheWidth(0, i);

            View columnHeader = findViewByPosition(i);
            columnHeader.setLeft(left);
            columnHeader.setRight(right);

            layoutDecoratedWithMargins(columnHeader, columnHeader.getLeft(), columnHeader.getTop(), columnHeader.getRight(), columnHeader.getBottom());

            left = right + 1;
        }
    }
}
