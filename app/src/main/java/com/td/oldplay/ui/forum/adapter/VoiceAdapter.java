package com.td.oldplay.ui.forum.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.widget.voicemanager.VoiceManager;

import java.util.List;

/**
 * Created by my on 2017/7/20.
 */

public class VoiceAdapter extends CommonAdapter<String> {
    private int lastPosition;

    public VoiceAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(final ViewHolder holder, final String strings, int position) {
        VoiceManager.getInstance(mContext).setSeekBarListener((SeekBar) holder.getView(R.id.item_voice_seek));
        holder.setOnClickListener(R.id.item_voice_start, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VoiceManager.getInstance(mContext).isPlaying()) {
                    VoiceManager.getInstance(mContext).stopPlay();
                    holder.setImageResource(R.id.item_voice_start, R.mipmap.icon_play);
                } else {
                    VoiceManager.getInstance(mContext).startPlay(strings);
                    holder.setImageResource(R.id.item_voice_start, R.mipmap.icon_pause);
                }


            }
        });
    }
}
