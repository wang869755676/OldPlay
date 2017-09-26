package com.td.oldplay.ui.forum.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.GlideImageLoader;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.bean.ForumDetial;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.ProgressListener;
import com.td.oldplay.http.UploadFileRequestBody;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.tencent.mm.opensdk.utils.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.td.oldplay.http.exception.ApiException.Code_TimeOut;

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
    private String id;

    private ImagePicker imagePicker;
    private VideoPicker videoPicker;
    private AudioPicker audioPicker;

    private List<Object> datas = new ArrayList<>();
    private MediaAdapter adapter;

    public static final int IMAGE = 100;
    public static final int AUDIO = 101;
    public static final int VIDEO = 102;

    private HashMap<String, Object> paramC = new HashMap<>();
    private HashMap<String, RequestBody> paramPi = new HashMap<>();
    private HashMap<String, RequestBody> paramVi = new HashMap<>();
    private HashMap<String, RequestBody> paramVo = new HashMap<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<String> videList = new ArrayList<>();
    private ArrayList<String> audioList = new ArrayList<>();

    private String titleS;
    private String content;
    private String contentId;

    private File file;
    private RequestBody requestFile;
    private int successContent;
    private int count;
    private ForumDetial forumDetial;

    private boolean isOther;
    private long totalPic;
    private long totalVideo;
    private long totalAudio;
    private int currentPic;
    private int currentVideo;
    private int currentAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_forum);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        forumDetial = (ForumDetial) getIntent().getSerializableExtra("model");
        paramC.put("userId", userId);
        paramC.put("boardId", id);
        initView();
    }

    private void initView() {
        if (type == 0) {
            title.setTitle("发布");
        } else {
            title.setTitle("编辑");
            setForumData();
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

        progressListener = new ProgressListener() {
            @Override
            public void onProgress(int progress, String tag) {

            }

            @Override
            public void onDetailProgress(final long written, final long total, final String tag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // updateProgress((int) (100 * written / totalPic));
                        if ("pic".equals(tag)) {
                            currentPic += written;
                            updateProgress(currentPic);
                        } else if ("video".equals(tag)) {
                            currentVideo += written;
                            updateProgress(currentVideo);
                        } else if ("audio".equals(tag)) {
                            currentAudio += written;
                            updateProgress(currentAudio);
                        }

                    }
                });

                android.util.Log.e("===", written + "      " + totalAudio + "   " + currentAudio + "  " + total);
            }
        };

    }

    private void setForumData() {
        if (forumDetial != null) {
            if (forumDetial.topic != null) {
                paramC.put("boardId", forumDetial.topic.boardId);
                paramC.put("topicId", forumDetial.topic.topicId);
                publishContent.setText(forumDetial.topic.content);
                publishTitle.setText(forumDetial.topic.title);
            }

            if (forumDetial.imageUrlList != null) {
                for (String s : forumDetial.imageUrlList) {
                    datas.add(new ImageItem(s, 1));
                }
            }

            if (forumDetial.speechUrlList != null) {
                for (String s : forumDetial.speechUrlList) {
                    datas.add(new AudioItem(s, 1));
                }
            }
            if (forumDetial.videoUrlList != null) {
                for (String s : forumDetial.videoUrlList) {
                    datas.add(new VideoItem(s, 1));
                }
            }
        }


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
                if (checkInput()) {
                    publicOne();
                }
                break;
        }

    }

    private void publicOne() {
        ToastUtil.show("正在上传中");
        setMedia();
        HttpManager.getInstance().postForumContent(paramC, new HttpSubscriber<String>(new OnResultCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                contentId = s;
                Log.e("===", contentId + "---------------");
                if (forumDetial != null) {
                    contentId = forumDetial.topic.topicId;
                }
                publishOther();

            }

            @Override
            public void onError(int code, String errorMsg) {

                hideLoading();
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
    }

    private void setMedia() {
        totalAudio = 0;
        totalPic = 0;
        totalVideo = 0;
        currentAudio = 0;
        currentPic = 0;
        currentVideo = 0;
        if (datas != null && datas.size() > 0) {
            for (Object obj : datas) {
                if (obj instanceof ImageItem) {
                    if (((ImageItem) obj).type == 1) {
                        imageList.add((((ImageItem) obj).path));
                    } else {
                        file = new File(((ImageItem) obj).path);
                        totalPic += file.length();
                        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        paramPi.put("picFile\"; filename=\"" + file.getName(), new UploadFileRequestBody(requestFile, progressListener, "pic"));
                    }

                } else if (obj instanceof VideoItem) {

                    if (((VideoItem) obj).type == 1) {
                        videList.add(((VideoItem) obj).path);
                    } else {
                        file = new File(((VideoItem) obj).path);
                        totalVideo += file.length();
                        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        paramVi.put("picFile\"; filename=\"" + file.getName(), new UploadFileRequestBody(requestFile, progressListener, "video"));
                    }

                } else if (obj instanceof AudioItem) {

                    if (((AudioItem) obj).type == 1) {
                        audioList.add(((AudioItem) obj).path);
                    } else {

                        file = new File(((AudioItem) obj).path);
                        totalAudio += file.length();
                        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        paramVo.put("picFile\"; filename=\"" + file.getName(), new UploadFileRequestBody(requestFile, progressListener, "audio"));
                    }

                }
            }
            if (imageList.size() > 0) {
                paramC.put("imageUrlList", imageList.toString().replace("[", "").replace("]", ""));
            }
            if (videList.size() > 0) {
                paramC.put("videoUrlList", videList.toString().replace("[", "").replace("]", ""));

            }
            if (audioList.size() > 0) {
                paramC.put("speechUrlList", audioList.toString().replace("[", "").replace("]", ""));

            }

        }

    }

    private void publishOther() {
        paramPi.put("topicId", toRequestBody(contentId));
        paramVi.put("topicId", toRequestBody(contentId));
        paramVo.put("topicId", toRequestBody(contentId));
        if (paramVi.size() >= 2) {
            count++;
            isOther = true;

        }
        if (paramVo.size() >= 2) {
            count++;
            isOther = true;

        }
        if (paramPi.size() >= 2) {
            isOther = true;
            count++;
            updateProgressTitle("上传图片中", totalPic);
            HttpManager.getInstance().postForumPic(paramPi, new HttpSubscriber<String>(new OnResultCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    uploadAudio();


                }

                @Override
                public void onError(int code, String errorMsg) {
                    if (code == Code_TimeOut) {
                        uploadAudio();
                    } else {
                        hideLoading();
                        ToastUtil.show(errorMsg);
                    }

                }

                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }
            }));
        } else {
            uploadAudio();
        }


        if (!isOther) {
            EventBus.getDefault().post(new EventMessage("publish"));
            ToastUtil.show("发布成功");
            finish();
        }
    }

    private RequestBody toRequestBody(String para) {
        return RequestBody.create(MediaType.parse("text.plain"), para);
    }

    private boolean checkInput() {
        titleS = publishTitle.getText().toString();
        if (TextUtils.isEmpty(titleS)) {
            ToastUtil.show("请输入标题");
            return false;
        }
        paramC.put("title", titleS);


        content = publishContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show("请输入内容");
            return false;
        }
        paramC.put("content", content);

        return true;
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
                if (((ImageItem) o).type == 1) {
                    GlideUtils.setImage(mContext, ((ImageItem) o).path, (ImageView) holder.getView(R.id.item_media_iv));
                } else {
                    GlideUtils.setPhotoImage(mContext, ((ImageItem) o).path, (ImageView) holder.getView(R.id.item_media_iv));
                    Log.e("===", ((ImageItem) o).path);
                }


            } else if (o instanceof VideoItem) {
                if (((VideoItem) o).type == 1) {
                    GlideUtils.setImage(mContext, ((VideoItem) o).path, (ImageView) holder.getView(R.id.item_media_iv));
                } else {
                    GlideUtils.setPhotoImage(mContext, ((VideoItem) o).path, (ImageView) holder.getView(R.id.item_media_iv));
                }

            } else if (o instanceof AudioItem) {
                holder.setImageResource(R.id.item_media_iv, R.mipmap.icon_audi);

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

    private HttpSubscriber<String> MediaSubscriber = new HttpSubscriber<>(new OnResultCallBack<String>() {

        @Override
        public void onSuccess(String s) {
            finishPublish();


        }

        @Override
        public void onError(int code, String errorMsg) {

            if (code == Code_TimeOut)
                finishPublish();
            else {
                hideLoading();
                clearData(2);
                ToastUtil.show("上传视频出错");
            }
        }

        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }
    });


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ProgressListener progressListener;

    private void uploadAudio() {
        if (paramVo.size() >= 2) {
            updateProgressTitle("上传音频中", totalAudio);
            HttpManager.getInstance().postForumVoicec(paramVo, new HttpSubscriber<String>(new OnResultCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    uploadVideo();

                }

                @Override
                public void onError(int code, String errorMsg) {
                    if (code == Code_TimeOut)
                        uploadVideo();
                    else {
                        hideLoading();
                        clearData(1);
                        ToastUtil.show(errorMsg);
                    }
                }

                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }
            }));
        } else {
            uploadVideo();
        }

    }

    private void uploadVideo() {
        if (paramVi.size() >= 2) {
            updateProgressTitle("上传视频中", totalVideo);
            HttpManager.getInstance().postForumVideo(paramVi, MediaSubscriber);
        } else {
            finishPublish();

        }
    }

    private void finishPublish() {
        hideLoading();
        ToastUtil.show("发布成功");
        EventBus.getDefault().post(new EventMessage("publish"));
        finish();
    }

    /**
     * @param type 1 图片 2 音频 3 视频
     */
    private void clearData(int type) {
        Iterator<Object> ite = datas.iterator();
        Object obj = null;
        while (ite.hasNext()) {
            obj = ite.next();
            switch (type) {
                case 1:

                    paramPi.clear();
                    if (obj instanceof ImageItem) {
                        ite.remove();
                    }
                    break;
                case 2:
                    paramVo.clear();
                    if (obj instanceof AudioItem) {
                        ite.remove();
                    }
                    break;
                case 3:
                    paramVi.clear();
                    if (obj instanceof VideoItem) {
                        ite.remove();
                    }
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }
}
