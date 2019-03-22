package com.jennifer.jennifer.ui.environment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.entity.PingNetEntity;
import com.jennifer.jennifer.util.PingNet;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentTestActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNet, tvContent;
    private static final String HSS_API = "api.hss.aicfe.cn";
    private static final String FEP_API = "fepapi.edu.web.sdp.101.com";
    private static final String NETEASE_API = "api.netease.im";
    private static final int UPDATE_CONTENT = 1111;
    private List<String> lstApi;
    private static final String TAG = "EnvironmentTestActivity";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_test);
        initData();
        initView();
        initListener();
    }

    @SuppressLint("HandlerLeak")
    private void initData() {
        lstApi = new ArrayList<>();
        lstApi.add(HSS_API);
        lstApi.add(FEP_API);
        lstApi.add(NETEASE_API);
        handler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_CONTENT:
                        tvContent.setText(tvContent.getText().toString() + msg.obj.toString());
                        break;
                }
            }
        };
    }

    private void initView() {
        tvNet = findViewById(R.id.tv_net);
        tvContent = findViewById(R.id.tv_content);
    }

    private void initListener() {
        tvNet.setOnClickListener(this);
        tvContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_net:
                for (final String api : lstApi) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PingNetEntity pingNetEntity = new PingNetEntity(api, 3, 5, new StringBuffer());
                            pingNetEntity = PingNet.ping(pingNetEntity);
                            Log.e("testPing",pingNetEntity.getIp());
                            Log.e("testPing","time="+pingNetEntity.getPingTime());
                            Log.e("testPing",pingNetEntity.isResult()+"");
                            Message msg = new Message();
                            msg.obj = pingNetEntity.getIp() + " 耗时：" + pingNetEntity.getPingTime() + "\n";
                            msg.what = UPDATE_CONTENT;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
        }
    }


}
