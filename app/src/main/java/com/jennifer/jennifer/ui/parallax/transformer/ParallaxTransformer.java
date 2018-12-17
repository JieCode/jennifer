package com.jennifer.jennifer.ui.parallax.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class ParallaxTransformer implements ViewPager.PageTransformer {
    private float parallaxCoefficient;
    private float distanceCoefficient;
    private SparseArray<int[]> layoutViewMap;

    public ParallaxTransformer(float parallaxCoefficient, float distanceCoefficient, SparseArray<int[]> layoutViewMap) {
        this.parallaxCoefficient = parallaxCoefficient;
        this.distanceCoefficient = distanceCoefficient;
        this.layoutViewMap = layoutViewMap;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(@NonNull View page, float position) {
        float scrollXOffset = page.getWidth() * parallaxCoefficient;
        int key = page.getId();
        int[] layer = layoutViewMap.get(key) == null ? new int[0] : layoutViewMap.get(key);
        if (layer.length > 0)
            for (int id : layer) {
                View view = page.findViewById(id);
                if (view != null) {
                    view.setTranslationX(scrollXOffset * position);
                }
                scrollXOffset *= distanceCoefficient;
            }

    }
}
