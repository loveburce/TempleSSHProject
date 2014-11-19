package com.elight.teaching.activity;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.adapter.ThemeMusicFragmentAdapter;
import com.elight.teaching.custom.ColumnHorizontalScrollView;
import com.elight.teaching.fragment.ThemeMusicFragment;
import com.elight.teaching.utils.ActivityUtils;


public class ThemeMusicActivity extends FragmentActivity {
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private List<String> tags = new ArrayList<String>();
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;
    private ViewPager mViewPager;
    private ImageView button_more_columns;
    private int columnSelectIndex = 0;
    public ImageView shade_left;
    public ImageView shade_right;
    private int mScreenWidth = 0;
    private int mItemWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private int count = 16;
    public int bookNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_music);

        bookNumber = getIntent().getIntExtra("position",0);

        Log.d("wangshengyanoopopopopopop", "oopopopopop position: 2 " + bookNumber);
        mScreenWidth = ActivityUtils.getScreenWidth(this);
        mItemWidth = mScreenWidth / count + 100;     //添加长度
        initView();
    }


    private void initView() {
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);

        setChangelView();
    }

    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    private void initColumnData() {
         for(int i=0; i<16; i++){
             tags.add(" 第 "+(i+1)+" 课 ");
         }
    }

    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = 16;
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 8;
            params.rightMargin = 8;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(tags.get(i));
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
//			          Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }

    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }

        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    private void initFragment() {
        int count = tags.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("lessonNumber", (i+1)+"");
            data.putString("bookNumber", (bookNumber+1)+"");
            ThemeMusicFragment bookShelfFragment = new ThemeMusicFragment();

            bookShelfFragment.setArguments(data);
            fragments.add(bookShelfFragment);

        }
        ThemeMusicFragmentAdapter mAdapetr = new ThemeMusicFragmentAdapter(getSupportFragmentManager(), fragments);
//		mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }

    public OnPageChangeListener pageListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };



}
