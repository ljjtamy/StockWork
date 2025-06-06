package com.example.stockwork;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

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

                // 跳转到计算页面
                Intent intent = new Intent(StockSimulatorActivity.this, StockCalculationActivity.class);
                intent.putExtra("stockInfo", selectedStock);
                startActivity(intent);
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
                text2.setText("买入价: " + parts[1] + " 卖出价: " + parts[2]);

                // 处理涨跌幅颜色（这里暂时用买入卖出差价模拟，可按需调整）
                double buyPrice = Double.parseDouble(parts[1]);
                double sellPrice = Double.parseDouble(parts[2]);
                if (sellPrice > buyPrice) {
                    text2.setTextColor(Color.GREEN);
                } else {
                    text2.setTextColor(Color.RED);
                }

                // 最后返回 view
                return view;
            }

        };
        lvStocks.setAdapter(stockAdapter);
    }

    private void initData() {
        stockList = new ArrayList<>();
        stockList.add("上证指数 (000001) - 3591.84 - 3595.84");
        stockList.add("深证成指 (399001) - 14870.91 - 14880.91");
        stockList.add("创业板指 (399006) - 3242.61 - 3245.61");
        stockList.add("腾讯控股 (00700.HK) - 428.60 - 430.60");
        stockList.add("阿里巴巴 (BABA) - 192.58 - 193.58");
        stockList.add("贵州茅台 (600519) - 1987.00 - 1990.00");
        stockList.add("工商银行 (601398) - 5.02 - 5.05");
        stockList.add("中国石油 (601857) - 4.68 - 4.70");

        favoriteList = new ArrayList<>();
    }
}