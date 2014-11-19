package com.elight.teaching.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.elight.teaching.R;

/**
 * Created by dawn on 2014/9/3.
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener {

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new ContactFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_contacts, null);

        layoutView.findViewById(R.id.button1).setOnClickListener(this);
        layoutView.findViewById(R.id.button1).setSelected(true);
        layoutView.findViewById(R.id.button2).setOnClickListener(this);
        layoutView.findViewById(R.id.button3).setOnClickListener(this);

        if(savedInstanceState ==null){
            BaseFragment fragment = (BaseFragment)getChildFragmentManager().findFragmentByTag(0+"");// getActivity().getSupportFragmentManager().findFragmentByTag(index+"");
            if(fragment==null){
                initFragment(0);
            }
        }

        return layoutView;
    }



    @Override
    public void onClick(View v) {

        allNoSelect();

        switch (v.getId()) {
            case R.id.button1:
                placeView(0);
                break;
            case R.id.button2:
                placeView(1);
                break;
            case R.id.button3:
                placeView(2);
                break;
            default:
                break;
        }

        v.setSelected(true);
    }

    private void allNoSelect() {
        layoutView.findViewById(R.id.button1).setSelected(false);
        layoutView.findViewById(R.id.button2).setSelected(false);
        layoutView.findViewById(R.id.button3).setSelected(false);
    }

    private void initFragment(int index) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.realtabcontent, ContactBookFragment.newInstance(index) ,index+"");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void placeView(int index){

        BaseFragment fragment = (BaseFragment)getChildFragmentManager().findFragmentByTag(index+"");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        if (fragment == null ) {
            switch (index) {
                case 0:
                    fragment = ContactBookFragment.newInstance(index);
                    break;
                case 1:
                    fragment = ContactRecentlyFragment.newInstance(index);
                    break;
                case 2:
                    fragment = ContactNearbyFragment.newInstance(index);
                    break;
                default:
                    return;
            }

        }
        ft.replace(R.id.realtabcontent, fragment,index+"");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

    }
}
