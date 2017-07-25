package com.td.oldplay.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.GlideImageLoader;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CircleImageView;
import com.td.oldplay.widget.CustomTitlebarLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tee3.avd.User;
import io.reactivex.disposables.Disposable;
import me.zuichu.picker.ImagePicker;
import me.zuichu.picker.bean.ImageItem;
import me.zuichu.picker.ui.image.ImageGridActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.PartMap;

public class PersonDetailActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.person_iv)
    CircleImageView personIv;
    @BindView(R.id.person_name)
    EditText personName;
    @BindView(R.id.person_save)
    Button personSave;

    private HashMap<String, RequestBody> params = new HashMap<>();
    private String name;
    private ImagePicker imagePicker;
    private String filePath;
    private MultipartBody.Part part;
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        ButterKnife.bind(this);
        userBean = (UserBean) getIntent().getSerializableExtra("model");
        title.setTitle("个人资料");
        title.setOnLeftListener(this);
        personSave.setOnClickListener(this);
        personIv.setOnClickListener(this);
        params.put("userId", toRequestBody(userId));
        if (userBean != null) {
            GlideUtils.setAvatorImage(mContext, userBean.avatar, personIv);
            personName.setText(userBean.nickName);
        }
        initPicker();

    }

    private RequestBody toRequestBody(String userId) {
        return RequestBody.create(MediaType.parse("text.plain"), userId);
    }

    private void initPicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(1);
        imagePicker.setCrop(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_iv:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.person_save:
                name = personName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.show("请输入昵称");
                    return;
                }
                params.put("nickName", toRequestBody(name));
                if (!TextUtils.isEmpty(filePath)) {
                    //        1、根据地址拿到File
                    File file = new File(filePath);

//        2、创建RequestBody，其中`multipart/form-data`为编码类型
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

//        3、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
                    part = MultipartBody.Part.createFormData("picFile", file.getName(), requestFile);
                    params.put("picFile\"; filename=\""+file.getName(),requestFile);
                }

                saveSerer();
                break;
            case R.id.left_text:
                finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_IMAGE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    GlideUtils.setAvatorLoadImage(mContext, images.get(0).path, personIv);
                    filePath = images.get(0).path;
                }
            } else {
                // Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveSerer() {
        HttpManager.getInstance().modifyUser(params, new HttpSubscriber<UserBean>(new OnResultCallBack<UserBean>() {

            @Override
            public void onSuccess(UserBean userBean) {
                ToastUtil.show("修改成功");
                spUilts.setUser(userBean);
                EventBus.getDefault().post(new EventMessage("userUpdate"));
                finish();
            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
       /* HttpManager.getInstance().modifyUser(params, part, new HttpSubscriber<UserBean>(new OnResultCallBack<UserBean>() {

            @Override
            public void onSuccess(UserBean userBean) {
                ToastUtil.show("修改成功");
                spUilts.setUser(userBean);
                EventBus.getDefault().post(new EventMessage("userUpdate"));
                finish();
            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }
        }));*/
    }
}
