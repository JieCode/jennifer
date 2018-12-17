package com.jennifer.jennifer.ui.parallax;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;
import com.jennifer.jennifer.ui.parallax.adapter.GuideAdapter;
import com.jennifer.jennifer.ui.parallax.fragment.BaseGuideFragment;
import com.jennifer.jennifer.ui.parallax.fragment.GuideCoachFragment;
import com.jennifer.jennifer.ui.parallax.fragment.GuideCoachMoreFragment;
import com.jennifer.jennifer.ui.parallax.fragment.GuideRecommendFragment;
import com.jennifer.jennifer.ui.parallax.transformer.ParallaxTransformer;

public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private final float PARALLAX_COEFFICIENT = 1.2f;
    private final float DISTANCE_COEFFICIENT = 0.5f;
    private ViewPager viewPager;
    private GuideAdapter guideAdapter;
    private SparseArray<int[]> layoutViewMap = new SparseArray<int[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
        guideAdapter = new GuideAdapter(getSupportFragmentManager());
        addGuide(new GuideRecommendFragment());
        addGuide(new GuideCoachFragment());
        addGuide(new GuideCoachMoreFragment());
        viewPager.setAdapter(guideAdapter);
        viewPager.setPageTransformer(true, new ParallaxTransformer(PARALLAX_COEFFICIENT, DISTANCE_COEFFICIENT, layoutViewMap));
        viewPager.addOnPageChangeListener(this);
        setTitleText(R.string.main_guide_parallax);

    }

    private void addGuide(BaseGuideFragment fragment) {
        guideAdapter.addItem(fragment);
        layoutViewMap.put(fragment.getRootViewId(), fragment.getChildViewIds());
    }

    @Override
    public void onClick(View v) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
