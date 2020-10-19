package com.jennifer.jennifer.ui.environment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.entity.PingNetEntity;
import com.jennifer.jennifer.util.PingNet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private RadarView radarView;
    private Handler radarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_test);
        initData();
        initView();
        initListener();
        log();
    }

    private void log() {
        Log.e("build", "CPU_ABI:" + Build.CPU_ABI);
        Log.e("build", "CPU_ABI2:" + Build.CPU_ABI2);
        Log.e("build", "SUPPORTED_ABIS:" + Arrays.toString(Build.SUPPORTED_ABIS));

        for (String string : getCpuInfo()
        ) {
            Log.e("cpu info", string);
        }
        Log.e("build", "BOARD:" + Build.BOARD);
        Log.e("build", "BOOTLOADER:" + Build.BOOTLOADER);
        Log.e("build", "BRAND:" + Build.BRAND);
        Log.e("build", "CPU_ABI:" + Build.CPU_ABI);
        Log.e("build", "CPU_ABI2:" + Build.CPU_ABI2);
        Log.e("build", "DEVICE:" + Build.DEVICE);
        Log.e("build", "DISPLAY:" + Build.DISPLAY);
        Log.e("build", "FINGERPRINT:" + Build.FINGERPRINT);
        Log.e("build", "HARDWARE:" + Build.HARDWARE);
        Log.e("build", "HOST:" + Build.HOST);
        Log.e("build", "ID:" + Build.ID);
        Log.e("build", "MANUFACTURER:" + Build.MANUFACTURER);
        Log.e("build", "MODEL:" + Build.MODEL);
        Log.e("build", "PRODUCT:" + Build.PRODUCT);
        Log.e("build", "RADIO:" + Build.RADIO);
        Log.e("build", "TAGS:" + Build.TAGS);
        Log.e("build", "TIME:" + Build.TIME);
        Log.e("build", "TYPE:" + Build.TYPE);
        Log.e("build", "UNKNOWN:" + Build.UNKNOWN);
        Log.e("build", "USER:" + Build.USER);
        Log.e("build", "VERSION.CODENAME:" + Build.VERSION.CODENAME);
        Log.e("build", "VERSION.INCREMENTAL:" + Build.VERSION.INCREMENTAL);
        Log.e("build", "VERSION.RELEASE:" + Build.VERSION.RELEASE);
        Log.e("build", "VERSION.SDK:" + Build.VERSION.SDK);
        Log.e("build", "VERSION.SDK_INT:" + Build.VERSION.SDK_INT);
    }

    private String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        Log.e(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
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
        radarHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                radarView.setProgress(msg.what);
                addProgress(msg.what);
            }
        };
    }

    private void addProgress(int i) {
        if (i < 100)
            i++;
        else i = 0;
        radarHandler.sendEmptyMessageDelayed(i, 50);
    }

    private void initView() {
        tvNet = findViewById(R.id.tv_net);
        tvContent = findViewById(R.id.tv_content);
        radarView = findViewById(R.id.radar_view);
        radarView.setProgress(0);
        addProgress(0);
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
                            Log.e("testPing", pingNetEntity.getIp());
                            Log.e("testPing", "time=" + pingNetEntity.getPingTime());
                            Log.e("testPing", pingNetEntity.isResult() + "");
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
