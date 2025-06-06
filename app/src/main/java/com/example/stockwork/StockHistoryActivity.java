package com.example.stockwork;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class StockHistoryActivity extends AppCompatActivity {

    private ListView lvHistory;
    private List<String> historyList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        lvHistory = findViewById(R.id.lv_history);
        historyList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        lvHistory.setAdapter(adapter);

        // 从数据库加载历史记录
        loadHistoryRecords();
    }


    private void loadHistoryRecords() {
        StockRecordDBHelper dbHelper = new StockRecordDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询所有记录
        Cursor cursor = db.query(
                StockRecordDBHelper.StockRecordEntry.TABLE_NAME,  // 表名
                null,                         // 所有列
                null,                         // 无筛选条件
                null,                         // 无筛选参数
                null,                         // 无分组
                null,                         // 无分组条件
                StockRecordDBHelper.StockRecordEntry._ID + " DESC"
        );

        historyList.clear();
        while (cursor.moveToNext()) {
            String stockInfo = cursor.getString(
                    cursor.getColumnIndexOrThrow(StockRecordDBHelper.StockRecordEntry.COLUMN_STOCK_INFO));
            double totalIncome = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(StockRecordDBHelper.StockRecordEntry.COLUMN_TOTAL_INCOME));

            String[] parts = stockInfo.split(" - ");
            String stockName = parts[0];

            String record = stockName + "\n收入: " + String.format("%.2f", totalIncome);
            historyList.add(record);
        }
        cursor.close();
        db.close();

        adapter.notifyDataSetChanged();
    }
}