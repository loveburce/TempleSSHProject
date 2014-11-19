package com.elight.teaching.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.activity.SplashActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dawn on 2014/10/14.
 */
public final class ActivityUtils {

    /*获取屏幕宽高*/
    public static int[] getScreenSize() {
        int[] screens;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = CustomApplication.getInstance().getResources().getDisplayMetrics();
        screens = new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
        return screens;
    }

    public static int getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static float[] getBitmapConfiguration(Bitmap bitmap, ImageView imageView, float screenRadio) {
        int screenWidth = ActivityUtils.getScreenSize()[0];
        float rawWidth = 0;
        float rawHeight = 0;
        float width = 0;
        float height = 0;
        if (bitmap == null) {
            width = (float) (screenWidth / screenRadio);
            height = (float) width;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            rawWidth = bitmap.getWidth();
            rawHeight = bitmap.getHeight();
            if (rawHeight > 10 * rawWidth) {
                imageView.setScaleType(ImageView.ScaleType.CENTER);
            } else {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            float radio = rawHeight / rawWidth;

            width = (float) (screenWidth / screenRadio);
            height = (float) (radio * width);
        }
        return new float[]{width, height};
    }

    /*获取应用的版本号*/
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*通过外部浏览器打开页面*/
    public static void openBrowser(Context context, String urlText) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(urlText);
        intent.setData(url);
        context.startActivity(intent);
    }

    /*切换全屏状态*/
    public static void toggleFullScreen(Activity activity, boolean isFull) {
        hideTitleBar(activity);
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (isFull) {
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /*设置为全屏*/
    public static void setFullScreen(Activity activity) {
        toggleFullScreen(activity, true);
    }


    /*隐藏Activity的系统默认标题栏*/
    public static void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /*强制设置activity的显示方向为垂直方向*/
    public static void setScreenVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /*强制设置Activity的显示方向为横向*/
    public static void setScreenHorizontal(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /*隐藏软件输入法*/
    public static void hideSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*关闭已经显示的输入法窗口*/
    public static void closeSoftInput(Context context, View focusingView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /*使UI适配输入法*/
    public static void adjustSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public static void showShortToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast, null);
        TextView text = (TextView) view.findViewById(R.id.toast_message);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 300);
        toast.setView(view);
        toast.show();
    }

    /*activity的切换动画*/
    @SuppressLint("NewApi")
    public static void runActivityAnim(Activity activity, boolean isEnd){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT){
            if(isEnd){
                activity.overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
            } else {
                activity.overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
            }
        }
    }

    /*快捷方式是否存在*/
    public static boolean ifAddShortCut(Context context){
        boolean isInstallShortCut = false;
        ContentResolver contentResolver = context.getContentResolver();
        String authority = "com.android.launcher2.settings";
        Uri uri = Uri.parse("content://" + authority + "/favorites?notify=true");
        Cursor cursor = contentResolver.query(uri, new String[]{"title","iconResource"},"title=?",new String[]{context.getString(R.string.app_name)},null);

        if(null != cursor && cursor.getCount() > 0){
            isInstallShortCut = true;
        }
        return isInstallShortCut;
    }

    /*创建快捷方式*/
    public static void addShortCut(Context context){
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //设置属性
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        Intent.ShortcutIconResource resource = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, resource);
        //是否允许重复创建
        shortcut.putExtra("duplicate",false);
        Intent intent = new Intent(Intent.ACTION_MAIN);       //标识activity为一个程序的开始
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, SplashActivity.class);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcut);
    }

    /*得到view的高度*/
    public static int getViewHeight(int w, int bmw, int bmh){
        return w * bmh / bmw;
    }

    /*安装一个APP文件*/
    public static void installApk(Context context, File file) {
        Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


    /**
     * 判断手机号码*/
    public static boolean isMobileNO(String mobiles){

        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobiles);

        return matcher.matches();

    }


}