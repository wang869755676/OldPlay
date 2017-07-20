package com.td.oldplay.ui.forum.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.GlideImageLoader;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zuichu.picker.AudioPicker;
import me.zuichu.picker.FilePicker;
import me.zuichu.picker.ImagePicker;
import me.zuichu.picker.VideoPicker;
import me.zuichu.picker.bean.AudioItem;
import me.zuichu.picker.bean.FileItem;
import me.zuichu.picker.bean.ImageItem;
import me.zuichu.picker.bean.VideoItem;
import me.zuichu.picker.ui.audio.AudioGridActivity;
import me.zuichu.picker.ui.image.ImageGridActivity;
import me.zuichu.picker.ui.video.VideoGridActivity;

public class PublishForumActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.publish_title)
    EditText publishTitle;
    @BindView(R.id.publish_content)
    EditText publishContent;
    @BindView(R.id.publisd_video)
    TextView publisdVideo;
    @BindView(R.id.publisd_Audio)
    TextView publisdAudio;
    @BindView(R.id.publisd_pic)
    TextView publisdPic;
    @BindView(R.id.publish_send)
    TextView publishSend;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private int type; //0 发布 1 编辑

    private ImagePicker imagePicker;
    private VideoPicker videoPicker;
    private AudioPicker audioPicker;

    private List<Object> datas = new ArrayList<>();
    private MediaAdapter adapter;

    public static final int IMAGE = 100;
    public static final int AUDIO = 101;
    public static final int VIDEO = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_forum);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        initView();
    }

    private void initView() {
        if (type == 0) {
            title.setTitle("发布");
        } else {
            title.setTitle("编辑");
        }
        title.setOnLeftListener(this);
        publisdAudio.setOnClickListener(this);
        publisdPic.setOnClickListener(this);
        publisdVideo.setOnClickListener(this);
        publishSend.setOnClickListener(this);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setSelectLimit(20);
        imagePicker.setShowCamera(true);
        imagePicker.setMultiMode(true);

        videoPicker = VideoPicker.getInstance();
        videoPicker.setImageLoader(new GlideImageLoader());
        videoPicker.setSelectLimit(5);
        videoPicker.setShowCamera(true);
        videoPicker.setMultiMode(true);

        audioPicker = audioPicker.getInstance();
        audioPicker.setImageLoader(new GlideImageLoader());
        audioPicker.setSelectLimit(5);
        audioPicker.setShowCamera(true);
        audioPicker.setMultiMode(true);

        recycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter = new MediaAdapter(mContext, R.layout.item_medial, datas);
        recycler.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.publisd_Audio:
                intent = new Intent(this, AudioGridActivity.class);
                startActivityForResult(intent, AUDIO);
                break;
            case R.id.publisd_video:
                intent = new Intent(this, VideoGridActivity.class);
                startActivityForResult(intent, VIDEO);
                break;
            case R.id.publisd_pic:
                intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE);
                break;
            case R.id.publish_send:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_IMAGE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                datas.addAll(images);
                adapter.notifyDataSetChanged();
            }
        }

        if (resultCode == VideoPicker.RESULT_VIDEO_ITEMS) {
            if (data != null && requestCode == VIDEO) {
                ArrayList<VideoItem> videos = (ArrayList<VideoItem>) data.getSerializableExtra(VideoPicker.EXTRA_RESULT_VIDEO_ITEMS);
                datas.addAll(videos);
                adapter.notifyDataSetChanged();
            }
        }
        if (resultCode == AudioPicker.RESULT_AUDIO_ITEMS) {
            if (data != null && requestCode == AUDIO) {
                ArrayList<AudioItem> audios = (ArrayList<AudioItem>) data.getSerializableExtra(AudioPicker.EXTRA_RESULT_AUDIO_ITEMS);
                datas.addAll(audios);
                adapter.notifyDataSetChanged();
            }
        }

    }

    public class MediaAdapter extends CommonAdapter<Object> {
        public MediaAdapter(Context context, int layoutId, List<Object> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, Object o, final int position) {
            if (o instanceof ImageItem) {
                GlideUtils.setPhotoImage(mContext, ((ImageItem) o).path, (ImageView) holder.getView(R.id.item_media_iv));

            } else if (o instanceof VideoItem) {
                GlideUtils.setPhotoImage(mContext, ((VideoItem) o).path, (ImageView) holder.getView(R.id.item_media_iv));
            } else if (o instanceof AudioItem) {
                holder.setImageResource(R.id.item_media_iv,R.mipmap.icon_audi);
               // GlideUtils.setPhotoImage(mContext, ((VideoItem) o).path, (ImageView) holder.getView(R.id.item_media_iv));
            }
            holder.setOnClickListener(R.id.item_media_delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublishForumActivity.this.datas.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
