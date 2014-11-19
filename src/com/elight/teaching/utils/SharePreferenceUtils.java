package com.elight.teaching.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dawn on 2014/9/22.
 */
public class SharePreferenceUtils {
    private Context context;
    private SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    private String SHARED_KEY_NOTIFY = "shared_key_notify";
    private String SHARED_KEY_VOICE = "shared_key_sound";
    private String SHARED_KEY_VIBRATE = "shared_key_vibrate";

    public SharePreferenceUtils(Context context, String name) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public SharedPreferences getInstance(){
        return mSharedPreferences;
    }

    public boolean isAllowPushNotify(){
        return mSharedPreferences.getBoolean(SHARED_KEY_NOTIFY,true);
    }

    public void setPushNotifyEnable(boolean isChecked){
        editor.putBoolean(SHARED_KEY_NOTIFY, isChecked);
        editor.commit();
    }

    public boolean isAllowVoice(){
        return mSharedPreferences.getBoolean(SHARED_KEY_VOICE,true);
    }

    public void setAllowVoiceEnable(boolean isChecked){
        editor.putBoolean(SHARED_KEY_VOICE, isChecked);
        editor.commit();
    }

    //允许震动
    public boolean isAllowVibrate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
    }

    public void setAllowVibrateEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_VIBRATE, isChecked);
        editor.commit();
    }

    //set

    //Boolean
    public void setValue(String key, boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public void setValue(int resKey, boolean value){
        setValue(this.context.getString(resKey), value);
    }

    //Float
    public void setValue(String key, float value){
        editor.putFloat(key, value);
        editor.commit();
    }

    public void setValue(int resKey, float value){
        setValue(this.context.getString(resKey), value);
    }

    //Integer
    public void setValue(String key, int value){
        editor.putInt(key, value);
        editor.commit();
    }

    public void setValue(int resKey, int value){
        setValue(this.context.getString(resKey), value);
    }

    //long
    public void setValue(String key, long value){
        editor.putLong(key, value);
        editor.commit();
    }

    public void setValue(int resKey, long value){
        setValue(this.context.getString(resKey), value);
    }

    //string
    public void setValue(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void setValue(int resKey, String value){
        setValue(this.context.getString(resKey), value);
    }

    //get

    //boolean
    public boolean getValue(String key, boolean defaultValue){
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean getValue(int resKey, boolean defaultValue){
        return getValue(this.context.getString(resKey), defaultValue);
    }

    //float
    public float getValue(String key, float defaultValue){
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public float getValue(int resKey, float defaultValue){
        return getValue(this.context.getString(resKey), defaultValue);
    }

    //Integer
    public int getValue(String key, int defaultValue){
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public int getValue(int resKey, int defaultValue){
        return getValue(this.context.getString(resKey), defaultValue);
    }

    //long
    public long getValue(String key, long defaultValue){
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public long getValue(int resKey, long defaultValue){
        return getValue(this.context.getString(resKey), defaultValue);
    }

    //string
    public String getValue(String key, String defaultValue){
        return  mSharedPreferences.getString(key, defaultValue);
    }

    public String getValue(int resKey, String defaultValue){
        return getValue(this.context.getString(resKey), defaultValue);
    }

    //delete
    public void remove(String key){
        editor.remove(key);
        editor.commit();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

    /*是否是第一次启动运用*/
    public boolean isFirstStart(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int curVersion = packageInfo.versionCode;
            int lastVersion = mSharedPreferences.getInt("version", 0);
            if(curVersion > lastVersion){
                //如果当前版本大于上次版本，该版本属于第一次启动
                //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /*是否第一次安装应用*/
    public boolean isFirstInstall(Context context){
        int install = mSharedPreferences.getInt("first_install", 0);
        if(install == 0){
            return true;
        }
        return false;
    }

    /*应用已启动*/
    public void setStarted(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int curVersion = info.versionCode;
            mSharedPreferences.edit().putInt("version", curVersion).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*应用已安装并启动*/
    public void setInstall(Context context){
        mSharedPreferences.edit().putInt("first_install",1).commit();
    }

    /*是否需要改变数据*/
    public boolean needChangeIndexContent(Context context, String openID){
        String save = mSharedPreferences.getString(openID, "");
        String cur = getDateByNumber();
        if(save.equals(cur)){
            //be the last statement in the method
            return false;
        }
        return true;
    }

    /*保存更新日期*/
    public void saveChangeIndexContent(Context context, String openID){
        String cur = getDateByNumber();
        mSharedPreferences.edit().putString(openID, cur).commit();
    }

    /*记录日期，决定是否数据是否需要改动*/
    public static String getDateByNumber(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",new Locale("zh"));
        String cur = simpleDateFormat.format(new Date());
        return cur;
    }

}
