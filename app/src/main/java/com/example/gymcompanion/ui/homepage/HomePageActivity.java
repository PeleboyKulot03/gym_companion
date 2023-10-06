package com.example.gymcompanion.ui.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.homepage.fragments.AccountFragment;
import com.example.gymcompanion.ui.homepage.fragments.ExploreFragment;
import com.example.gymcompanion.ui.homepage.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private ExploreFragment findFriendsFragment;
    private AccountFragment accountFragment;
    private static final int home = R.id.home;
    private static final int findFriends = R.id.findFriend;
    private static final int account = R.id.account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);

        homeFragment = new HomeFragment();
        findFriendsFragment = new ExploreFragment();
        accountFragment = new AccountFragment();

        // switch fragment to default home
        switchFragment(homeFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == home) {
                switchFragment(homeFragment);
            }
            if (item.getItemId() == findFriends) {
                switchFragment(findFriendsFragment);
            }
            if (item.getItemId() == account) {
                switchFragment(accountFragment);
            }

            return true;
        });
    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }
}