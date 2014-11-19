package com.elight.teaching.multithreaddownload.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by dawn on 2014/11/13.
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "multi_thread_download";
    private static final int VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createFileDownLog(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteFileDownLog(db);
    }

    private void createFileDownLog(SQLiteDatabase db){
        String stmt = "CREATE TABLE IF NOT EXISTS FileDownLog(" +
                "id integer primary key autoincrement," +
                "downPath varchar(100)," +
                "threadId integer," +
                "downLength integer," +
                "updateTime timestamp default(datetime('now','localtime')))";
        db.execSQL(stmt);
    }

    private void deleteFileDownLog(SQLiteDatabase db){
        String str = "drop table if exists FileDownLog";
        db.execSQL(str);
    }
}
