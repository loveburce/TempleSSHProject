package com.elight.teaching.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dawn on 2014/10/16.
 */
public class DBHelper extends SQLiteOpenHelper{

    private final static String db_name = "elight";
    private final static int db_version = 1;

    public DBHelper(Context context){
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createMyFav(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists MyFav");
        onCreate(db);
    }

    private void createMyFav(SQLiteDatabase database){
        String stmt = "CREATE TABLE IF NOT EXISTS MyFav(" +
                "id integer primary key autoincrement," +
                "user_id text," +
                "object_id text," +
                "is_fav integer," +
                "is_love integer," +
                "updateTime timestamp not null default(datetime('now','localtime')))";
        database.execSQL(stmt);
    }

}
