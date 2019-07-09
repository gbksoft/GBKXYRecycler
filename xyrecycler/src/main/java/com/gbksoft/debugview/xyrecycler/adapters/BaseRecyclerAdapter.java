package com.gbksoft.debugview.xyrecycler.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.XYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerViewAdapter;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.RowHeaderRecyclerViewAdapter;

import java.util.List;


public abstract class BaseRecyclerAdapter<CH, RH, C> implements IRecyclerAdapter {

    protected Context context;

    private IXYRecycler xyRecycler;

    private View cornerView;

    private ColumnHeaderRecyclerViewAdapter columnHeaderRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter rowHeaderRecyclerViewAdapter;
    private CellRecyclerViewAdapter cellRecyclerViewAdapter;

    protected List<CH> columnHeaderItems;
    protected List<RH> rowHeaderItems;
    protected List<List<C>> cellItems;

    private int rowHeaderWidth;
    private int columnHeaderHeight;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setXYRecycler(XYRecycler xyRecycler) {
        this.xyRecycler = xyRecycler;

        columnHeaderRecyclerViewAdapter = new ColumnHeaderRecyclerViewAdapter(context, columnHeaderItems, this);
        rowHeaderRecyclerViewAdapter = new RowHeaderRecyclerViewAdapter(context, rowHeaderItems, this);
        cellRecyclerViewAdapter = new CellRecyclerViewAdapter(context, cellItems, xyRecycler);
    }

    private void setColumnHeaderItems(List<CH> columnHeaderItems) {
        if (columnHeaderItems != null) {
            this.columnHeaderItems = columnHeaderItems;
            xyRecycler.getColumnHeaderLayoutManager().clearCachedWidths();
            columnHeaderRecyclerViewAdapter.setItems(this.columnHeaderItems);
        }
    }

    private void setRowHeaderItems(List<RH> rowHeaderItems) {
        if (rowHeaderItems != null) {
            this.rowHeaderItems = rowHeaderItems;
            rowHeaderRecyclerViewAdapter.setItems(this.rowHeaderItems);
        }
    }

    private void setCellItems(List<List<C>> cellItems) {
        if (cellItems != null) {
            this.cellItems = cellItems;
            xyRecycler.getCellLayoutManager().clearCachedWidths();
            cellRecyclerViewAdapter.setItems(this.cellItems);
        }
    }

    public void setAllItems(List<CH> columnHeaderItems, List<RH> rowHeaderItems, List<List<C>> cellItems) {
        setColumnHeaderItems(columnHeaderItems);
        setRowHeaderItems(rowHeaderItems);
        setCellItems(cellItems);

        if ((columnHeaderItems != null && !columnHeaderItems.isEmpty()) &&
                (rowHeaderItems != null && !rowHeaderItems.isEmpty()) &&
                xyRecycler != null && cornerView == null) {
            cornerView = onCreateCornerView();
            xyRecycler.addView(cornerView, new FrameLayout.LayoutParams(rowHeaderWidth, columnHeaderHeight));

        } else if (cornerView != null) {
            if (rowHeaderItems != null && !rowHeaderItems.isEmpty()) {
                cornerView.setVisibility(View.VISIBLE);
            } else {
                cornerView.setVisibility(View.GONE);
            }
        }
    }

    public ColumnHeaderRecyclerViewAdapter getColumnHeaderRecyclerViewAdapter() {
        return columnHeaderRecyclerViewAdapter;
    }

    public RowHeaderRecyclerViewAdapter getRowHeaderRecyclerViewAdapter() {
        return rowHeaderRecyclerViewAdapter;
    }

    public CellRecyclerViewAdapter getCellRecyclerViewAdapter() {
        return cellRecyclerViewAdapter;
    }

    public void setColumnHeaderHeight(int columnHeaderHeight) {
        this.columnHeaderHeight = columnHeaderHeight;
    }

    public void setRowHeaderWidth(int rowHeaderWidth) {
        this.rowHeaderWidth = rowHeaderWidth;

        if (cornerView != null) {
            ViewGroup.LayoutParams layoutParams = cornerView.getLayoutParams();
            layoutParams.width = rowHeaderWidth;
        }
    }
}
