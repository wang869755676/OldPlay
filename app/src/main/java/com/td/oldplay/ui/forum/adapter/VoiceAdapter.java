package com.td.oldplay.ui.forum.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.voicemanager.VoiceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by my on 2017/7/20.
 */

public class VoiceAdapter extends CommonAdapter<String> {
    private int lastPosition;
    private int currentPosition = -1;
    private VoiceManager voiceManager;
    private HashMap<Integer, SeekBar> seekBars;


    public VoiceAdapter(Context context, int layoutId, List<String> picStr, List<String> datas) {
        super(context, layoutId, datas);
        voiceManager = VoiceManager.getInstance(context);
        seekBars = new HashMap<>();

    }

    @Override
    protected void convert(final ViewHolder holder, final String strings, final int position) {
        seekBars.put(position, (SeekBar) holder.getView(R.id.item_voice_seek));
        holder.setImageResource(R.id.item_voice_pause, R.mipmap.icon_pause);
        holder.setVisible(R.id.item_voice_pause, false);
        holder.setVisible(R.id.item_voice_start, true);
        if (voiceManager.isPlaying() && lastPosition == position) {
            holder.getView(R.id.item_voice_pause).setVisibility(View.VISIBLE);
            holder.getView(R.id.item_voice_start).setVisibility(View.GONE);

        }
        holder.setOnClickListener(R.id.item_voice_start, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                if (voiceManager.isPlaying() && lastPosition == position) {
                    voiceManager.stopPlay();
                } else {
                    voiceManager.stopPlay();
                    voiceManager.setVoicePlayListener(new VoiceManager.VoicePlayCallBack() {
                        @Override
                        public void voiceTotalLength(long time, String strTime) {
                            // ((SeekBar) holder.getView(R.id.item_voice_seek)).setMax((int) time);
                        }

                        @Override
                        public void playDoing(long time, String strTime) {


                        }

                        @Override
                        public void playPause() {

                        }

                        @Override
                        public void playStart() {
                            v.setEnabled(true);

                        }

                        @Override
                        public void playFinish() {
                            holder.getView(R.id.item_voice_pause).setVisibility(View.GONE);
                            holder.getView(R.id.item_voice_start).setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void playError() {
                            voiceManager.stopPlay();
                            ToastUtil.show("播放出错");


                        }
                    });
                    voiceManager.setSeekBarListener((SeekBar) holder.getView(R.id.item_voice_seek));
                    voiceManager.startPlay(strings);
                }
                lastPosition = position;
                notifyDataSetChanged();

            }
        });

        holder.setOnClickListener(R.id.item_voice_pause, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceManager.isPlaying() && lastPosition == position) {
                    holder.setImageResource(R.id.item_voice_pause, R.mipmap.icon_play);
                } else {
                    holder.setImageResource(R.id.item_voice_pause, R.mipmap.icon_pause);
                }
                voiceManager.continueOrPausePlay();
            }
        });

    }

    private void restoreSeekBar() {
        for (int i = 0; i < getItemCount(); i++) {
            if (seekBars.get(i) != null) {
                seekBars.get(i).setProgress(0);
            }
        }

    }
}
