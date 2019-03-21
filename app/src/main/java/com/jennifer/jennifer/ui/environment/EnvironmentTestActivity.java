package com.jennifer.jennifer.ui.environment;

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
    private List<String> lstApi;
    private static final String TAG = "EnvironmentTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_test);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        lstApi = new ArrayList<>();
        lstApi.add(HSS_API);
        lstApi.add(FEP_API);
        lstApi.add(NETEASE_API);
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
                tvNet.setEnabled(false);
                StringBuilder stringBuilder = new StringBuilder(tvContent.getText().toString().trim());
                for (String api : lstApi) {
                    PingNetEntity pingNetEntity = new PingNetEntity(api, 3, 5, new StringBuffer());
                    pingNetEntity = PingNet.ping(pingNetEntity);
                    stringBuilder.append(pingNetEntity.getIp() + " 耗时：" + pingNetEntity.getPingTime() + "\n");
                    tvContent.setText(stringBuilder.toString());
                }
                tvNet.setEnabled(true);
                break;
            case R.id.tv_content:
                break;
        }
    }
}
