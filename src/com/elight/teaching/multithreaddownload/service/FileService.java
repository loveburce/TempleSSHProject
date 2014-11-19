package com.elight.teaching.multithreaddownload.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.elight.teaching.multithreaddownload.db.DBOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dawn on 2014/11/13.
 */
public class FileService {
    private DBOpenHelper openHelper;

    public FileService(Context context) {
        openHelper = new DBOpenHelper(context);
    }

    /**
     * 获取每条线程已经下载的文件长度
     * @param path
     * @return
     */
    public Map<Integer, Integer> getData(String path){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select threadId, downLength from FileDownLog where downPath=?",new String[]{path});

        Map<Integer, Integer> data = new HashMap<Integer, Integer>();
        while(cursor.moveToNext()){
            data.put(cursor.getInt(0), cursor.getInt(1));
        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * 保存每条线程已经下载的文件长度
     * @param path
     * @param map
     */
    public void saveData(String path, Map<Integer, Integer> map){   //int thread, int position
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            for(Map.Entry<Integer, Integer> entry : map.entrySet()){
                db.execSQL("insert into FileDownLog(downPath, threadId, downLength) values(?,?,?)",
                    new Object[]{path, entry.getKey(), entry.getValue()});
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        db.close();
    }

    /**
     * 实时更新每条线程已经下载的文件长度
     * @param path
     * @param threadId
     * @param pos
     */
    public void updateData(String path, int threadId, int pos){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("update FileDownLog set downLength=? where downPath=? and threadId=?",
                new Object[]{pos, path, threadId});
        db.close();
    }

    /**
     * 当文件下载完成后，删除对应的下载记录
     * @param path
     */
    public void deleteData(String path){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("delete from FileDownLog where downPath=?", new Object[]{path});
        db.close();
    }
}
