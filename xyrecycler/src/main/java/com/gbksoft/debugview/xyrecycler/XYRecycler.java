package com.gbksoft.debugview.xyrecycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gbksoft.debugview.xyrecycler.adapters.BaseRecyclerAdapter;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.CellRecyclerView;
import com.gbksoft.debugview.xyrecycler.handlers.ScrollHandler;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.CellLayoutManager;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.ColumnHeaderLayoutManager;
import com.gbksoft.debugview.xyrecycler.listeners.XYRecyclerLayoutChangeListener;
import com.gbksoft.debugview.xyrecycler.listeners.HorizontalScrollListener;
import com.gbksoft.debugview.xyrecycler.listeners.VerticalScrollListener;

public class XYRecycler extends FrameLayout implements IXYRecycler {

    protected BaseRecyclerAdapter recyclerAdapter;

    private ColumnHeaderLayoutManager columnHeaderLayoutManager;
    private LinearLayoutManager rowHeaderLayoutManager;
    private CellLayoutManager cellLayoutManager;

    protected CellRecyclerView columnHeaderRecyclerView;
    protected CellRecyclerView rowHeaderRecyclerView;
    protected CellRecyclerView cellRecyclerView;

    private VerticalScrollListener verticalRecyclerListener;
    private HorizontalScrollListener horizontalScrollListener;

    private DividerItemDecoration verticalItemDecoration;
    private DividerItemDecoration horizontalItemDecoration;

    private ScrollHandler scrollHandler;

    private int columnHeaderHeight;
    private int rowHeaderWidth;

    private boolean isFixedWidth;

    private int dividerColor = -1;
    private boolean isShowHorizontalDividers = true;
    private boolean isShowVerticalDividers = true;


    public XYRecycler(@NonNull Context context) {
        super(context);
        init(null);
    }

