package com.example.gymcompanion.ui.homepage.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.staticValues.DifferentExercise;
import com.example.gymcompanion.ui.registration.RegistrationPageActivity;
import com.example.gymcompanion.utils.HomeFragmentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment implements IHomeFragment {

    private RecyclerView recyclerView;
    private ImageView customProgressBar;
    private TextView count, addInfo;
    private LinearLayout emptyState;
    private TextView description, day;
    private HomeFragmentPresenter presenter;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        addInfo = view.findViewById(R.id.addInformation);
        customProgressBar = view.findViewById(R.id.customProgressBar);
        count = view.findViewById(R.id.count);
        emptyState = view.findViewById(R.id.emptyState);
        description = view.findViewById(R.id.description);
        day = view.findViewById(R.id.day);
        linearLayout = view.findViewById(R.id.loader);

//        CardView header = view.findViewById(R.id.header);
        recyclerView = view.findViewById(R.id.recyclerView);

        presenter = new HomeFragmentPresenter(this);
        presenter.getExercise();
        customProgressBar.startAnimation(animation);
        addInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegistrationPageActivity.class);
            intent.putExtra("key", true);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void getExercise(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {
        customProgressBar.clearAnimation();
        linearLayout.setVisibility(View.GONE);
        day.setText(currentDay);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        ArrayList<String> programs = new ArrayList<>();
        programs.add("Push Day");
        programs.add("Pull Day");
        programs.add("Leg Day");
        programs.add("Rest Day");

        if (date != null && !date.equals(formatter.format(currentDate))) {
            int index = programs.indexOf(currentDay);
            if (index == 3) {
                index = 0;
            }
            else {
                index += 1;
            }
            presenter.getNewExercise(programs.get(index));
        }

        if (!hasInfo) {
            description.setText(getString(R.string.empty_state_description_1));
            addInfo.setVisibility(View.VISIBLE);
        }
        if (!models.isEmpty()) {
            emptyState.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (verdict) {
                HomeFragmentAdapter adapter = new HomeFragmentAdapter(getContext(), getActivity(), models);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            String countValue = finished + "/" + models.size();
            count.setText(countValue);
            return;
        }

        emptyState.setVisibility(View.VISIBLE);
    }
}