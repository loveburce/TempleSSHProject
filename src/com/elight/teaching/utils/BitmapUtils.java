package com.elight.teaching.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by dawn on 2014/10/16.
 */
public class BitmapUtils {

    //compress bitmap
    public static Bitmap compressImageFromFile(String srcPath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;  //只读边，不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if(w > h && w > ww){
            be = (int)(newOpts.outWidth /ww);
        } else if(w < h && h > hh){
            be = (int)(newOpts.outHeight / hh);
        }
        if(be <= 0) be = 1;

        newOpts.inSampleSize = be;  //设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;    //该模式是默认的，不可设置
        newOpts.inPurgeable = true;     //同时设置才会有效
        newOpts.inInputShareable = true;    //当系统内存不足时图片自动回收

        bitmap = BitmapFactory.decodeFile(srcPath,newOpts);
        return bitmap;
    }

}
