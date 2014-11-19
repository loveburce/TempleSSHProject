package com.elight.teaching;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.datatype.BmobGeoPoint;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.elight.teaching.config.Constants;
import com.elight.teaching.entity.EventInfo;
import com.elight.teaching.entity.UserInfo;

import android.app.Application;
import android.content.res.Configuration;
import com.elight.teaching.utils.CollectionUtils;
import com.elight.teaching.utils.SharePreferenceUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class CustomApplication extends Application {
    public static CustomApplication mInstance;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    //上一次定位到的经纬度
    public static BmobGeoPoint lastPoint = null;
    MediaPlayer mMediaPlayer;

    NotificationManager mNotificationManager;

    // 单例模式，才能及时返回数据
    SharePreferenceUtils mSpUtil;
    public static final String PREFERENCE_NAME = "_shareInfo";

    public final String PREF_LONGITUDE = "longitude";// 经度
    private String longitude = "";
    public final String PREF_LATITUDE = "latitude";// 经度
    private String latitude = "";
    public final String PREF_ADDRESS = "address";// 经度
    private String address = "";

    private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

    //当前用户
    private EventInfo eventInfo = null;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

        BmobChat.DEBUG_MODE = true;
        mInstance = this;
        initBmobPushService();
        initTitleList();

        initBaidu();
        initImageLoader();
	}

    private void initBmobPushService(){
        mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        //若用户登陆过，则先从好友的数据库中取出好友list存入内存中
        if(BmobUserManager.getInstance(getApplicationContext()).getCurrentUser() != null){
            contactList = CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList());
        }

    }

    private void initBaidu(){

        Log.d("wangshengyanopopopopop","poppopopop : 0 ");

        SDKInitializer.initialize(this);
        initBaiduLocClient();
    }

    private void initBaiduLocClient(){

        Log.d("wangshengyanopopopopop","poppopopop : 1 ");

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }

    public static CustomApplication getInstance(){
        return mInstance;
    }

    public UserInfo getCurrentUser(){
        UserInfo userInfo = BmobChatUser.getCurrentUser(getApplicationContext(), UserInfo.class);
        if(userInfo != null){
            return userInfo;
        }
        return null;
    }

    public EventInfo getCurrentEventInfo(){
        return eventInfo;
    }

    public void setCurrentEventInfo(EventInfo eventInfo){
        this.eventInfo = eventInfo;
    }

    public void addActivity(Activity activity){
        ActivityManager.getInstance().addActivity(activity);
    }

    public void exit(){
        ActivityManager.getInstance().killAllActivity();

    }

    public Activity getTopActivity(){
        return ActivityManager.getInstance().getTopActivity();
    }


    public synchronized SharePreferenceUtils getSpUtil(){
        if(mSpUtil == null){
            String currentId = BmobUserManager.getInstance(getApplicationContext()).getCurrentUserObjectId();
            String sharedName = currentId + PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtils(this, sharedName);
        }
        return mSpUtil;
    }

    public NotificationManager getNotificationManager(){
        if(mNotificationManager == null){
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public synchronized MediaPlayer getMediaPlayer(){
        if(mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        }
        return mMediaPlayer;
    }

    public String getLongitude(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        longitude = preferences.getString(PREF_LONGITUDE,"");
        return longitude;
    }


    public void setLongitude(String lon){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LONGITUDE, lon).commit()) {
            longitude = lon;
        }
    }

    public String getLatitude() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        latitude = preferences.getString(PREF_LATITUDE, "");
        return latitude;
    }

    public void setLatitude(String lat) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LATITUDE, lat).commit()) {
            latitude = lat;
        }
    }


    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (lastPoint != null) {
                if (lastPoint.getLatitude() == location.getLatitude()
                        && lastPoint.getLongitude() == location.getLongitude()) {
//					BmobLog.i("两次获取坐标相同");// 若两次请求获取到的地理位置坐标是相同的，则不再定位
                    mLocationClient.stop();
                    return;
                }
            }
            lastPoint = new BmobGeoPoint(longitude, latitude);
        }
    }

    public Map<String, BmobChatUser> getContactList() {
        return contactList;
    }

    public void setContactList(Map<String, BmobChatUser> contactList) {
        if (this.contactList != null) {
            this.contactList.clear();
        }
        this.contactList = contactList;
    }

    public void logout() {
        BmobUserManager.getInstance(getApplicationContext()).logout();
        setContactList(null);
        setLatitude(null);
        setLongitude(null);
    }

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

    /*初始化imageLoader*/
    public void initImageLoader(){
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new LruMemoryCache(5*1024*1024))
                .discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static DisplayImageOptions getOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_temporarily)
                .showImageForEmptyUri(R.drawable.image_temporarily)
                .showImageOnFail(R.drawable.image_temporarily)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
        return options;
    }

    public void initTitleList(){
        Constants.listTitle.add("弟子规");
        Constants.listTitle.add("三字经");
        Constants.listTitle.add("千字文");
        Constants.listTitle.add("笠翁对韵");
        Constants.listTitle.add("论语(上)");
        Constants.listTitle.add("论语(下)");
        Constants.listTitle.add("大学中庸");
        Constants.listTitle.add("孟子");
        Constants.listTitle.add("老子");
        Constants.listTitle.add("庄子");
        Constants.listTitle.add("史记");
        Constants.listTitle.add("资治通鉴");
        Constants.listTitle.add("历代美文");
    }

    private class GeoCoderResultListener implements OnGetGeoCoderResultListener{

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        }
    }

}
