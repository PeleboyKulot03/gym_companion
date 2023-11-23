package com.example.gymcompanion.ui.homepage.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.staticValues.DifferentExercise;
import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements IHomeFragment {

    private RecyclerView recyclerView;
    private ImageView customProgressBar;
    private TextView count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        customProgressBar = view.findViewById(R.id.customProgressBar);
        count = view.findViewById(R.id.count);

//        CardView header = view.findViewById(R.id.header);
        recyclerView = view.findViewById(R.id.recyclerView);

        HomeFragmentPresenter presenter = new HomeFragmentPresenter(this);
        presenter.getExercise();
        customProgressBar.startAnimation(animation);

        return view;
    }

    @Override
    public void getExercise(boolean verdict, ArrayList<HomeFragmentModel> models, int finished) {
        customProgressBar.clearAnimation();
        if (verdict) {
            HomeFragmentAdapter adapter = new HomeFragmentAdapter(getContext(), getActivity(), models);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        String countValue = finished + "/" + models.size();
        count.setText(countValue);

    }
}