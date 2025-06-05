package com.example.stockwork;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Color;

public class StockSimulatorActivity extends AppCompatActivity {

    private List<String> stockList;
    private List<String> favoriteList;
    private ArrayAdapter<String> stockAdapter;
    private ArrayAdapter<String> favoriteAdapter;
    private ListView lvStocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_simulator);

        // 初始化股票数据和收藏夹
        initData();

        // 设置股票列表
        lvStocks = findViewById(R.id.lv_stocks);
        setupStockAdapter();

        // 设置收藏夹列表
        ListView lvFavorites = findViewById(R.id.lv_favorites);
        favoriteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteList);
        lvFavorites.setAdapter(favoriteAdapter);

        // 股票列表项点击事件 - 添加到收藏夹
        lvStocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStock = stockList.get(position);
                if (!favoriteList.contains(selectedStock)) {
                    favoriteList.add(selectedStock);
                    favoriteAdapter.notifyDataSetChanged();
                    Toast.makeText(StockSimulatorActivity.this, "已添加到收藏夹", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StockSimulatorActivity.this, "该股票已在收藏夹中", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 收藏夹列表项点击事件 - 从收藏夹移除
        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStock = favoriteList.get(position);
                favoriteList.remove(selectedStock);
                favoriteAdapter.notifyDataSetChanged();
                Toast.makeText(StockSimulatorActivity.this, "已从收藏夹移除", Toast.LENGTH_SHORT).show();
            }
        });

        // 刷新模拟数据按钮
        Button btnRefresh = findViewById(R.id.btn_refresh); // 你的按钮ID
        if (btnRefresh == null) {
            Log.e("StockDebug", "刷新按钮未找到");
        } else {
            btnRefresh.setOnClickListener(v -> {
                Log.d("StockDebug", "刷新按钮被点击");
                refreshStockData();
            });
        }
        btnRefresh.setOnClickListener(v -> {
            Log.d("StockDebug", "刷新按钮被点击"); // 加日志
            refreshStockData(); // 调用刷新方法
        });
    }

    private void setupStockAdapter() {
        stockAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                stockList
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 先调用父类方法获取 view，后续才能对 view 里的控件操作
                View view = super.getView(position, convertView, parent);

                // 拿到布局里的 text1、text2
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                // 拆分数据，设置文本
                String[] parts = getItem(position).split(" - ");
                text1.setText(parts[0]);
                text2.setText(parts[1]);

                // 处理涨跌幅颜色
                String[] changeParts = parts[1].split(" ");
                if (changeParts.length > 1) {
                    String change = changeParts[1];
                    if (change.contains("+")) {
                        text2.setTextColor(Color.GREEN);
                    } else {
                        text2.setTextColor(Color.RED);
                    }
                }

                // 最后返回 view
                return view;
            }

        };
        lvStocks.setAdapter(stockAdapter);
    }

    private void initData() {
        stockList = new ArrayList<>();
        stockList.add("上证指数 (000001) - ¥3,591.84 +1.02%");
        stockList.add("深证成指 (399001) - ¥14,870.91 +1.25%");
        stockList.add("创业板指 (399006) - ¥3,242.61 +2.12%");
        stockList.add("腾讯控股 (00700.HK) - HK$428.60 +2.34%");
        stockList.add("阿里巴巴 (BABA) - US$192.58 +1.87%");
        stockList.add("贵州茅台 (600519) - ¥1,987.00 +0.95%");
        stockList.add("工商银行 (601398) - ¥5.02 +0.40%");
        stockList.add("中国石油 (601857) - ¥4.68 +1.30%");

        favoriteList = new ArrayList<>();
    }

    private void refreshStockData() {
        // 1. 刷新前，打印当前股票列表
        Log.d("StockDebug", "刷新前数据: " + stockList);

        // 2. 生成新数据（原来的随机逻辑）
        List<String> newStockList = new ArrayList<>();
        for (String stock : stockList) {
            String[] parts = stock.split(" - ");
            String nameCode = parts[0];
            String priceChange = parts[1];

            String[] priceChangeParts = priceChange.split(" ");
            String priceStr = priceChangeParts[0];
            String changeStr = priceChangeParts[1];

            // 生成随机涨跌幅（-5% ~ +5% 示例，可调整范围）
            double randomChange = new Random().nextDouble() * 10 - 5;
            double currentPrice = Double.parseDouble(priceStr.replaceAll("[^0-9.]", ""));
            double newPrice = currentPrice * (1 + randomChange / 100);

            // 格式化新价格和涨跌幅
            String currency = priceStr.contains("¥") ? "¥" :
                    priceStr.contains("HK$") ? "HK$" : "US$";
            String newPriceStr = currency + String.format("%.2f", newPrice);
            String newChangeStr = (randomChange >= 0 ? "+" : "") +
                    String.format("%.2f%%", randomChange);

            newStockList.add(nameCode + " - " + newPriceStr + " " + newChangeStr);
        }

        // 3. 替换原数据
        stockList.clear();
        stockList.addAll(newStockList);

        // 4. 刷新后，打印新股票列表
        Log.d("StockDebug", "刷新后数据: " + stockList);

        // 5. 通知适配器更新（关键！）
        runOnUiThread(() -> {
            stockAdapter.notifyDataSetChanged();
            Log.d("StockDebug", "适配器已通知更新");
        });
    }
}