package com.example.gymcompanion.ui.homepage.fragments.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.gymcompanion.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class DetailedDashboardActivity extends AppCompatActivity {
    private BarChart barChart;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_dashboard);
        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);

        generateLineChart();
        generateBarData();
    }

    private void generateLineChart() {
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);


        ArrayList<Entry> linevalues = new ArrayList<>();

        linevalues.add(new Entry(1f, 0.0F));
        linevalues.add(new Entry(2f, 3.0F));
        linevalues.add(new Entry(3f, 2.0F));
        linevalues.add(new Entry(4f, 1.0F));
        linevalues.add(new Entry(5f, 8.0F));
        linevalues.add(new Entry(6f, 10.0F));
        linevalues.add(new Entry(7f, 1.0F));
        linevalues.add(new Entry(8f, 2.0F));
        linevalues.add(new Entry(9f, 5.0F));
        linevalues.add(new Entry(10f, 1.0F));
        linevalues.add(new Entry(11f, 20.0F));
        linevalues.add(new Entry(12f, 40.0F));

        ArrayList<String> labels = new ArrayList<>();

        labels.add("jan");
        labels.add("feb");
        labels.add("mar");
        labels.add("apr");
        labels.add("may");
        labels.add("jun");
        labels.add("jul");
        labels.add("aug");
        labels.add("sep");
        labels.add("nov");
        labels.add("oct");
        labels.add("dec");

        LineDataSet linedataset = new LineDataSet(linevalues, "Monthly Progress");
        //We add features to our chart
        linedataset.setColor(getColor(R.color.lightPrimary));

        linedataset.setCircleRadius(5f);
        linedataset.setDrawFilled(true);
        linedataset.setValueTextSize(10f);
        linedataset.setFillColor(getColor(R.color.lightPrimaryVariant));
        linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //We connect our data to the UI Screen
        LineData data = new LineData(linedataset);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setLabelCount(linevalues.size(), false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setAxisMinimum(data.getXMin() - .5f);
        xAxis.setAxisMaximum(data.getXMax());

        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(data);

    }

    private void generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            float val = (float) (Math.random() * 100);
            entries1.add(new BarEntry(i, val));
        }
        for (int i = 0; i < 7; i++) {
            float val = (float) (Math.random() * 100);
            entries2.add(new BarEntry(i, val));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Time");
        set1.setValueTextColor(getColor(R.color.white));
        set1.setValueTextSize(12f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getColor(R.color.white));

        BarDataSet set2 = new BarDataSet(entries2, "Accuracy");
        set2.setValueTextSize(12f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(getColor(R.color.lightGray));
        set2.setValueTextColor(getColor(R.color.white));


        Legend legend = barChart.getLegend();
        legend.setYOffset(5f);
        legend.setTextColor(getColor(R.color.white));
        legend.setTextSize(12f);

        float groupSpace = 0.20f;
        float barSpace = 0.10f; // x2 dataset

        BarData d = new BarData(set1, set2);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setPinchZoom(true);
        barChart.setFitBars(true);
        barChart.setData(d);
        barChart.invalidate(); // refresh
        barChart.getAxisLeft().setTextColor(getColor(R.color.white));
        barChart.getXAxis().setTextColor(getColor(R.color.white));
        barChart.getAxisRight().setTextColor(getColor(R.color.white));
        barChart.getLegend().setTextColor(getColor(R.color.white));
        barChart.getDescription().setTextColor(getColor(R.color.white));
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * entries1.size());
    }
}