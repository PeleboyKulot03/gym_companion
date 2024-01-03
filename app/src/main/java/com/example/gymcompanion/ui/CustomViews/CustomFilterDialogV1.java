package com.example.gymcompanion.ui.CustomViews;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.gymcompanion.ui.homepage.fragments.dashboard.IDashBoardFragment;
import com.example.gymcompanion.ui.homepage.fragments.dashboard.IDetailedDashBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomFilterDialogV1 extends Dialog {
    private IDashBoardFragment iDashBoardFragment;
    private String chip = "Yearly";
    private Map<String, TextView> textViewMap;
    private Drawable ACTIVE, INACTIVE;
    public CustomFilterDialogV1(Activity activity, IDashBoardFragment iDashBoardFragment) {
        super(activity);
        this.iDashBoardFragment = iDashBoardFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_filter_dialog_v1);

        ACTIVE = AppCompatResources.getDrawable(getContext(), R.drawable.round_edit_text);
        INACTIVE = AppCompatResources.getDrawable(getContext(), R.drawable.rounded_border);


        TextView yearly = findViewById(R.id.yearly);
        TextView monthly = findViewById(R.id.monthly);
        TextView cancel = findViewById(R.id.cancel);
        TextView apply = findViewById(R.id.apply);

        textViewMap = new HashMap<>();
        textViewMap.put("Yearly", yearly);
        textViewMap.put("Monthly", monthly);

        yearly.setOnClickListener(view -> {
            if (chip.equals("Yearly")) return;
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(INACTIVE);
            chip = "Yearly";
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(ACTIVE);
        });

        monthly.setOnClickListener(view -> {
            if (chip.equals("Monthly")) return;
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(INACTIVE);
            chip = "Monthly";
            Objects.requireNonNull(textViewMap.get(chip)).setBackground(ACTIVE);
        });

        cancel.setOnClickListener(view -> dismiss());
        apply.setOnClickListener(view -> {
            iDashBoardFragment.onChangeFilter(chip);
            dismiss();
        });
    }
}