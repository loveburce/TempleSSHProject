package com.elight.teaching.fragment;

import android.os.Bundle;
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
public class MessagesFragment extends BaseFragment implements View.OnClickListener{

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new MessagesFragment();
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

        layoutView = inflater.inflate(R.layout.fragment_message, null);

        layoutView.findViewById(R.id.messages_private_button).setOnClickListener(this);
        layoutView.findViewById(R.id.messages_private_button).setSelected(true);
        layoutView.findViewById(R.id.message_system_button).setOnClickListener(this);

        if(savedInstanceState ==null){
            BaseFragment fragment = (BaseFragment)getChildFragmentManager().findFragmentByTag(0+"");
            if(fragment==null){
                initFragment(0);
            }
        }

        return layoutView;
    }

    private void initFragment(int index) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.messages_fragment, MessagesPrivateFragment.newInstance(index) ,index+"");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        allNoSelect();

        switch (v.getId()) {
            case R.id.messages_private_button:
                placeView(0);
                break;
            case R.id.message_system_button:
                placeView(1);
                break;
            default:
                break;
        }

        v.setSelected(true);
    }

    private void allNoSelect(){
        layoutView.findViewById(R.id.messages_private_button).setSelected(false);
        layoutView.findViewById(R.id.message_system_button).setSelected(false);
    }

    public void placeView(int index){
        BaseFragment fragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(index+"");
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(fragment == null){
            switch (index){
                case 0:
                    fragment = MessagesPrivateFragment.newInstance(index);
                    break;
                case 1:
                    fragment = MessagesSystemFragment.newInstance(index);
                    break;
                default:
                    return;
            }
        }
        fragmentTransaction.replace(R.id.messages_fragment,fragment,index+"");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
