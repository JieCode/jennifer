package com.jennifer.jennifer.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.ui.SLPLoginActivity;
import com.jennifer.jennifer.ui.canvas.CanvasActivity;
import com.jennifer.jennifer.ui.ceiling.CeilingActivity;
import com.jennifer.jennifer.ui.environment.EnvironmentTestActivity;
import com.jennifer.jennifer.ui.life.LifeCycleActivity;
import com.jennifer.jennifer.ui.main.adapter.MainAdapter;
import com.jennifer.jennifer.ui.notification.NotificationActivity;
import com.jennifer.jennifer.ui.palette.PaletteActivity;
import com.jennifer.jennifer.ui.parallax.GuideActivity;
import com.jennifer.jennifer.ui.radio.RecycleViewRadioButtonActivity;
import com.jennifer.jennifer.ui.speech.SpeechRecognizerActivity;
import com.jennifer.jennifer.ui.tab.TabActivity;
import com.jennifer.jennifer.ui.trtc.TRTCVideoTestActivity;
import com.jennifer.jennifer.ui.video.VideoViewActivity;
import com.jennifer.jennifer.ui.web.WebViewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainAdapter.OnItemClickListener {

    private RecyclerView rvMain;
    private MainAdapter adapter;
    private List<String> lstData;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        lstData = new ArrayList<>();
        lstData.addAll(Arrays.asList(getResources().getStringArray(R.array.main_guide)));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        rvMain = findViewById(R.id.rv_main);
        rvMain.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MainAdapter(lstData, this);
        rvMain.setAdapter(adapter);
        adapter.setItemClickListener(this);
    }

    private void intentClass(Class intentClass) {
        Intent intent = new Intent(this, intentClass);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                intentClass(GuideActivity.class);
                break;
            case 1:
                intentClass(WebViewActivity.class);
                break;
            case 2:
                intentClass(PaletteActivity.class);
                break;
            case 3:
                intentClass(EnvironmentTestActivity.class);
                break;
            case 4:
                intentClass(TRTCVideoTestActivity.class);
                break;
            case 5:
                intentClass(LifeCycleActivity.class);
                break;
            case 6:
                intentClass(RecycleViewRadioButtonActivity.class);
                break;
            case 7:
                intentClass(SpeechRecognizerActivity.class);
                break;
            case 8:
                intentClass(VideoViewActivity.class);
                break;
            case 9:
                intentClass(NotificationActivity.class);
                break;
            case 10:
                intentClass(SLPLoginActivity.class);
                break;
            case 11:
                intentClass(CanvasActivity.class);
                break;
            case 12:
                intentClass(TabActivity.class);
                break;
            case 13:
                intentClass(CeilingActivity.class);
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
