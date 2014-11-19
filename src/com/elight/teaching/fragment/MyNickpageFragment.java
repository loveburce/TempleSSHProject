package com.elight.teaching.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import com.elight.teaching.R;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.widgets.CustomScrollView;

/**
 * Created by dawn on 2014/9/3.
 */
public class MyNickpageFragment extends BaseFragment{
    private ImageView backgroundImageView = null;
    private View layoutView = null;
    private CustomScrollView customScrollView = null;
    private TextView textViewName;


    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new ContactBookFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = (ViewGroup) inflater.inflate(R.layout.fragment_my_nick_page,null);
        UserInfo user = bmobUserManager.getCurrentUser(UserInfo.class);
        findViewById();
        initView();
        return layoutView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void findViewById(){
        backgroundImageView = (ImageView) layoutView.findViewById(R.id.personal_background_image);
        customScrollView = (CustomScrollView) layoutView.findViewById(R.id.personal_scrollView);
        textViewName = (TextView) layoutView.findViewById(R.id. fragment_my_nick_page_name);

    }

    protected void initView(){
        customScrollView.setImageView(backgroundImageView);
        textViewName.setText(bmobUserManager.getCurrentUserName()+"  欢迎来到育灵童教学助手");
    }



}
