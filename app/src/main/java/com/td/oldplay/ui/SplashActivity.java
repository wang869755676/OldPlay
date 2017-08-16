package com.td.oldplay.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.ui.live.LiveLoginHelper;
import com.td.oldplay.utils.ToastUtil;
import com.tencent.ilivesdk.ILiveCallBack;
;


/**
 * Created by my on 2017/6/26.
 */

public class SplashActivity extends BaseFragmentActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (spUilts.getIsLogin()) {
                    LiveLoginHelper.iLiveLogin(userBean.userId, userBean.userSig, new ILiveCallBack() {
                        @Override
                        public void onSuccess(Object data) {
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            ToastUtil.show(errMsg + "errcode: " + errCode);
                        }
                    });

                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
    /*   // 自定义图片加载器
    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            // TODO 在这边可以自定义图片加载库来加载ImageView，例如Glide、Picasso、ImageLoader等
            Glide.with(context).load(path).into(imageView);
        }
    };


    public void Single(View view) {

        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                // 是否多选
                .multiSelect(false)
                .btnText("Confirm")
                // 确定按钮背景色
                //.btnBgColor(Color.parseColor(""))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.drawable.abc_ic_ab_back_material)
                .title("Images")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#3F51B5"))
                .allImagesText("All Images")
                .needCrop(false)
                .cropSize(1, 1, 200, 200)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9)
                .build();

        ImgSelActivity.startActivity(this, config, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);

            // 测试Fresco。可不理会
            // draweeView.setImageURI(Uri.parse("file://"+pathList.get(0)));
            for (String path : pathList) {
                //  tvResult.append(path + "\n");
            }
        }
    }*/
}
