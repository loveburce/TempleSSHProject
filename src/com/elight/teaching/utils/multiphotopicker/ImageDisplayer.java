package com.elight.teaching.utils.multiphotopicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import android.os.Handler;
import android.widget.ImageView;
import com.elight.teaching.R;
import com.elight.teaching.utils.BitmapUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by dawn on 2014/10/28.
 */
public class ImageDisplayer {

    private static final int THUMB_WIDTH = 256;
    private static final int THUMB_HEIGHT = 256;
    private static ImageDisplayer instance;
    private Context context;
    private int screenWidth;
    private int screenHeight;

    public static ImageDisplayer getInstance(Context context){
        if(instance == null){
            synchronized (ImageDisplayer.class){
                instance = new ImageDisplayer(context);
            }
        }
        return instance;
    }

    public ImageDisplayer(Context context){
        if(context.getApplicationContext() != null){
            this.context = context.getApplicationContext();
        }else{
            this.context = context;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = this.context.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    public Handler handler = new Handler();
    public final String TAG = getClass().getSimpleName();
    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    public void put(String key, Bitmap bmp){
        if(!TextUtils.isEmpty(key) && bmp != null){
            imageCache.put(key, new SoftReference<Bitmap>(bmp));
        }
    }

    public void displayBmp(final ImageView iv, final String thumbPath, final String sourcePath){
        displayBmp(iv, thumbPath, sourcePath, true);
    }

    public void displayBmp(final ImageView iv, final String thumbPath, final String sourcePath, final boolean showThumb){
        if(TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)){
            return;
        }
        if(iv.getTag() != null && iv.getTag().equals(sourcePath)){
            return;
        }
        showDefault(iv);

        final String path;
        if(!TextUtils.isEmpty(thumbPath) && showThumb){
            path = thumbPath;
        }else if(!TextUtils.isEmpty(sourcePath)){
            path = sourcePath;
        }else{
            return;
        }
        iv.setTag(path);

        if(imageCache.containsKey(showThumb ? path + THUMB_WIDTH + THUMB_HEIGHT : path)){
            SoftReference<Bitmap> reference = imageCache.get(showThumb ? path + THUMB_WIDTH + THUMB_HEIGHT : path);
            //可以用LruCache会好些
            Bitmap imgInCache = reference.get();
            if(imgInCache != null){
                refreshView(iv, imgInCache, path);
                return;
            }
        }
        iv.setImageBitmap(null);

        //不在缓存则加载图片
        new Thread(){
            Bitmap img;

            public void run(){
                try{
                    if(path != null && path.equals(thumbPath)){
                        img = BitmapFactory.decodeFile(path);
                    }
                    if(img == null){
                        img = compressImg(sourcePath, showThumb);
                    }
                }catch (Exception e){

                }

                if(img != null){
                    put(showThumb ? path+THUMB_WIDTH+THUMB_HEIGHT : path, img);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshView(iv, img, path);
                    }
                });
            }
        }.start();
    }

    private void refreshView(ImageView imageView, Bitmap bitmap, String path){
        if(imageView != null && bitmap != null){
            if(path != null){
                ((ImageView)imageView).setImageBitmap(bitmap);
                imageView.setTag(path);
            }
        }
    }

    public Bitmap compressImg(String path, boolean showThumb) throws IOException{
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();
        int i = 0;
        Bitmap bitmap = null;
        if(showThumb){
            while(true){
                if((options.outWidth>>i <= THUMB_WIDTH) && (options.outHeight>>i <= THUMB_HEIGHT)){
                    inputStream = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int)Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    break;
                }
                i += 1;
            }
        }else{
            while(true){
                if((options.outWidth>>i <= screenWidth) && (options.outHeight>>i <= screenHeight)){
                    inputStream = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int)Math.pow(2.0D,i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    break;
                }
                i += 1;
            }
        }
        return bitmap;
    }

    private void showDefault(ImageView iv){
        iv.setBackgroundResource(R.drawable.image_temporarily);
    }

    public interface ImageCallback{
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
    }
}
