package com.example.gymcompanion.ui.homePage.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        CardView header = view.findViewById(R.id.header);

        recyclerView = view.findViewById(R.id.recyclerView);
        HomeFragmentModel model = new HomeFragmentModel(getString(R.string.sample_program), getString(R.string.sample_reps));
        ArrayList<HomeFragmentModel> models = new ArrayList<>();
        models.add(model);
        models.add(model);
        models.add(model);
        models.add(model);
        models.add(model);
        models.add(model);
        models.add(model);


        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getContext(), models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }
}