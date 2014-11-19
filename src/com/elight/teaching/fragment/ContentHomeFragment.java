package com.elight.teaching.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.elight.teaching.R;

/**
 * Created by dawn on 2014/9/5.
 */
public class ContentHomeFragment extends BaseFragment {

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new ListInfoFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }

    private View layoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_content_center,null);

        if(savedInstanceState ==null){
            BaseFragment fragment = (BaseFragment)getChildFragmentManager().findFragmentByTag(0+"");// getActivity().getSupportFragmentManager().findFragmentByTag(index+"");
            if(fragment==null){
                initFragment(0);
            }
        }

        return layoutView;
    }

    private void initFragment(int index){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.content_center_fragment, MainThemeFragment.newInstance(index) ,index+"");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
