package com.example.gymcompanion.ui.DetailedProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.DetailedProfileModel;

import java.util.ArrayList;
import java.util.Map;

public class DetailedProfileActivity extends AppCompatActivity implements IDetailedProfile{

    private String name, weight, height, imageSrc, id;
    private RecyclerView recyclerView;
    private String program = "pushDay";
    private TextView currDay;
    private Map<String, ArrayList<DetailedProfileModel>> models;
    private LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_profile);
        Intent intent = getIntent();

        TextView nameTV = findViewById(R.id.name);
        TextView weightTV = findViewById(R.id.weight);
        TextView heightTV = findViewById(R.id.height);
        ImageView profilePic = findViewById(R.id.profilePicture);
        recyclerView = findViewById(R.id.recyclerView);
        loadingLayout = findViewById(R.id.loadinLayout);
        TextView pushDay = findViewById(R.id.pushDay);
        TextView pullDay = findViewById(R.id.pullDay);
        TextView legDay = findViewById(R.id.legDay);
        currDay = pushDay;

        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name");
            weight = intent.getStringExtra("weight");
            height = intent.getStringExtra("height");
            imageSrc = intent.getStringExtra("imageSrc");
            id = intent.getStringExtra("id");
        }


        DetailedProfilePresenter presenter = new DetailedProfilePresenter(this);
        presenter.getData(id);

        pushDay.setOnClickListener(v -> {
            if (program.equals("pushDay")) return;
            program = "pushDay";
            currDay.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
            currDay = pushDay;
            currDay.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.round_edit_text));
            setUpRecyclerView();
        });

        pullDay.setOnClickListener(v -> {
            if (program.equals("pullDay")) return;
            program = "pullDay";
            currDay.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
            currDay = pullDay;
            currDay.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.round_edit_text));
            setUpRecyclerView();
        });

        legDay.setOnClickListener(v -> {
            if (program.equals("legDay")) return;
            program = "legDay";
            currDay.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
            currDay = legDay;
            currDay.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.round_edit_text));
            setUpRecyclerView();
        });
        String finalWeight = weight + " kg";
        String finalHeight = height + " cm";
        nameTV.setText(name);
        weightTV.setText(finalWeight);
        heightTV.setText(finalHeight);
        Glide.with(getApplicationContext())
                .load(imageSrc)
                .circleCrop()
                .into(profilePic)
                .waitForLayout();

    }

    private void setUpRecyclerView() {
        recyclerView.removeAllViews();
        loadingLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        DetailedProfileAdapter adapter = new DetailedProfileAdapter(models.get(program), getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onGetProgram(Map<String, ArrayList<DetailedProfileModel>> models) {
        this.models = models;
        setUpRecyclerView();
    }


}