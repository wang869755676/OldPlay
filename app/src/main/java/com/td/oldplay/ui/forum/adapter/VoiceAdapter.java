package com.td.oldplay.ui.forum.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by my on 2017/7/20.
 */

public class VoiceAdapter extends CommonAdapter<List<String>> {
    public VoiceAdapter(Context context, int layoutId, List<List<String>> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, List<String> strings, int position) {

    }
}
