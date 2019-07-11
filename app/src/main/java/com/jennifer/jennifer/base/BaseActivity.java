package com.jennifer.jennifer.base;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aitangba.swipeback.SwipeBackActivity;
import com.jennifer.jennifer.R;

public class BaseActivity extends SwipeBackActivity implements View.OnClickListener {
    private LinearLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里
    private Toolbar toolbar;
    private ImageView ivBack, ivMore;
    private TextView tvTitle, tvMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppBarLayout appBarLayout = findViewById(R.id.appbar);
            appBarLayout.setPadding(0, (int) (getResources().getDisplayMetrics().density * 25), 0, 0);
        }
        initToolBar();
    }

    /**
     * 初始化contentview
     */
    private void initContentView(int layoutResID) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

    }

    @Override
    public void setContentView(View view) {

        parentLinearLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        parentLinearLayout.addView(view, params);

    }

    private void initToolBar() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvMore = findViewById(R.id.tv_more);
        ivMore = findViewById(R.id.iv_more);
        toolbar = findViewById(R.id.toolbar);
        ivBack.setOnClickListener(this);
    }

    public void setToolbarVisible(int visibility) {
        toolbar.setVisibility(visibility);
    }

    public void setBack(int backId) {
        ivBack.setImageResource(backId);
    }

    public void setTitleText(String title) {
        tvTitle.setText(title);
    }

    public void setTitleText(int titleId) {
        tvTitle.setText(titleId);
    }

    public String getTitleText() {
        return tvTitle.getText().toString() == null ? "" : tvTitle.getText().toString();
    }

    public void setMoreText(String more) {
        tvMore.setText(more);
    }


    public void setMoreText(int moreId) {
        tvMore.setText(moreId);
    }

    public String getMoreText() {
        return tvMore.getText().toString() == null ? "" : tvMore.getText().toString();
    }

    public void setMoreImage(int moreId) {
        ivMore.setImageResource(moreId);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
