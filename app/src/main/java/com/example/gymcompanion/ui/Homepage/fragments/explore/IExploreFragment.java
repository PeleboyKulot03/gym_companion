package com.example.gymcompanion.ui.Homepage.fragments.explore;

import com.example.gymcompanion.utils.ExploreFragmentModel;

import java.util.ArrayList;

public interface IExploreFragment {
    void onGetData(boolean verdict, ArrayList<ExploreFragmentModel> models);
}
