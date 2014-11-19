package com.elight.teaching.utils.multiphotopicker;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.elight.teaching.entity.multiphotopicker.ImageBucket;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import com.elight.teaching.entity.multiphotopicker.ImageItem;

import java.util.*;

/**
 * Created by dawn on 2014/10/28.
 */
public class ImageFetcher {

    private static ImageFetcher instance;
    private Context context;
    private HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();
    private HashMap<String, String> thumbnailList = new HashMap<String, String>();

    private ImageFetcher(Context context){
        this.context = context;
    }

    public static ImageFetcher getInstance(Context context){
        if(instance == null){
            synchronized (ImageFetcher.class){
                instance = new ImageFetcher(context);
            }
        }
        return instance;
    }

    /*是否加载过了相册的集合*/
    boolean hasBuildImageBucketList = false;

    /**
     * 得到图片集
     * @param refresh
     * @return
     */
    public List<ImageBucket> getImagesBucketList(boolean refresh){
        if(refresh || (!refresh && !hasBuildImageBucketList)){
            buildImageBucketList();
        }
        List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
        Iterator<Map.Entry<String, ImageBucket>> iterator = bucketList.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, ImageBucket> entry = iterator.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    private void buildImageBucketList(){
        Cursor cursor = null;
        try {
            long startTime = System.currentTimeMillis();
            //构造缩略图索引
            getThumbnail();
            //构造相册索引
            String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.DATA, Media.BUCKET_DISPLAY_NAME};
            //得到一个游标
            cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI,columns,null,null,null);
            if(cursor.moveToFirst()){
                //获取到指定列的索引
                int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
                int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
                int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);
                do{
                    String _id = cursor.getString(photoIDIndex);
                    String path = cursor.getString(photoPathIndex);
                    String bucketName = cursor.getString(bucketDisplayNameIndex);
                    String bucketId = cursor.getString(bucketIdIndex);

                    ImageBucket bucket = bucketList.get(bucketId);

                    if(bucket == null){
                        bucket = new ImageBucket();
                        bucketList.put(bucketId, bucket);
                        bucket.imageItemList = new ArrayList<ImageItem>();
                        bucket.bucketName = bucketName;
                    }

                    bucket.count++;
                    ImageItem imageItem = new ImageItem();
                    imageItem.imageId = _id;
                    imageItem.sourcePath = path;
                    imageItem.thumbnailPath = thumbnailList.get(_id);
                    bucket.imageItemList.add(imageItem);
                }while (cursor.moveToNext());
            }
            hasBuildImageBucketList = true;
            long endTime = System.currentTimeMillis();

        }finally {
            cursor.close();
        }
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail(){
        Cursor cursor = null;
        try{
            String[] projection = {Thumbnails.IMAGE_ID, Thumbnails.DATA};
            cursor = context.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
            getThumbnailColumnData(cursor);
        }finally {
            cursor.close();
        }
    }

    /**
     * 从数据库里得到缩略图
     * @param cursor
     */
    private void getThumbnailColumnData(Cursor cursor){
        if(cursor.moveToFirst()){
            int image_id;
            String image_path;
            int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);

            do{
                image_id = cursor.getInt(image_idColumn);
                image_path = cursor.getString(dataColumn);
                thumbnailList.put(""+image_id, image_path);
            }while(cursor.moveToNext());
        }
    }
}
