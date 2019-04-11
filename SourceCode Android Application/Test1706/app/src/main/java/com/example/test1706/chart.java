package com.example.test1706;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chart extends AppCompatActivity {
    FirebaseDatabase mydb;
    DatabaseReference myRef;
    List<Float> testchart;
    List<String> listCategory;
    private static final String TAG = "chart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        mydb = FirebaseDatabase.getInstance();
        myRef = mydb.getReference();
        testchart = new ArrayList<Float>();
        listCategory = new ArrayList<String>();
        myRef.child("NiteWatch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    testchart.add((float) dataSnapshot1.getChildrenCount());
                    listCategory.add(dataSnapshot1.getKey());
                    setupPieChart();
                    Log.d(TAG, "onDataChange: " + dataSnapshot1.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupPieChart() {
        List<PieEntry> PieEntries = new ArrayList<>();
        for (int i = 0; i < testchart.size(); i++) {
            PieEntries.add(new PieEntry(testchart.get(i), listCategory.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(PieEntries, "Thong so vi du");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
        chart.setCenterTextSize(30);

    }
}
