package com.example.gymcompanion.ui.Homepage.fragments.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.CustomViews.CustomFilterDialog;
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
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class DetailedDashboardActivity extends AppCompatActivity implements IDetailedDashBoard{
    private BarChart barChart;
    private LineChart lineChart;
    private final int FILTER_ID = R.id.filter;
    private DetailedDashBoardPresenter presenter;
    private int counter = 0;
    private TextView detailedMonthPicker;
    private final ArrayList<String> months = new ArrayList<>(
            Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Nov", "Oct", "Dec")
    );
    private int detailedPickedYear = 2024;
    private int detailedPickedMonth = 1;
    private int lineChartPickedYear = 2023;
    private String exercise = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_dashboard);
        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        detailedMonthPicker = findViewById(R.id.detailedMonth);
        detailedPickedYear = Calendar.getInstance().get(Calendar.YEAR);
        detailedPickedMonth = Calendar.getInstance().get(Calendar.MONTH);
        lineChartPickedYear = detailedPickedYear;

        Intent intent = getIntent();
        if (intent.hasExtra("exercise")){
            exercise = intent.getStringExtra("exercise");
        }

        TextView detailedYearPicker = findViewById(R.id.detailedYear);
        TextView lineChartYearPicker = findViewById(R.id.year);
        MaterialToolbar toolbar = findViewById(R.id.toolBar);

        toolbar.setTitle(exercise);
        lineChartYearPicker.setOnClickListener(v -> {
            MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
            newFragment.setListener((view1, year, month, dayOfMonth) -> {
                lineChartYearPicker.setText(String.valueOf(year));
                lineChartPickedYear = year;

                // TODO: SETUP THE LINE CHART
                presenter.getDataLineChart(exercise, "Monthly", lineChartPickedYear, 0);
            }, "Year");
            newFragment.show(getSupportFragmentManager(), "DatePicker");

        });
        detailedYearPicker.setText(String.valueOf(detailedPickedYear));
        detailedMonthPicker.setOnClickListener(view -> {
            MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
            newFragment.setListener((view1, year, month, dayOfMonth) -> {
                detailedMonthPicker.setText(months.get(month - 1));
                presenter.getData(exercise, "Weekly", detailedPickedYear, month);
                detailedPickedMonth = month;
            }, "Month");
            newFragment.show(getSupportFragmentManager(), "DatePicker");
        });

        detailedYearPicker.setOnClickListener(view -> {
            MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
            newFragment.setListener((view1, year, month, dayOfMonth) -> {
                detailedYearPicker.setText(String.valueOf(year));
                detailedPickedYear = year;
                presenter.getData(exercise, "Monthly", detailedPickedYear, month);
            }, "Year");
            newFragment.show(getSupportFragmentManager(), "DatePicker");
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == FILTER_ID) {
                CustomFilterDialog cdd = new CustomFilterDialog(DetailedDashboardActivity.this, DetailedDashboardActivity.this);
                Objects.requireNonNull(cdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
            return true;
        });

        presenter = new DetailedDashBoardPresenter(this);

        presenter.getData(exercise, "Monthly", detailedPickedYear, detailedPickedMonth);
        presenter.getDataLineChart(exercise, "Monthly", lineChartPickedYear, 0);

//        ArrayList<BarEntry> entries1 = new ArrayList<>();
//        ArrayList<BarEntry> entries2 = new ArrayList<>();
//
//        for (int i = 0; i < 7; i++) {
//            float val = (float) (Math.random() * 100);
//            entries1.add(new BarEntry(i, val));
//        }
//        for (int i = 0; i < 7; i++) {
//            float val = (float) (Math.random() * 100);
//            entries2.add(new BarEntry(i, val));
//        }
//        generateBarData(entries1, entries2);
    }

    private void generateLineChart(ArrayList<Entry> accuracies, ArrayList<Entry> times) {
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);


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

        LineDataSet accuracyDataSet = new LineDataSet(accuracies, "Accuracy (percentage)");
        //We add features to our chart
        accuracyDataSet.setColor(getColor(R.color.lightPrimary));

        accuracyDataSet.setCircleRadius(5f);
        accuracyDataSet.setDrawFilled(true);
        accuracyDataSet.setValueTextSize(10f);
        accuracyDataSet.setFillColor(getColor(R.color.lightPrimaryVariant));
        accuracyDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet timeDataSet = new LineDataSet(times, "Time (seconds)");
        //We add features to our chart
        timeDataSet.setColor(getColor(R.color.red));

        timeDataSet.setCircleRadius(5f);
        timeDataSet.setDrawFilled(true);
        timeDataSet.setValueTextSize(10f);
        timeDataSet.setFillColor(getColor(R.color.lightGray));
        timeDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //We connect our data to the UI Screen
        LineData data = new LineData(accuracyDataSet, timeDataSet);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setLabelCount(accuracies.size(), false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setAxisMinimum(data.getXMin() - .5f);
        xAxis.setAxisMaximum(data.getXMax());

        YAxis y = lineChart.getAxisLeft();
        y.setAxisMaximum(Math.max(accuracyDataSet.getYMax(), timeDataSet.getYMax()) + 20);
        y.setAxisMinimum(0);

        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(data);
        lineChart.setExtraOffsets(0f, 0f, 0f, 10f);
        
        lineChart.animateXY(1000, 1000);
    }

    private void generateBarData(ArrayList<BarEntry> times, ArrayList<BarEntry> accuracy, ArrayList<String> labels) {
        barChart.clear();
        barChart.invalidate();

        BarDataSet set1 = new BarDataSet(times, "Time (seconds)");
        set1.setValueTextColor(getColor(R.color.white));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getColor(R.color.white));

        BarDataSet set2 = new BarDataSet(accuracy, "Accuracy (percentage)");
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(getColor(R.color.lightGray));
        set2.setValueTextColor(getColor(R.color.white));

        YAxis y = barChart.getAxisLeft();
        y.setAxisMaximum(Math.max(set1.getYMax(), set2.getYMax()) + 10);
        y.setAxisMinimum(0);

        Log.i("testing", "generateBarData: " + labels.size());

        Legend legend = barChart.getLegend();
        legend.setYOffset(5f);
        legend.setTextColor(getColor(R.color.white));
        legend.setTextSize(12f);

        float groupSpace = 0.15f;
        float barSpace = 0.01f; // x2 dataset
        float barWidth = 0.42f;

        BarData data = new BarData(set1, set2);

        data.setValueTextSize(12f);
        data.setBarWidth(barWidth);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(labels.size());
        xAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMaximum(labels.size());


        barChart.setData(data);
        barChart.groupBars(0f,groupSpace,barSpace);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setTouchEnabled(false);
        barChart.animateY(1000);
        barChart.getAxisLeft().setTextColor(getColor(R.color.white));
        barChart.getXAxis().setTextColor(getColor(R.color.white));
        barChart.getAxisRight().setTextColor(getColor(R.color.white));
        barChart.invalidate();

