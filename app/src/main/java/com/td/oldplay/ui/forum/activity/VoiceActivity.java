package com.td.oldplay.ui.forum.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.voicemanager.RecordVoiceButton;
import com.tencent.mm.opensdk.utils.Log;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoiceActivity extends BaseFragmentActivity {
    @BindView(R.id.recordbtn)
    RecordVoiceButton recordbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);

        recordbtn.setEnrecordVoiceListener(new RecordVoiceButton.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {
                Log.e("===",filePath+"lujing");
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                sendBroadcast(intent);
                setResult(RESULT_OK,new Intent().putExtra("path",filePath));
            }
        });
    }
}
