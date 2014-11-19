package com.elight.teaching.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by dawn on 2014/10/20.
 */
public class CacheUtils {

    /*获取 /data/data/files */
    public static File getFileDirectory(Context context){
        File appCacheDir = null;
        if(appCacheDir == null){
            appCacheDir = context.getFilesDir();
        }
        if(appCacheDir == null){
            String cacheDirPath = "/data/data/"+context.getPackageName()+"/files/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    public static File getCacheDirectory(Context context, boolean preferExternal, String dirName){
        File appCacheDir = null;
        if(preferExternal
                && MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && hasExternalStoragePermission(context)){
            appCacheDir = getExternalCacheDir(context, dirName);
        }
        if(appCacheDir == null){
            appCacheDir = context.getCacheDir();
        }
        if(appCacheDir == null){
            String cacheDirPath = "/data/data/"+context.getPackageName()+"/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context, String dirName){
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(),"Android"),"data");
        File appCacheDir2 = new File(new File(dataDir, context.getPackageName()),"cache");
        File appCacheDir = new File(appCacheDir2, dirName);
        if(!appCacheDir.exists()){
            if(!appCacheDir.mkdirs()){
                return null;
            }
            try{
                new File(appCacheDir,".nomedia").createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return appCacheDir;
    }

    private static final String TAG = "CacheUtils";
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static boolean hasExternalStoragePermission(Context context){
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public static String saveToSdCard(Context context, Bitmap bitmap, String fileName){
        String files = getCacheDirectory(context, true, "pic")+fileName+"_11";
        File file = new File(files);
        try{
            FileOutputStream outputStream = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)){
                outputStream.flush();
                outputStream.close();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    /*检查SD卡是否存在*/
    public static  boolean checkSdCard(){
        if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