//        barChart.getDescription().setEnabled(false);
//        barChart.setDrawGridBackground(false);
//        barChart.setPinchZoom(false);
//        barChart.setScaleEnabled(false);
//        barChart.setData(d);
//        barChart.getAxisLeft().setTextColor(getColor(R.color.white));
//        barChart.getXAxis().setTextColor(getColor(R.color.white));
//        barChart.getAxisRight().setTextColor(getColor(R.color.white));
//        barChart.getLegend().setTextColor(getColor(R.color.white));
//        barChart.getDescription().setTextColor(getColor(R.color.white));
////        barChart.getXAxis().setAxisMinimum(0);
////        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * times.size());
//        barChart.getXAxis().setCenterAxisLabels(true);
//        barChart.groupBars(0, groupSpace, barSpace);
//        barChart.getXAxis().setAxisMinimum(0);
//        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
//        barChart.getXAxis().setCenterAxisLabels(true);
//        barChart.setFitBars(true);
//        Log.i("testing", "generateBarData: " + d.getBarWidth());
    }

    @Override
    public void onGetData(boolean verdict, Map<String, Map<Double, Long>> stats) {
        barChart.setVisibility(View.INVISIBLE);
        if (verdict) {
            ArrayList<BarEntry> times = new ArrayList<>();
            ArrayList<BarEntry> accuracies = new ArrayList<>();

            for (String key: stats.keySet()) {
                ArrayList<Double> keySet = new ArrayList<>(Objects.requireNonNull(stats.get(key)).keySet());
                for (Double accuracy: keySet){
                    int time = Math.toIntExact(stats.get(key).get(accuracy));
                    counter += 1;
                    times.add(new BarEntry(counter, time / 1000f));
                    accuracies.add(new BarEntry(counter, (float) (Math.round((accuracy) * 100.0) / 100.0)));
                }
            }

            generateBarData(times, accuracies, new ArrayList<>(stats.keySet()));
            barChart.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onGetDataLineChart(boolean verdict, Map<String, Map<Double, Long>> stats) {
        lineChart.setVisibility(View.INVISIBLE);
        if (verdict) {
            ArrayList<Entry> accuracies = new ArrayList<>();
            ArrayList<Entry> times = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                if (!stats.containsKey(String.valueOf(i))) {
                    accuracies.add(new Entry(i, 0.0f));
                    times.add(new Entry(i, 0.0f));
                }
                else {
                    ArrayList<Double> keySet = new ArrayList<>(Objects.requireNonNull(stats.get(String.valueOf(i))).keySet());
                    for (Double accuracy: keySet){
                        int time = Math.toIntExact(stats.get(String.valueOf(i)).get(accuracy));
                        accuracies.add(new Entry(i, (float) (Math.round((accuracy) * 100.0) / 100.0)));
                        times.add(new Entry(i, time / 1000f));
                    }
                }
            }
            generateLineChart(accuracies, times);
            lineChart.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onChangeFilter(String mode) {
        Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
        if (mode.equals("Monthly")) {
            presenter.getData(exercise, mode, detailedPickedYear, detailedPickedMonth);
            detailedMonthPicker.setVisibility(View.GONE);
        }
        if (mode.equals("Weekly")) {
            presenter.getData(exercise, mode, detailedPickedYear, detailedPickedMonth);
            detailedMonthPicker.setVisibility(View.VISIBLE);
        }
    }
}