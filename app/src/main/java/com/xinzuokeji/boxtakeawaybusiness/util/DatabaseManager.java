package com.xinzuokeji.boxtakeawaybusiness.util;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xinzuokeji.boxtakeawaybusiness.GSApplication;

import java.util.concurrent.atomic.AtomicInteger;




/**
 *
 */
public class DatabaseManager {
    //
    private static DatabaseManager instance;

    //
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    //数据库实例链接计数
    private AtomicInteger mOpenCounter = new AtomicInteger();

    //数据库名称
//    private static final String DATABASE_NAME = "goldensteward.db";
    private static final String DATABASE_NAME = "xinzuo.db";
    //数据库版本，如果后续修改数据库，需要更新版本号
    private static final int DATABASE_VERSION = 2;

    //初始化数据库管理器
    private static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    // 获取数据库管理器
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            initializeInstance(new SQLiteOpenHelper(GSApplication.getInstance()
                    .getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL("create table cookies(domain varchar(100) not null , cookieinfo varchar(4096) not null );");
                    db.execSQL("create table readids(id varchar(32) not null , type INTEGER not null );");
                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int i, int i1) {
                    db.execSQL("DROP TABLE IF EXISTS cookies");
                    db.execSQL("DROP TABLE IF EXISTS readids");
                    onCreate(db);
                }
            });
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {// Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {// Closing database
            mDatabase.close();
        }
    }
}
