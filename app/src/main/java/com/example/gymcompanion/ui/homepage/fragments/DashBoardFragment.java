package com.example.gymcompanion.ui.homepage.fragments;

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
import com.example.gymcompanion.utils.DashBoardFragmentModel;

import java.util.ArrayList;

public class DashBoardFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        RecyclerView verticalRecyclerView = view.findViewById(R.id.verticalRecyclerView);
        RecyclerView horizontalRecyclerView = view.findViewById(R.id.horizontalRecyclerView);

        DifferentExercise differentExercise = new DifferentExercise(getContext());

        ArrayList<String> programs = new ArrayList<>(differentExercise.getDRAWABLES().keySet());
        ArrayList<DashBoardFragmentModel> models = new ArrayList<>();
        DashBoardFragmentModel model = new DashBoardFragmentModel("Weight Loss", 55, 55);
        DashBoardFragmentModel model1 = new DashBoardFragmentModel("Weight Loss", 90, 90);
        DashBoardFragmentModel model2 = new DashBoardFragmentModel("Weight Loss", 20, 20);

        models.add(model);
        models.add(model1);
        models.add(model2);
        models.add(model);

        DashBoardFragmentAdapter adapter = new DashBoardFragmentAdapter(programs, getContext());
        DashBoardFragmentAdapterCards adapterCards = new DashBoardFragmentAdapterCards(models, getContext());

        horizontalRecyclerView.setAdapter(adapterCards);
        verticalRecyclerView.setAdapter(adapter);

        horizontalRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}