package com.example.stockwork;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StockCalculationActivity extends AppCompatActivity {

    private TextView tvStockInfo;
    private EditText etAmount;
    private EditText etCommission;
    private TextView tvTotalIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_calculation);

        tvStockInfo = findViewById(R.id.tv_stock_info);
        etAmount = findViewById(R.id.et_amount);
        etCommission = findViewById(R.id.et_commission);
        tvTotalIncome = findViewById(R.id.tv_total_income);
        Button btnCalculate = findViewById(R.id.btn_calculate);

        // 获取传递过来的股票信息
        String stockInfo = getIntent().getStringExtra("stockInfo");
        tvStockInfo.setText(stockInfo);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTotalIncome(stockInfo);
            }
        });
    }

    private void calculateTotalIncome(String stockInfo) {
        String[] parts = stockInfo.split(" - ");
        double buyPrice = Double.parseDouble(parts[1]);
        double sellPrice = Double.parseDouble(parts[2]);

        String amountStr = etAmount.getText().toString();
        String commissionStr = etCommission.getText().toString();

        if (TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(commissionStr)) {
            Toast.makeText(this, "请输入购买股数和手续费", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr);
        double commission = Double.parseDouble(commissionStr);

        double totalIncome = sellPrice * amount * (1 + commission) - buyPrice * amount;
        tvTotalIncome.setText("总收入: " + String.format("%.2f", totalIncome));

        //当收入大于5000时
        if (totalIncome > 1000) {
            showTip();
        }
    }

    private void showTip() {
        Toast.makeText(this, "恭喜您取得了收获！建议将这支股票加入收藏夹。", Toast.LENGTH_LONG).show();
    }
}