package com.example.gymcompanion.ui.Homepage.fragments.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.SavedVideos.SaveVideosActivity;
import com.example.gymcompanion.ui.LoginPage.LoginPageActivity;
import com.example.gymcompanion.ui.Settings.PrivacySettingsActivity;
import com.example.gymcompanion.ui.Settings.UserSettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {
    private AccountFragmentPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        TextView savedVideos = view.findViewById(R.id.savedVideos);
        TextView displayName = view.findViewById(R.id.displayName);
        TextView logout = view.findViewById(R.id.logout);
        TextView userSettings = view.findViewById(R.id.userProfile);
        TextView privacySettings = view.findViewById(R.id.privacySetting);
        TextView settings = view.findViewById(R.id.settings);
        ImageView profilePicture = view.findViewById(R.id.profilePicture);


        userSettings.setOnClickListener(v -> startActivity(new Intent(getContext(), UserSettingsActivity.class)));
        privacySettings.setOnClickListener(v -> startActivity(new Intent(getContext(), PrivacySettingsActivity.class)));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String dName = user.getDisplayName();
            Uri uri = user.getPhotoUrl();
            Log.i("TAG", "onCreateView: " + uri);

            if (getContext() != null) {
                if (uri != null) {
                    Glide.with(getContext())
                            .load(uri)
                            .circleCrop()
                            .into(profilePicture)
                            .waitForLayout();
                }
                displayName.setText(dName);
            }
        }

        presenter = new AccountFragmentPresenter();


        logout.setOnClickListener(view1 -> {
            presenter.signOut();
            if (getActivity() != null) {
                startActivity(new Intent(getContext(), LoginPageActivity.class));
                getActivity().finish();
            }
        });

        savedVideos.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), SaveVideosActivity.class));
        });
        return view;
    }
}