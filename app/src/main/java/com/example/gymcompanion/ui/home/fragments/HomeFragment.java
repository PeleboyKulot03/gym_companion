package com.example.gymcompanion.ui.home.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gymcompanion.R;
import com.example.gymcompanion.staticValues.DifferentExercise;
import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        CardView header = view.findViewById(R.id.header);

        DifferentExercise exercises = new DifferentExercise();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ArrayList<HomeFragmentModel> pushDay = new ArrayList<>(exercises.getPushDay());


        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getContext(), pushDay);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}