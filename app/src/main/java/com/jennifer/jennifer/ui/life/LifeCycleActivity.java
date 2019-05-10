package com.jennifer.jennifer.ui.life;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;

public class LifeCycleActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "LifeCycleActivity";
    private TextView tv1, tv2, tv3;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        initView();
        Log.e(TAG, "A onCreate()");
    }

    private void initView() {
        setTitleText("生命周期A");
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setTitle("测试生命周期");
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
                intent.setClass(this, LifeCycle2Activity.class);
                startActivityForResult(intent,1001);
                break;
            case R.id.tv2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                break;
            case R.id.tv3:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "A onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "A onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "A onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "A onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "A onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "A onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"A onActivityResult()");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "A onRequestPermissionsResult()" );
    }
}
