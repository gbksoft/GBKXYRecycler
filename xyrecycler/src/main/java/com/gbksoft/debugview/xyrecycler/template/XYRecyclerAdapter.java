package com.gbksoft.debugview.xyrecycler.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbksoft.debugview.xyrecycler.R;
import com.gbksoft.debugview.xyrecycler.adapters.BaseRecyclerAdapter;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.holder.BaseViewHolder;
import com.gbksoft.debugview.xyrecycler.template.models.CellModel;
import com.gbksoft.debugview.xyrecycler.template.models.ColumnHeaderModel;
import com.gbksoft.debugview.xyrecycler.template.models.RowHeaderModel;


public class XYRecyclerAdapter extends BaseRecyclerAdapter<ColumnHeaderModel, RowHeaderModel, CellModel> {

    private final LayoutInflater inflater;

    public XYRecyclerAdapter(Context context) {
        super(context);

        inflater = LayoutInflater.from(context);
    }


    @Override
    public View onCreateCornerView() {
        View corner = inflater.inflate(R.layout.corner_layout, null);
        corner.setOnClickListener(view -> {
        });
        return corner;
    }


    @Override
    public BaseViewHolder onColumnHeaderCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColumnHeaderViewHolder(inflater.inflate(R.layout.header_column_layout, parent, false));
    }

    @Override
    public void onColumnHeaderBindViewHolder(BaseViewHolder holder, ColumnHeaderModel columnHeaderItemModel, int columnPosition) {
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(columnHeaderItemModel);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }


    @Override
    public BaseViewHolder onRowHeaderCreateViewHolder(ViewGroup parent, int viewType) {
        return new RowHeaderViewHolder(inflater.inflate(R.layout.header_row_layout, parent, false));
    }

    @Override
    public void onRowHeaderBindViewHolder(BaseViewHolder holder, RowHeaderModel rowHeaderItemModel, int rowPosition) {
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.setRowHeader(rowHeaderItemModel);
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }


    public BaseViewHolder onCellCreateViewHolder(ViewGroup parent, int viewType) {
        return new CellViewHolder(inflater.inflate(R.layout.cell_layout, parent, false));
    }

    @Override
    public void onCellBindViewHolder(BaseViewHolder holder, CellModel cellItemModel, int columnPosition, int rowPosition) {
        CellViewHolder cellViewHolder = (CellViewHolder) holder;
        cellViewHolder.setCell(cellItemModel);
    }

    @Override
    public int getCellItemViewType(int column) {
        return 0;
    }


    // === Holders ===

    class ColumnHeaderViewHolder extends BaseViewHolder {

        private final LinearLayout columnHeaderContainer;
        private final TextView columnHeaderTV;

        ColumnHeaderViewHolder(View itemView) {
            super(itemView);
            columnHeaderContainer = itemView.findViewById(R.id.columnHeaderContainer);
            columnHeaderTV = itemView.findViewById(R.id.columnHeaderTV);
        }

        void setColumnHeader(ColumnHeaderModel columnHeader) {
            columnHeaderContainer.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            columnHeaderTV.setText(columnHeader.getContent());
            columnHeaderTV.requestLayout();
        }
    }

    class RowHeaderViewHolder extends BaseViewHolder {

        private final LinearLayout rowHeaderContainer;
        private final TextView rowHeaderTV;

        RowHeaderViewHolder(View itemView) {
            super(itemView);
            rowHeaderContainer = itemView.findViewById(R.id.rowHeaderContainer);
            rowHeaderTV = itemView.findViewById(R.id.rowHeaderTV);
        }

        void setRowHeader(RowHeaderModel rowHeader) {
            rowHeaderContainer.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            rowHeaderTV.setText(rowHeader.getContent());
            rowHeaderTV.requestLayout();
        }
    }

    class CellViewHolder extends BaseViewHolder {

        private final LinearLayout cellContainer;
        private final TextView cellContentTV;

        CellViewHolder(View itemView) {
            super(itemView);
            cellContainer = itemView.findViewById(R.id.cellContainer);
            cellContentTV = itemView.findViewById(R.id.cellContentTV);
        }

        void setCell(CellModel cell) {
            cellContainer.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            cellContentTV.setText(cell.getContent());
            cellContentTV.requestLayout();
        }
    }
}
