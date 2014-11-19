package com.elight.teaching.custom;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dawn on 2014/9/25.
 */
public class CustomViewPage extends ViewPager {
    private boolean isEnable = true;

    public CustomViewPage(Context context) {
        super(context);
    }

    public CustomViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isEnable){
            try{
                return super.onInterceptTouchEvent(ev);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isEnable){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setAdapter(PagerAdapter adapter, int index){
        super.setAdapter(adapter);
        setCurrentItem(index, false);
    }

    public void setEnableTouchScroll(boolean isEnable){
        this.isEnable = isEnable;
    }
}
