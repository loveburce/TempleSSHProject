package com.elight.teaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.elight.teaching.R;
import com.elight.teaching.fragment.*;

public class HomeActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {MainThemeFragment.class,EventFragment.class, MessagesFragment.class,ContactFragment.class,MyNickpageFragment.class};

    private int mImageViewArray[] = {R.drawable.home_tab_main_selector,R.drawable.home_tab_category_selector,R.drawable.home_tab_message_selector,R.drawable.home_tab_selfinfo_selector,R.drawable.home_tab_more_selector};

    private String mTextviewArray[] = {"主页","动态", "消息", "联系人", "设置"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView(){
        layoutInflater = LayoutInflater.from(this);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }


    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
