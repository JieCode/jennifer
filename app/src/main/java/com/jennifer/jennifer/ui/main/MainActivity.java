package com.jennifer.jennifer.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;
import com.jennifer.jennifer.ui.environment.EnvironmentTestActivity;
import com.jennifer.jennifer.ui.main.adapter.MainAdapter;
import com.jennifer.jennifer.ui.palette.PaletteActivity;
import com.jennifer.jennifer.ui.parallax.GuideActivity;
import com.jennifer.jennifer.ui.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, MainAdapter.OnItemClickListener {

    private RecyclerView rvMain;
    private MainAdapter adapter;
    private List<String> lstData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        lstData = new ArrayList<>();
        String[] strings = getResources().getStringArray(R.array.main_guide);
        for (String str : strings)
            lstData.add(str);
    }

    private void initView() {
        setToolbarVisible(View.GONE);
        rvMain = findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMain.setLayoutManager(layoutManager);
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
        }
    }
}
