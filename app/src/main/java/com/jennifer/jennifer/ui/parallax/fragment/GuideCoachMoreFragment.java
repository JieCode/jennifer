package com.jennifer.jennifer.ui.parallax.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jennifer.jennifer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideCoachMoreFragment extends BaseGuideFragment {


    public GuideCoachMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_coach_more, container, false);
    }

    @Override
    public int[] getChildViewIds() {
        return new int[]{R.id.iv_coach_more_layer4, R.id.iv_coach_more_layer3, R.id.iv_coach_more_layer2, R.id.iv_coach_more_layer1, R.id.tv_start};
    }

    @Override
    public int getRootViewId() {
        return R.id.rl_coach_more;
    }
}
