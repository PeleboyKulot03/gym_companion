package com.example.gymcompanion.ui.Homepage.fragments.explore;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.ExploreFragmentModel;
import java.util.ArrayList;

public class ExploreFragment extends Fragment implements IExploreFragment{
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<ExploreFragmentModel> models;
    private ExploreFragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        models = new ArrayList<>();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.explore);
        toolbar.setVisibility(View.VISIBLE);

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setQueryHint("Search here...");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Toast like print
                    Log.i("ing","onQueryTextSubmit: " + query);
                    if(!searchView.isIconified()) {
                        searchView.setIconified(true);
                    }
                    if (searchItem != null) {
                        searchItem.collapseActionView();
                    }
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    ArrayList<ExploreFragmentModel> tempModel = new ArrayList<>();
                    for (ExploreFragmentModel model: models) {
                        if (model.getDisplayName().toLowerCase().contains(s.toLowerCase())) {
                            tempModel.add(model);
                        }
                    }
                    recyclerView.removeAllViews();
                    adapter = new ExploreFragmentAdapter(getContext(), getActivity(), (s.isEmpty() ? models: tempModel));
                    recyclerView.setAdapter(adapter);
                    return false;
                }
            });
        }
        ExploreFragmentPresenter presenter = new ExploreFragmentPresenter(ExploreFragment.this);
        presenter.onGetUsers();
        return view;
    }

    @Override
    public void onGetData(boolean verdict, ArrayList<ExploreFragmentModel> models) {
        this.models = models;
        adapter = new ExploreFragmentAdapter(getContext(), getActivity(), models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}