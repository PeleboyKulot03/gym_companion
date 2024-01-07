package com.example.gymcompanion.ui.Homepage.fragments.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.CustomViews.CustomFilterDialogV1;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashBoardFragment extends Fragment implements IDashBoardFragment {

    private HorizontalBarChart chart;
    private PieChart pieChart;
    private int color;
    private Context context;
    private TextView monthPicker;
    private int detailedPickedYear;
    private int detailedPickedMonth;
    private final ArrayList<String> months = new ArrayList<>(
            Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Nov", "Oct", "Dec")
    );
    private String mode = "Monthly";
    private DashBoardFragmentPresenter presenter;
    private TextView currWeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        presenter = new DashBoardFragmentPresenter(this);
        currWeight = view.findViewById(R.id.currWeight);
        if (getActivity() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        pieChart = view.findViewById(R.id.pieChart);
        chart = view.findViewById(R.id.chart);
        ImageView filter = view.findViewById(R.id.filter);
        monthPicker = view.findViewById(R.id.monthFilter);
        TextView yearPicker = view.findViewById(R.id.yearFilter);

        detailedPickedYear = Calendar.getInstance().get(Calendar.YEAR);
        detailedPickedMonth = Calendar.getInstance().get(Calendar.MONTH);
        yearPicker.setText(String.valueOf(detailedPickedYear));
        monthPicker.setText(months.get(detailedPickedMonth));

        monthPicker.setOnClickListener(v -> {
            MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
            newFragment.setListener((view1, year, month, dayOfMonth) -> {
                monthPicker.setText(months.get(month - 1));
                detailedPickedMonth = month;

                // TODO: SETUP THE BAR CHART
                presenter.getData(mode, String.valueOf(detailedPickedYear), String.valueOf(detailedPickedMonth));
            }, "Month");
            newFragment.show(fragmentManager, "DatePicker");
        });

        yearPicker.setOnClickListener(v -> {
            MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
            newFragment.setListener((view1, year, month, dayOfMonth) -> {
                yearPicker.setText(String.valueOf(year));
                detailedPickedYear = year;

                // TODO: SETUP BARCHART
                presenter.getData(mode, String.valueOf(detailedPickedYear), String.valueOf(detailedPickedMonth));

            }, "Year");
            newFragment.show(fragmentManager, "DatePicker");
        });

        filter.setOnClickListener(v -> {
            CustomFilterDialogV1 cdd = new CustomFilterDialogV1(getActivity(), DashBoardFragment.this);
            Objects.requireNonNull(cdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();
        });
        context = getContext();
        if (context != null) {
            color = context.getColor(R.color.white);
        }

        presenter.getWeight();
        presenter.getData(mode, String.valueOf(detailedPickedYear), String.valueOf(detailedPickedMonth + 1));

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> typeAmountMap = new HashMap<>();

        // function for setting up the chart

        SetUpPieChart(pieEntries, typeAmountMap);
        return view;
    }

    private void SetUpPieChart(ArrayList<PieEntry> pieEntries, Map<String, Float> typeAmountMap) {
        String label = "type";

        //initializing data
        typeAmountMap.put("Muscle", 30.0f);
        typeAmountMap.put("Body Fat", 50.0f);

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#9DB2BF"));
        colors.add(Color.parseColor("#526D82"));
        colors.add(Color.parseColor("#27374D"));
        colors.add(Color.parseColor("#DDE6ED"));

        //input data and fit data into pie chart entry
        for(String key: typeAmountMap.keySet()){
            Float val = typeAmountMap.get(key);
            if (val != null) {
                pieEntries.add(new PieEntry(val, key));
            }
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
    private void SetUpHorizontalBarChart(ArrayList<BarEntry> arrayList, ArrayList<String> xAxisValues){
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
        chart.setDrawValueAboveBar(true);


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

        YAxis y = chart.getAxisLeft();
        y.setAxisMaximum(barDataSet.getYMax() + 20);
        y.setAxisMinimum(0);
        chart.setData(barData);
    }

    @Override
    public void onGetData(boolean verdict, Map<String, Map<Double, Long>> stats,  Map<String, Integer> occurrence) {
        // setting the bar chart

        Log.i("tegel", "onGetData: " + stats);
        float counter = 0f;
        chart.setVisibility(View.INVISIBLE);
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (String key: stats.keySet()) {
            for (Double accuracy: Objects.requireNonNull(stats.get(key)).keySet()){
                barEntries.add(new BarEntry(counter, (float) (Math.round((accuracy / occurrence.get(key)) * 100.0) / 100.0)));
                counter += 1f;
            }
        }

        if (!stats.keySet().isEmpty()) {
            SetUpHorizontalBarChart(barEntries, new ArrayList<>(stats.keySet()));
            chart.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onChangeFilter(String mode) {
        this.mode = mode;
        if (mode.equals("Yearly")) {
            monthPicker.setVisibility(View.GONE);
        }
        if (mode.equals("Monthly")) {
            monthPicker.setVisibility(View.VISIBLE);
        }
        presenter.getData(mode, String.valueOf(detailedPickedYear), String.valueOf(detailedPickedMonth));
    }

    @Override
    public void onGetWeight(boolean verdict, String currentWeight, String lostWeight) {
        if (verdict) {
            String temp = currentWeight + " KG";
            currWeight.setText(temp);
        }
    }
}