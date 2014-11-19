package com.elight.teaching.custom;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.elight.teaching.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by dawn on 2014/9/11.
 */
public class DrawerView implements View.OnClickListener {
    private final Activity activity;
    SlidingMenu slidingMenu;

    public DrawerView(Activity activity) {
        this.activity = activity;
    }

    public SlidingMenu initSlidingMenu(){
        slidingMenu = new SlidingMenu(activity);
        slidingMenu.setMode(SlidingMenu.LEFT);  //设置左滑菜单
//        slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);  //设置要使菜单滑动的触碰屏幕的范围
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  //设置要使菜单滑动的触碰屏幕的范围
//        slidingMenu.setTouchModeBehind(SlidingMenu.RIGHT);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);    //设置阴影图片的宽度
        slidingMenu.setShadowDrawable(R.drawable.shadow);        //设置阴影图片
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);     //SlidingMenu划出时主页面显示的剩余宽度
        slidingMenu.setFadeDegree(0.35f);   //slidingMenu 滑动时的渐变程度
        slidingMenu.attachToActivity(activity, SlidingMenu.LEFT); //使slidingMenu 附加在activity右边
//        slidingMenu.setShadowWidthRes(R.dimen.left_drawer_avatar_size);            //设置SlidingMenu菜单的宽度
        slidingMenu.setMenu(R.layout.fragment_course_show_sliding);
        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {

            }
        });

        return slidingMenu;
    }


    @Override
    public void onClick(View v) {

    }
}
