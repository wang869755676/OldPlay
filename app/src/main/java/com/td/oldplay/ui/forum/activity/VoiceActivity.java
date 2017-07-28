package com.td.oldplay.ui.forum.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.permission.MPermission;
import com.td.oldplay.permission.annotation.OnMPermissionDenied;
import com.td.oldplay.permission.annotation.OnMPermissionGranted;
import com.td.oldplay.permission.annotation.OnMPermissionNeverAskAgain;
import com.td.oldplay.permission.util.MPermissionUtil;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.voicemanager.RecordVoiceButton;
import com.tencent.mm.opensdk.utils.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoiceActivity extends BaseFragmentActivity {
    @BindView(R.id.recordbtn)
    RecordVoiceButton recordbtn;
    public static final int REQUEST_PERMISSION_RECORD = 0x02;
    public static final String permissions = Manifest.permission.RECORD_AUDIO;
    private boolean isPermissionGrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);
        MPermission.with(this)
                .addRequestCode(REQUEST_PERMISSION_RECORD)
                .permissions(permissions)
                .request();
        recordbtn.setEnrecordVoiceListener(new RecordVoiceButton.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {
                if (isPermissionGrant) {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                    sendBroadcast(intent);
                    setResult(RESULT_OK, new Intent().putExtra("path", filePath));
                    finish();
                } else {
                    ToastUtil.show("权限不足");
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @OnMPermissionGranted(REQUEST_PERMISSION_RECORD)
    public void onLivePermissionGranted() {
        isPermissionGrant = true;

    }

    @OnMPermissionDenied(REQUEST_PERMISSION_RECORD)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, new String[]{permissions});
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法录音";
        ToastUtil.show(tip);
    }

    @OnMPermissionNeverAskAgain(REQUEST_PERMISSION_RECORD)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, new String[]{permissions});
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, new String[]{permissions});
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启录音，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        ToastUtil.show(sb);
    }

}
