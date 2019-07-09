package com.gbksoft.debugview.xyrecycler.adapters.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.IXYRecycler;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.holder.BaseViewHolder;
import com.gbksoft.debugview.xyrecycler.handlers.ScrollHandler;
import com.gbksoft.debugview.xyrecycler.layoutmanagers.ColumnLayoutManager;

import java.util.List;


public class CellRecyclerViewAdapter<C> extends BaseRecyclerViewAdapter<C> {

    private IXYRecycler xyRecycler;
    private final RecyclerView.RecycledViewPool recycledViewPool;

    private int recyclerViewId = 0;

    public CellRecyclerViewAdapter(Context context, List<C> items, IXYRecycler xyRecycler) {
        super(context, items);
        this.xyRecycler = xyRecycler;

        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CellRecyclerView recyclerView = new CellRecyclerView(context);
        recyclerView.setRecycledViewPool(recycledViewPool);

        if (xyRecycler.isShowHorizontalDividers()) {
            recyclerView.addItemDecoration(xyRecycler.getHorizontalItemDecoration());
        }

        recyclerView.setHasFixedSize(xyRecycler.hasFixedWidth());
        recyclerView.addOnItemTouchListener(xyRecycler.getHorizontalScrollListener());
        recyclerView.setLayoutManager(new ColumnLayoutManager(context, xyRecycler));
        recyclerView.setAdapter(new CellRowRecyclerViewAdapter(context, xyRecycler));
        recyclerView.setId(recyclerViewId);
        recyclerViewId++;

        return new CellRowViewHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int yPosition) {
        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        CellRowRecyclerViewAdapter viewAdapter = (CellRowRecyclerViewAdapter) viewHolder.recyclerView.getAdapter();
        List<C> rowList = (List<C>) items.get(yPosition);

        viewAdapter.setYPosition(yPosition);
        viewAdapter.setItems(rowList);
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        ScrollHandler scrollHandler = xyRecycler.getScrollHandler();
        ((ColumnLayoutManager) viewHolder.recyclerView.getLayoutManager()).scrollToPositionWithOffset(scrollHandler.getColumnPosition(), scrollHandler.getColumnPositionOffset());
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        viewHolder.recyclerView.clearScrolledX();
    }

    static class CellRowViewHolder extends BaseViewHolder {
        final CellRecyclerView recyclerView;

        CellRowViewHolder(View itemView) {
            super(itemView);
            recyclerView = (CellRecyclerView) itemView;
        }
    }
}
