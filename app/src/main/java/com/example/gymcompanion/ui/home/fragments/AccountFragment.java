package com.example.gymcompanion.ui.home.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.loginpage.LoginPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {
    private AccountFragmentPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);



        TextView displayName = view.findViewById(R.id.displayName);
        TextView logout = view.findViewById(R.id.logout);
        ImageView profilePicture = view.findViewById(R.id.profilePicture);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String dName = user.getDisplayName();
            Uri uri = user.getPhotoUrl();

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
        return view;
    }
}