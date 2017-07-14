package com.td.oldplay.ui.window;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;


/**
 * Created by my on 2017/7/13.
 */

public class ListPopupWindow<T> extends PopupWindow {
    private CommonAdapter<T> adapter;
    private Context mContext;
    private int heiht;
    private int width;
    private View ContentView;
    private RecyclerView recyclerView;

    public ListPopupWindow(Context context, CommonAdapter<T> adapter, int heiht, int width) {
        super(context);
        this.adapter = adapter;
        this.mContext = context;
        this.heiht = heiht;
        this.width = width;
        init();

    }

    private void init() {
        ContentView = LayoutInflater.from(mContext).inflate(R.layout.pop_list, null);
        recyclerView = (RecyclerView) ContentView.findViewById(R.id.recycler);
        setContentView(ContentView);

        setHeight(heiht);
        setWidth(width);

        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.corwhite));
        setOutsideTouchable(true);
        setFocusable(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }


    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 5);
        } else {
            this.dismiss();
        }
    }
}
