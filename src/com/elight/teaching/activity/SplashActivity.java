package com.elight.teaching.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import cn.bmob.im.BmobChat;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.config.Constants;

public class SplashActivity extends BaseActivity {

    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;

	public static final String TAG = SplashActivity.class.getSimpleName();

	private ImageView mSplashItem_iv = null;

    private LocationClient locationClient;
    //注册广播接收器，用于监听网络及验证key
    private BaiduReceiver baiduReceiver;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mHandler = new Handler(getMainLooper());
		findViewById();
		initView();

        //BmobIM SDK的初始化 -- 只需要这一段代码既可以完成初始化
        BmobChat.getInstance(this).init(Constants.applicationId);

        //开启定位
        initLocationClient();
        // 注册地图SDK 广播监听者
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        intentFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        baiduReceiver = new BaiduReceiver();
        registerReceiver(baiduReceiver, intentFilter);

        if(userManager.getCurrentUser() != null){
            // 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
            updateUserInfo();
            mHandler.sendEmptyMessageAtTime(GO_HOME, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        }
	}

    private void initLocationClient(){
//        locationClient = CustomApplication.getInstance().mLocationClient;
//        LocationClientOption option = new LocationClientOption();
//        //设置定位模式：高精度模式
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        //设置坐标类型，百度经纬度
//        option.setCoorType("bd09ll");
//        //设置发起定位请求的间隔时间为1000ms:低于1000为手动定位一次，大于或等于1000则为定时定位
//        option.setScanSpan(1000);
//        //设置需不需要地址信息：
//        option.setIsNeedAddress(false);
//        locationClient.setLocOption(option);
//        locationClient.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    openActivity(HomeActivity.class);
                    finish();
                    break;
                case GO_LOGIN:
                    openActivity(UserChooseActivity.class);
                    finish();
                    break;
            }
        }
    };

   @Override
    protected void findViewById() {
        // TODO Auto-generated method stub
        mSplashItem_iv = (ImageView) findViewById(R.id.splash_loading_item);
    }

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		Animation translate = AnimationUtils.loadAnimation(this,
				R.anim.splash_loading);
		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				//启动homeactivty，相当于Intent
//				openActivity(HomeActivity.class);
                openActivity(UserChooseActivity.class);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				SplashActivity.this.finish();
			}
		});
		mSplashItem_iv.setAnimation(translate);
	}

    /*构造广播监听者，监听SDK key 验证以及网络异常*/
    public class BaiduReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            if(str.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)){
                //提示
            } else if(str.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)){
                //提示
            }
        }
    }

    @Override
    protected void onDestroy() {
        //退出时销毁定位
        if(locationClient != null && locationClient.isStarted()){
            locationClient.stop();
        }
        unregisterReceiver(baiduReceiver);
        super.onDestroy();
    }
}
