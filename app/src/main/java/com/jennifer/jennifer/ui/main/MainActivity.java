package com.jennifer.jennifer.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;
import com.jennifer.jennifer.ui.parallax.GuideActivity;
import com.jennifer.jennifer.ui.web.WebViewActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvParallax, tvWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        setToolbarVisible(View.GONE);
        tvParallax = findViewById(R.id.tv_parallax);
        tvParallax.setOnClickListener(this);
        tvWeb = findViewById(R.id.tv_web);
        tvWeb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_parallax: {
                intentClass(GuideActivity.class);
                break;
            }
            case R.id.tv_web: {
                intentClass(WebViewActivity.class);
                break;
            }
        }
    }

    private void intentClass(Class intentClass) {
        Intent intent = new Intent(this, intentClass);
        startActivity(intent);
    }
}
