package com.td.oldplay.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by snowbean on 16-5-28.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected static final String TAG = BaseRecyclerAdapter.class.getSimpleName();

    protected List<T> mData;

    protected OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void refreshData(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (data == null) return;
        if (mData == null) {
            refreshData(data);
            return;
        }
        final int oldCount = getItemCount();
        mData.addAll(data);

        notifyItemInserted(oldCount);
    }

    protected abstract int getLayoutResId();

    protected abstract RecyclerView.ViewHolder createViewHolder(View itemView);

    protected abstract void bind(RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
        return createViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bind(holder, position);
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T t, int position);
    }
}
