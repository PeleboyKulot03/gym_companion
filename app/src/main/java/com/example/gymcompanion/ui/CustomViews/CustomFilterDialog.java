package com.example.gymcompanion.ui.CustomViews;

import androidx.appcompat.content.res.AppCompatResources;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.homepage.fragments.dashboard.IDetailedDashBoard;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomFilterDialog extends Dialog {
    private String chip = "Monthly";
    private Map<String, TextView> textViewMap;
    private Drawable ACTIVE, INACTIVE;
    private final IDetailedDashBoard iDetailedDashBoard;

    public CustomFilterDialog(Activity activity, IDetailedDashBoard iDetailedDashBoard) {
        super(activity);
        this.iDetailedDashBoard = iDetailedDashBoard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_filter_dialog);

        ACTIVE = AppCompatResources.getDrawable(getContext(), R.drawable.round_edit_text);
        INACTIVE = AppCompatResources.getDrawable(getContext(), R.drawable.rounded_border);


        TextView monthly = findViewById(R.id.monthly);
        TextView weekly = findViewById(R.id.weekly);
        TextView custom = findViewById(R.id.custom);
        TextView cancel = findViewById(R.id.cancel);
        TextView apply = findViewById(R.id.apply);
        LinearLayout customHolder = findViewById(R.id.customHolder);
//        MaterialCalendarView materialCalendarView = findViewById(R.id.calendarView);


        textViewMap = new HashMap<>();
        textViewMap.put("Monthly", monthly);
        textViewMap.put("Weekly", weekly);
        textViewMap.put("Custom", custom);

        monthly.setOnClickListener(view -> {
            if (chip.equals("Monthly")) return;
            customHolder.setVisibility(View.GONE);
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(INACTIVE);
            chip = "Monthly";
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(ACTIVE);
//            materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
//            materialCalendarView.setVisibility(View.VISIBLE);
        });

        weekly.setOnClickListener(view -> {
            if (chip.equals("Weekly")) return;
            customHolder.setVisibility(View.GONE);
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(INACTIVE);
            chip = "Weekly";
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(ACTIVE);
//            materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
//            materialCalendarView.setVisibility(View.VISIBLE);
        });

        custom.setOnClickListener(view -> {
            if (chip.equals("Custom")) return;
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(INACTIVE);
            chip = "Custom";
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(ACTIVE);
            customHolder.setVisibility(View.VISIBLE);
//            materialCalendarView.setVisibility(View.GONE);
        });

        cancel.setOnClickListener(view -> dismiss());
        apply.setOnClickListener(view -> {
            iDetailedDashBoard.onChangeFilter(chip);
            dismiss();
        });
    }
}