package com.gbksoft.debugview.xyrecycler.adapters.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.gbksoft.debugview.xyrecycler.adapters.IRecyclerAdapter;
import com.gbksoft.debugview.xyrecycler.adapters.recyclerview.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context context;

    protected IRecyclerAdapter recyclerAdapter;
    protected List<T> items;

    public BaseRecyclerViewAdapter(Context context, List<T> items) {
        this.context = context;
        setItems(items);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        setItems(items, true);
    }

    public void setItems(List<T> items, boolean notifyDataSet) {
        if (items != null) {
            if (this.items == null) {
                this.items = new ArrayList<>();
            }
            this.items.clear();
            this.items.addAll(items);

            if (notifyDataSet) {
                notifyDataSetChanged();
            }
        }
    }

    public void addItem(int position, T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            items.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void changeItem(int position, T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            items.set(position, item);
            notifyItemChanged(position);
        }
    }

    public T getItem(int position) {
        if (items == null || items.isEmpty() || position < 0 || position >= items.size()) {
            return null;
        }
        return items.get(position);
    }

    public void deleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        return 1;
//    }
}
