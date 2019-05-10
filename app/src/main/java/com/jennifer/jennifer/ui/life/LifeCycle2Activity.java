package com.jennifer.jennifer.ui.life;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;

public class LifeCycle2Activity extends BaseActivity implements OnClickListener {
    private static final String TAG = "LifeCycle2Activity";
    private TextView tv1, tv2, tv3;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        initView();
        Log.e(TAG, "B onCreate()");
    }

    private void initView() {
        setTitleText("生命周期B");
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setTitle("测试生命周期B");
        dialog.setContentView(R.layout.item_main);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv1:
                Intent intent = new Intent();
                intent.setClass(this, LifeCycleActivity.class);
                startActivity(intent);
                break;
            case R.id.tv2:
                dialog.show();
                break;
            case R.id.tv3:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "B onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "B onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "B onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "B onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "B onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "B onDestroy()");
    }
}
