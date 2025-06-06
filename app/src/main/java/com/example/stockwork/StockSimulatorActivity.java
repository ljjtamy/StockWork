package com.example.stockwork;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;

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

        // 股票列表项点击事件 - 跳转到计算页面
        lvStocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStock = stockList.get(position);
                // 跳转到计算页面
                Intent intent = new Intent(StockSimulatorActivity.this, StockCalculationActivity.class);
                intent.putExtra("stockInfo", selectedStock);
                startActivity(intent);
            }
        });

        // 收藏夹列表项点击事件 - 弹出确认对话框
        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedStock = favoriteList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockSimulatorActivity.this);
                builder.setTitle("确认移除")
                        .setMessage("您确定要移除收藏吗？")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            favoriteList.remove(selectedStock);
                            favoriteAdapter.notifyDataSetChanged();
                            Toast.makeText(StockSimulatorActivity.this, "已从收藏夹移除", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private void setupStockAdapter() {
        stockAdapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_stock,
                R.id.tv_stock_name,
                stockList
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                // 拿到布局里的 tv_stock_name、tv_stock_prices 和 btn_add_to_favorite
                TextView tvStockName = view.findViewById(R.id.tv_stock_name);
                TextView tvStockPrices = view.findViewById(R.id.tv_stock_prices);
                Button btnAddToFavorite = view.findViewById(R.id.btn_add_to_favorite);

                // 拆分数据，设置文本
                String[] parts = getItem(position).split(" - ");
                tvStockName.setText(parts[0]);
                tvStockPrices.setText("买入价: " + parts[1] + " 卖出价: " + parts[2]);

                // 处理涨跌幅颜色
                double buyPrice = Double.parseDouble(parts[1]);
                double sellPrice = Double.parseDouble(parts[2]);
                if (sellPrice > buyPrice) {
                    tvStockPrices.setTextColor(Color.GREEN);
                } else {
                    tvStockPrices.setTextColor(Color.RED);
                }

                final String selectedStock = getItem(position);
                btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!favoriteList.contains(selectedStock)) {
                            favoriteList.add(selectedStock);
                            favoriteAdapter.notifyDataSetChanged();
                            Toast.makeText(StockSimulatorActivity.this, "已添加到收藏夹", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StockSimulatorActivity.this, "该股票已在收藏夹中", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // 最后返回 view
                return view;
            }

        };
        lvStocks.setAdapter(stockAdapter);
    }

    private void initData() {
        stockList = new ArrayList<>();
        List<String> stockNames = new ArrayList<>();
        stockNames.add("上证指数 (000001)");
        stockNames.add("深证成指 (399001)");
        stockNames.add("创业板指 (399006)");
        stockNames.add("腾讯控股 (00700.HK)");
        stockNames.add("阿里巴巴 (BABA)");
        stockNames.add("贵州茅台 (600519)");
        stockNames.add("工商银行 (601398)");
        stockNames.add("中国石油 (601857)");

        Random random = new Random();
        for (String stockName : stockNames) {
            // 生成随机买入价
            double buyPrice = 1 + random.nextDouble() * 2000;
            // 生成随机卖出价
            double sellPrice = buyPrice + (random.nextDouble() - 0.5) * 10;
            String stockInfo = stockName + " - " + String.format("%.2f", buyPrice) + " - " + String.format("%.2f", sellPrice);
            stockList.add(stockInfo);
        }

        favoriteList = new ArrayList<>();
    }
}