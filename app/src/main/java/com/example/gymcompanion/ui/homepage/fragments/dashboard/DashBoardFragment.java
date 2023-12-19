package com.example.gymcompanion.ui.homepage.fragments.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gymcompanion.R;
import com.example.gymcompanion.staticValues.DifferentExercise;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashBoardFragment extends Fragment implements IDashBoardFragment{

    private HorizontalBarChart chart;
    private PieChart pieChart;
    private int color;
    private Context context;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        pieChart = view.findViewById(R.id.pieChart);
        chart = view.findViewById(R.id.chart);

        context = getContext();
        if (context != null) {
            color = context.getColor(R.color.white);
        }

        DashBoardFragmentPresenter presenter = new DashBoardFragmentPresenter(this);
        presenter.getData("Monthly", "2023", "12");

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> typeAmountMap = new HashMap<>();

        // function for setting up the chart

        SetUpPieChart(pieEntries, typeAmountMap);
        return view;
    }

    private void SetUpPieChart(ArrayList<PieEntry> pieEntries, Map<String, Float> typeAmountMap) {
        String label = "type";

        //initializing data
        typeAmountMap.put("Muscle",30.0f);
        typeAmountMap.put("Body Fat", 50.0f);

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#9DB2BF"));
        colors.add(Color.parseColor("#526D82"));
        colors.add(Color.parseColor("#27374D"));
        colors.add(Color.parseColor("#DDE6ED"));

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(color);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(5f);
        l.setTextSize(11f);

        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.setExtraOffsets(0f,0f,0f,5f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawMarkers(false); // To remove markers when click
        pieChart.setDrawEntryLabels(false); // To remove labels from piece of pie
        pieChart.getDescription().setEnabled(false); // To remove description of pie
        pieChart.invalidate();
        pieChart.setHoleColor(context.getColor(R.color.lightPrimaryVariant));
        pieChart.setCenterText("Body Composition");
        pieChart.setCenterTextSize(12f);
        pieChart.setCenterTextColor(Color.BLACK);
    }
    private void SetUpHorizontalBarChart(ArrayList<BarEntry> arrayList, final ArrayList<String> xAxisValues){
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(true);
        chart.setFitBars(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getAxisLeft().setTextColor(color);
        chart.getXAxis().setTextColor(color);
        chart.getAxisRight().setTextColor(color);
        chart.getLegend().setTextColor(color);
        chart.getDescription().setTextColor(color);


        // the data for the chart
        BarDataSet barDataSet = new BarDataSet(arrayList, "Accuracies");
        barDataSet.setColor(context.getColor(R.color.white));
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barData.setValueTextColor(color);

        // turning off the legends in the chart
        Legend l = chart.getLegend();
        l.setEnabled(false);

        // setting the labels ticks and spaces
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(10f);
        xAxis.setTextColor(color);
        xAxis.setLabelCount(arrayList.size());
        xAxis.setAxisMinimum(barData.getXMin()-.5f);
        xAxis.setAxisMaximum(barData.getXMax()+.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        xAxis.setDrawGridLines(false);

        chart.setData(barData);
    }

    @Override
    public void onGetData(boolean verdict, Map<String, Map<Double, Long>> stats) {
        // setting the bar chart
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (String key: stats.keySet()) {
            for (Double accuracy: stats.get(key).keySet()){
                counter += 1;
                barEntries.add(new BarEntry(counter, (float) (Math.round((accuracy / 3) * 100.0) / 100.0)));
                Log.i("tegeelelele", key + ": " + accuracy / 3);
            }
        }

        if (!stats.keySet().isEmpty()) {
            SetUpHorizontalBarChart(barEntries, new ArrayList<>(stats.keySet()));
            chart.setVisibility(View.VISIBLE);
        }
    }
}