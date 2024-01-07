package com.example.gymcompanion.ui.Homepage.fragments.explore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.ExploreFragmentModel;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements IExploreFragment{
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        ExploreFragmentPresenter presenter = new ExploreFragmentPresenter(ExploreFragment.this);
        presenter.onGetUsers();
        return view;
    }

    @Override
    public void onGetData(boolean verdict, ArrayList<ExploreFragmentModel> models) {
        ExploreFragmentAdapter adapter = new ExploreFragmentAdapter(getContext(), models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}