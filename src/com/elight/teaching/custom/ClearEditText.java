package com.elight.teaching.custom;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import com.elight.teaching.R;

/**
 * Created by dawn on 2014/9/28.
 */
public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher{

    //删除按钮的yinyon
    private Drawable clearDrawable;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        //获取EditText 的 drawableRight，假如没有设置我们就是用默认的图片
        clearDrawable = getCompoundDrawables()[2];
        if(clearDrawable == null){
            clearDrawable = getResources().getDrawable(R.drawable.search_clear);
        }
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /*
    * 因为我们不能直接给EditText 设置点击事件，所以我们应记住我们按下的位置来模拟点击事件
    * 当我们按下的位置在EditText 的宽度 - 图标到控件右边的问题 - 图标宽度 和
    * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
    * */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(getCompoundDrawables()[2] != null){
            if(event.getAction() == MotionEvent.ACTION_UP){
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth())
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if(touchable){
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /*当clearEditText 的焦点发生变化时，判断里面字符串长度 设置清除图标的显示与隐藏*/
     @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    /*设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去*/
    protected void setClearIconVisible(boolean visible){
        Drawable right = visible ? clearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /*当输入框里面内容发生变化的时候回调的方法*/

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        setClearIconVisible(text.length() > 0);
    }

    /*设置晃动动画*/
    public void setShakeAnimation(){
        this.setAnimation(shakeAnimation(5));
    }

    /*晃动动画*/
    public static Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
}
