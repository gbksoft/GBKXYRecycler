package com.gbksoft.debugview.xyrecycler.listeners;

import android.view.View;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerView;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.CellLayoutManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class XYRecyclerLayoutChangeListener implements View.OnLayoutChangeListener {

    private CellLayoutManager cellLayoutManager;

    private CellRecyclerView columnHeaderRecyclerView;
    private CellRecyclerView cellRecyclerView;

    public XYRecyclerLayoutChangeListener(IXYRecycler xyRecycler) {
        cellLayoutManager = xyRecycler.getCellLayoutManager();
        cellRecyclerView = xyRecycler.getCellRecyclerView();
        columnHeaderRecyclerView = xyRecycler.getColumnHeaderRecyclerView();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (v.isShown() && (right - left) != (oldRight - oldLeft)) {

            if (columnHeaderRecyclerView.getWidth() > cellRecyclerView.getWidth()) {
                cellLayoutManager.remeasureAllChild();

            } else if (cellRecyclerView.getWidth() > columnHeaderRecyclerView.getWidth()) {
                columnHeaderRecyclerView.getLayoutParams().width = WRAP_CONTENT;
                columnHeaderRecyclerView.requestLayout();
            }
        }
    }
}
