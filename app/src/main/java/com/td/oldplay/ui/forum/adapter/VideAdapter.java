package com.td.oldplay.ui.forum.adapter;

import android.content.Context;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.api.NetWorkAPI;
import com.td.oldplay.utils.GlideUtils;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by my on 2017/7/20.
 */

public class VideAdapter extends CommonAdapter<String> {
    JCVideoPlayerStandard jcVideoPlayer;

    public VideAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String strings, int position) {
        jcVideoPlayer = holder.getView(R.id.videoplayer);
        jcVideoPlayer.setUp(strings,JCVideoPlayer.SCREEN_LAYOUT_LIST, "");


       // jcVideoPlayer.setUp("http://tanzi27niu.cdsb.mobiwps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST, "");

    }
}
