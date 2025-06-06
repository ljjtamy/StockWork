package com.example.stockwork;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FinancialNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_news);

        ListView listViewNews = findViewById(R.id.lv_news);
        List<String> newsList = generateSampleNews();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                newsList
        );

        listViewNews.setAdapter(adapter);
    }

    private List<String> generateSampleNews() {
        List<String> news = new ArrayList<>();
        news.add("央行开展2000亿元逆回购操作 中标利率2.2%");
        news.add("统计局：5月CPI同比上涨2.4% 涨幅继续回落");
        news.add("证监会发布《证券公司租用第三方网络平台开展证券业务活动管理规定》");
        news.add("创业板改革并试点注册制相关制度规则正式发布");
        news.add("国际油价大幅上涨 美油期货收涨超5%");
        news.add("多家银行下调个人账户线上交易限额");
        news.add("6月PMI为50.9% 制造业延续稳定扩张态势");
        news.add("美股三大指数集体收涨 纳指再创历史新高");
        news.add("财政部发行7500亿元特别国债 票面利率2.48%");
        news.add("工信部：前5月规模以上电子信息制造业增加值同比增长9.9%");
        news.add("沪深交易所发布基础设施REITs扩募规则 支持优质资产上市");
        news.add("银保监会：2025年一季度末银行业总资产同比增长10.6%");
        news.add("发改委：进一步完善政府猪肉储备调节机制 促进生猪市场平稳运行");
        return news;
    }
}