package com.jennifer.jennifer.ui.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.ui.main.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jingjie
 * @date :2021/4/20 10:00
 * TODO：跳转到其它页面
 */
public class IntentActivity extends AppCompatActivity {
    private RecyclerView rvMain;
    private MainAdapter adapter;
    private List<String> lstData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        initData();
        initView();
    }

    private void initData() {
        lstData.add("打开AI好老师");
    }

    private void initView() {
        rvMain = findViewById(R.id.rv_main);
        rvMain.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MainAdapter(lstData, this);
        rvMain.setAdapter(adapter);
        adapter.setItemClickListener(onItemClickListener);
    }

    /**
     * 适配器点击事件
     */
    private final MainAdapter.OnItemClickListener onItemClickListener = new MainAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Uri uri = Uri.parse("aiteacher://cn.edu.bnu.ai_teacher?keyword=333&&");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra("toast","我来了");
            startActivity(intent);
        }
    };
}