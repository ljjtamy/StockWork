package com.example.stockwork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class StockRecordDBHelper extends SQLiteOpenHelper {
    // 数据库基本信息
    private static final String DATABASE_NAME = "stock_records.db";
    private static final int DATABASE_VERSION = 1;

    // 定义表结构
    public static class StockRecordEntry implements BaseColumns {
        public static final String TABLE_NAME = "stock_records";
        public static final String COLUMN_STOCK_INFO = "stock_info";
        public static final String COLUMN_TOTAL_INCOME = "total_income";
    }

    // SQL 创建表语句（不含时间戳）
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StockRecordEntry.TABLE_NAME + " (" +
                    StockRecordEntry._ID + " INTEGER PRIMARY KEY," +
                    StockRecordEntry.COLUMN_STOCK_INFO + " TEXT," +
                    StockRecordEntry.COLUMN_TOTAL_INCOME + " REAL" +
                    ")";

    // SQL 删除表语句
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StockRecordEntry.TABLE_NAME;

    public StockRecordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级策略：删除旧表并重建
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}