    public XYRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public XYRecycler(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);
    }

    private void init(AttributeSet attrs) {
        rowHeaderWidth = (int) getResources().getDimension(R.dimen.row_header_width);
        columnHeaderHeight = (int) getResources().getDimension(R.dimen.column_header_height);

        if (attrs != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.XYRecycler, 0, 0);
            try {
                rowHeaderWidth = (int) typedArray.getDimension(R.styleable.XYRecycler_row_header_width, rowHeaderWidth);
                columnHeaderHeight = (int) typedArray.getDimension(R.styleable.XYRecycler_column_header_height, columnHeaderHeight);
                dividerColor = typedArray.getColor(R.styleable.XYRecycler_divider_color, ContextCompat.getColor(getContext(), R.color.divider_color));
                isShowVerticalDividers = typedArray.getBoolean(R.styleable.XYRecycler_show_vertical_divider, isShowVerticalDividers);
                isShowHorizontalDividers = typedArray.getBoolean(R.styleable.XYRecycler_show_horizontal_divider, isShowHorizontalDividers);
            } finally {
                typedArray.recycle();
            }
        }

        columnHeaderRecyclerView = createColumnHeaderRecyclerView();
        rowHeaderRecyclerView = createRowHeaderRecyclerView();
        cellRecyclerView = createCellRecyclerView();

        addView(columnHeaderRecyclerView);
        addView(rowHeaderRecyclerView);
        addView(cellRecyclerView);

        scrollHandler = new ScrollHandler(this);

        initListeners();
    }

    protected void initListeners() {
        verticalRecyclerListener = new VerticalScrollListener(this);
        rowHeaderRecyclerView.addOnItemTouchListener(verticalRecyclerListener);
        cellRecyclerView.addOnItemTouchListener(verticalRecyclerListener);

        horizontalScrollListener = new HorizontalScrollListener(this);
        columnHeaderRecyclerView.addOnItemTouchListener(horizontalScrollListener);

        XYRecyclerLayoutChangeListener layoutChangeListener = new XYRecyclerLayoutChangeListener(this);
        columnHeaderRecyclerView.addOnLayoutChangeListener(layoutChangeListener);
        cellRecyclerView.addOnLayoutChangeListener(layoutChangeListener);
    }

    protected CellRecyclerView createColumnHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());
        recyclerView.setLayoutManager(getColumnHeaderLayoutManager());

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, columnHeaderHeight);
        layoutParams.leftMargin = rowHeaderWidth;
        recyclerView.setLayoutParams(layoutParams);

        if (isShowHorizontalDividers()) {
            recyclerView.addItemDecoration(getHorizontalItemDecoration());
        }

        return recyclerView;
    }

    protected CellRecyclerView createRowHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());
        recyclerView.setLayoutManager(getRowHeaderLayoutManager());

        LayoutParams layoutParams = new LayoutParams(rowHeaderWidth, LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = columnHeaderHeight;
        recyclerView.setLayoutParams(layoutParams);

        if (isShowVerticalDividers()) {
            recyclerView.addItemDecoration(getVerticalItemDecoration());
        }

        return recyclerView;
    }

    protected CellRecyclerView createCellRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());
        recyclerView.setMotionEventSplittingEnabled(false);
        recyclerView.setLayoutManager(getCellLayoutManager());

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = rowHeaderWidth;
        layoutParams.topMargin = columnHeaderHeight;
        recyclerView.setLayoutParams(layoutParams);

        if (isShowVerticalDividers()) {
            recyclerView.addItemDecoration(getVerticalItemDecoration());
        }

        return recyclerView;
    }

    public void setAdapter(BaseRecyclerAdapter recyclerAdapter) {
        if (recyclerAdapter != null) {
            this.recyclerAdapter = recyclerAdapter;
            this.recyclerAdapter.setRowHeaderWidth(rowHeaderWidth);
            this.recyclerAdapter.setColumnHeaderHeight(columnHeaderHeight);
            this.recyclerAdapter.setXYRecycler(this);

            if (columnHeaderRecyclerView != null) {
                columnHeaderRecyclerView.setAdapter(this.recyclerAdapter.getColumnHeaderRecyclerViewAdapter());
            }
            if (rowHeaderRecyclerView != null) {
                rowHeaderRecyclerView.setAdapter(this.recyclerAdapter.getRowHeaderRecyclerViewAdapter());
            }
            if (cellRecyclerView != null) {
                cellRecyclerView.setAdapter(this.recyclerAdapter.getCellRecyclerViewAdapter());
            }
        }
    }

    @Override
    public boolean hasFixedWidth() {
        return isFixedWidth;
    }

    public void setHasFixedWidth(boolean isFixedWidth) {
        this.isFixedWidth = isFixedWidth;
        columnHeaderRecyclerView.setHasFixedSize(isFixedWidth);
    }

    @Override
    public boolean isShowHorizontalDividers() {
        return isShowHorizontalDividers;
    }

    public void setShowHorizontalDividers(boolean isShowHorizontalDividers) {
        this.isShowHorizontalDividers = isShowHorizontalDividers;
    }

    @Override
    public boolean isShowVerticalDividers() {
        return isShowVerticalDividers;
    }

    public void setShowVerticalDividers(boolean isShowVerticalDividers) {
        this.isShowVerticalDividers = isShowVerticalDividers;
    }

    @Override
    public CellRecyclerView getCellRecyclerView() {
        return cellRecyclerView;
    }

    @Override
    public CellRecyclerView getColumnHeaderRecyclerView() {
        return columnHeaderRecyclerView;
    }

    @Override
    public CellRecyclerView getRowHeaderRecyclerView() {
        return rowHeaderRecyclerView;
    }

    @Override
    public ColumnHeaderLayoutManager getColumnHeaderLayoutManager() {
        if (columnHeaderLayoutManager == null) {
            columnHeaderLayoutManager = new ColumnHeaderLayoutManager(getContext(), this);
        }
        return columnHeaderLayoutManager;
    }

    @Override
    public LinearLayoutManager getRowHeaderLayoutManager() {
        if (rowHeaderLayoutManager == null) {
            rowHeaderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        return rowHeaderLayoutManager;
    }

    @Override
    public CellLayoutManager getCellLayoutManager() {
        if (cellLayoutManager == null) {
            cellLayoutManager = new CellLayoutManager(getContext(), this);
        }
        return cellLayoutManager;
    }

    @Override
    public HorizontalScrollListener getHorizontalScrollListener() {
        return horizontalScrollListener;
    }

    @Override
    public VerticalScrollListener getVerticalRecyclerViewListener() {
        return verticalRecyclerListener;
    }

    @Override
    public void remeasureColumnWidth(int column) {
        getColumnHeaderLayoutManager().removeCachedWidth(column);
        getCellLayoutManager().fitWidthSize(column, false);
    }

    @Override
    public BaseRecyclerAdapter getAdapter() {
        return recyclerAdapter;
    }

    @Override
    public void scrollToColumnPosition(int column) {
        scrollHandler.scrollToColumnPosition(column);
    }

    @Override
    public void scrollToColumnPosition(int column, int offset) {
        scrollHandler.scrollToColumnPosition(column, offset);
    }

    @Override
    public void scrollToRowPosition(int row) {
        scrollHandler.scrollToRowPosition(row);
    }

    @Override
    public void scrollToRowPosition(int row, int offset) {
        scrollHandler.scrollToRowPosition(row, offset);
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    @Override
    public DividerItemDecoration getHorizontalItemDecoration() {
        if (horizontalItemDecoration == null) {
            horizontalItemDecoration = createItemDecoration(DividerItemDecoration.HORIZONTAL);
        }
        return horizontalItemDecoration;
    }

    @Override
    public DividerItemDecoration getVerticalItemDecoration() {
        if (verticalItemDecoration == null) {
            verticalItemDecoration = createItemDecoration(DividerItemDecoration.VERTICAL);
        }
        return verticalItemDecoration;
    }

    protected DividerItemDecoration createItemDecoration(int orientation) {
        Drawable divider = ContextCompat.getDrawable(getContext(), R.drawable.cell_divider);

        if (dividerColor != -1) {
            divider.setColorFilter(dividerColor, PorterDuff.Mode.SRC_ATOP);
        }

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), orientation);
        itemDecoration.setDrawable(divider);
        return itemDecoration;
    }

    public void setDividerColor(@ColorInt int dividerColor) {
        this.dividerColor = dividerColor;
    }

    @Override
    public @ColorInt
    int getDividerColor() {
        return dividerColor;
    }

    @Override
    public int getRowHeaderWidth() {
        return rowHeaderWidth;
    }

    @Override
    public void setRowHeaderWidth(int rowHeaderWidth) {
        this.rowHeaderWidth = rowHeaderWidth;

        if (rowHeaderRecyclerView != null) {
            LayoutParams layoutParams = (LayoutParams) rowHeaderRecyclerView.getLayoutParams();
            layoutParams.width = rowHeaderWidth;
            rowHeaderRecyclerView.setLayoutParams(layoutParams);
            rowHeaderRecyclerView.requestLayout();
        }

        if (columnHeaderRecyclerView != null) {
            LayoutParams layoutParams = (LayoutParams) columnHeaderRecyclerView.getLayoutParams();
            layoutParams.leftMargin = rowHeaderWidth;
            columnHeaderRecyclerView.setLayoutParams(layoutParams);
            columnHeaderRecyclerView.requestLayout();
        }

        if (cellRecyclerView != null) {
            LayoutParams layoutParams = (LayoutParams) cellRecyclerView.getLayoutParams();
            layoutParams.leftMargin = rowHeaderWidth;
            cellRecyclerView.setLayoutParams(layoutParams);
            cellRecyclerView.requestLayout();
        }

        if (getAdapter() != null) {
            getAdapter().setRowHeaderWidth(rowHeaderWidth);
        }
    }
}
