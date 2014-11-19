package com.elight.teaching.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import com.elight.teaching.ActivityManager;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.config.Constants;
import com.elight.teaching.custom.HeaderLayout;
import com.elight.teaching.custom.dialog.DialogTips;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.utils.CollectionUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

//2674486148


public abstract class BaseActivity extends FragmentActivity {

	public static final String TAG = BaseActivity.class.getSimpleName();

    Toast toast;

    BmobUserManager userManager;
    BmobChatManager manager;

    CustomApplication mApplication;
    protected HeaderLayout mHeaderLayout;

    protected int mScreenWidth;
    protected int mScreenHeight;
    ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);


        userManager = BmobUserManager.getInstance(this);
        manager = BmobChatManager.getInstance(this);
        mApplication = CustomApplication.getInstance();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
	}

	/**
	 * 绑定控件id
	 */
	protected abstract void findViewById();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 通过类名启动Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 通过Action启动Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 通过Action启动Activity，并且含有Bundle数据
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**加载进度条*/
	public void showProgressDialog() {
		
		if(progressDialog!=null){
			progressDialog.cancel();
		}
		progressDialog = new ProgressDialog(this);
		Drawable drawable = getResources().getDrawable(R.drawable.loading_animation);
		progressDialog.setIndeterminateDrawable(drawable);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
		progressDialog.setMessage("请稍候，正在努力加载。。");
		progressDialog.show();
	}

    public void cancelProgressDialog(){
        if(progressDialog!=null){
            progressDialog.cancel();
        }
    }

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (toast == null) {
                        toast = Toast.makeText(getApplicationContext(), text,
                                Toast.LENGTH_LONG);
                    } else {
                        toast.setText(text);
                    }
                    toast.show();
                }
            });
        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (toast == null) {
                    toast = Toast.makeText(BaseActivity.this.getApplicationContext(), resId,
                            Toast.LENGTH_LONG);
                } else {
                    toast.setText(resId);
                }
                toast.show();
            }
        });
    }

    /* title 的初始化*/
    public void initTopBarForOnlyTitle(String titleName) {
        mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    /**
     * 初始化标题栏-带左右按钮
     * @return void
     * @throws
     */
    public void initTopBarForBoth(String titleName, int rightDrawableId,String text,
                                  HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId,text,
                listener);
    }

    public void initTopBarForBoth(String titleName, int rightDrawableId,
                                  HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    /**
     * 只有左边按钮和Title initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeft(String titleName) {
        mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
    }

    public void showOfflineDialog(final Context context) {
        DialogTips dialog = new DialogTips(this,"您的账号已在其他设备上登录!", "重新登录");
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                CustomApplication.getInstance().logout();
                startActivity(new Intent(context, UserLoginActivity.class));
                finish();
                dialogInterface.dismiss();
            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }

    // 左边按钮的点击事件
    public class OnLeftButtonClickListener implements
            HeaderLayout.onLeftImageButtonClickListener {

        @Override
        public void onClick() {
            finish();
        }
    }

    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void updateUserInfo(){
        //更新地理位置
        updateUserLocation();
        //查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，
        // 如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
        //这里默认采取的是登陆成功之后即将好友列表存储到数据库中，并更新到当前内存中.
        userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
            @Override
            public void onSuccess(List<BmobChatUser> users) {
                // 保存到application中方便比较
                CustomApplication.getInstance().setContactList(CollectionUtils.list2map(users));
            }

            @Override
            public void onError(int i, String s) {
                if(i== BmobConfig.CODE_COMMON_NONE){

                }else{

                }
            }
        });
    }

    public void updateUserLocation(){
        if(CustomApplication.lastPoint != null){
            String saveLatitude = mApplication.getLatitude();
            String saveLongtitude = mApplication.getLongitude();
            String newLat = String.valueOf(CustomApplication.lastPoint.getLatitude());
            String newLong = String.valueOf(CustomApplication.lastPoint.getLongitude());
            //只有位置有变化就更新当前位置，达到实时更新的目的
            if( !saveLatitude.equals(newLat) || !saveLongtitude.equals(newLong)){
                final UserInfo user = userManager.getCurrentUser(UserInfo.class);
                user.setLocation(CustomApplication.lastPoint);
                user.update(BaseActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        CustomApplication.getInstance().setLatitude(String.valueOf(user.getLocation().getLatitude()));
                        CustomApplication.getInstance().setLongitude(String.valueOf(user.getLocation().getLongitude()));
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        }
    }

}
