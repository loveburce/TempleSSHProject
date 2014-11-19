package com.elight.teaching.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by dawn on 2014/9/3.
 */
public class TouristThemeFragment extends BaseFragment {

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new TouristThemeFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }

    private View layoutView;

}
