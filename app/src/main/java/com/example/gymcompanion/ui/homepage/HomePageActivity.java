package com.example.gymcompanion.ui.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.homepage.fragments.AccountFragment;
import com.example.gymcompanion.ui.homepage.fragments.DashBoardFragment;
import com.example.gymcompanion.ui.homepage.fragments.ExploreFragment;
import com.example.gymcompanion.ui.homepage.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private ExploreFragment findFriendsFragment;
    private AccountFragment accountFragment;
    private DashBoardFragment dashBoardFragment;
    private static final int menu = R.id.menu;
    private static final int home = R.id.home;
    private static final int findFriends = R.id.findFriend;
    private static final int dashboard = R.id.dashboard;
    private static final int account = R.id.account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        homeFragment = new HomeFragment();
        findFriendsFragment = new ExploreFragment();
        accountFragment = new AccountFragment();
        dashBoardFragment = new DashBoardFragment();

        DrawerLayout mDrawer =  findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == menu){
                mDrawer.openDrawer(GravityCompat.END);
            }
            return true;
        });
        // switch fragment to default home
        switchFragment(homeFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            toolbar.setVisibility(View.GONE);
            if (item.getItemId() == home) {
                switchFragment(homeFragment);
            }
            if (item.getItemId() == dashboard) {
                switchFragment(dashBoardFragment);
                toolbar.setVisibility(View.VISIBLE);
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