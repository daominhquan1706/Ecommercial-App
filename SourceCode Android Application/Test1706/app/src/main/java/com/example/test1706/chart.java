package com.example.test1706;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class chart extends AppCompatActivity {

    float testchart[]={98.8f,123.8f,159.7f};
    String Thongso[]={"Dong ho 1 ","Dong ho 2 ","Dong ho 3 "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        setupPieChart();
    }

    private void setupPieChart() {
        List<PieEntry> PieEntries=new ArrayList<>();
        for (int i=0;i<testchart.length;i++)
        {
            PieEntries.add(new PieEntry(testchart[i],Thongso[i]));
        }

        PieDataSet dataSet=new PieDataSet(PieEntries,"Thong so vi du");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data=new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();

    }
}
