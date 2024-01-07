package com.example.gymcompanion.ui.Homepage.fragments.explore;

import com.example.gymcompanion.utils.ExploreFragmentModel;

public class ExploreFragmentPresenter {
    private final IExploreFragment iExploreFragment;

    public ExploreFragmentPresenter(IExploreFragment iExploreFragment) {
        this.iExploreFragment = iExploreFragment;
    }

    public void onGetUsers() {
        ExploreFragmentModel model = new ExploreFragmentModel();
        model.getUser(iExploreFragment::onGetData);
    }
}
