package com.example.gymcompanion.ui.Homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.Homepage.fragments.account.AccountFragment;
import com.example.gymcompanion.ui.Homepage.fragments.dashboard.DashBoardFragment;
import com.example.gymcompanion.ui.Homepage.fragments.explore.ExploreFragment;
import com.example.gymcompanion.ui.Homepage.fragments.dashboard.DetailedDashboardActivity;
import com.example.gymcompanion.ui.Homepage.fragments.home.HomeFragment;
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
    private static final int DIPS = R.id.dips;
    private static final int DUMBBELL_SHOULDER_PRESS = R.id.dumbbellPress;
    private static final int FLAT_BENCH_PRESS = R.id.flatBenchPress;
    private static final int INCLINE_BENCH_PRESS = R.id.inclineBenchPress;

    private static final int SIDE_LATERAL = R.id.sideLateral;
    private static final int SKULL_CRUSHER = R.id.skullCursher;
    private static final int BARBELL_CURLS = R.id.barbellCurls;
    private static final int BARBELL_ROWS = R.id.barbellRows;
    private static final int DEADLIFT = R.id.deadLift;
    private static final int DUMBBELL_ROWS = R.id.dumbbellRows;
    private static final int PREACHER_CURLS = R.id.preacherCurls;
    private static final int LEG_EXTENSION = R.id.legExtension;
    private static final int LUNGES = R.id.lunges;
    private static final int ROMANIAN_DEADLIFT = R.id.rdl;
    private static final int SQUAT = R.id.squats;



    private Toolbar toolbar;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        toolbar = findViewById(R.id.toolbar);
        homeFragment = new HomeFragment();
        findFriendsFragment = new ExploreFragment();
        accountFragment = new AccountFragment();
        dashBoardFragment = new DashBoardFragment();
        DrawerLayout mDrawer =  findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);

        navView.setNavigationItemSelectedListener(item -> {
            Intent intent = new Intent(getApplicationContext(), DetailedDashboardActivity.class);

            if (item.getItemId() == DIPS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == FLAT_BENCH_PRESS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == DUMBBELL_SHOULDER_PRESS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == INCLINE_BENCH_PRESS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == SIDE_LATERAL) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == SKULL_CRUSHER) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == BARBELL_CURLS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == BARBELL_ROWS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == DEADLIFT) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == DUMBBELL_ROWS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == PREACHER_CURLS) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == LEG_EXTENSION) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == LUNGES) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == ROMANIAN_DEADLIFT) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }
            if (item.getItemId() == SQUAT) {
                intent.putExtra("exercise", item.getTitle());
                startActivity(intent);
            }

            return true;
        });
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
            toolbar.getMenu().clear();
            if (item.getItemId() == home) {
                switchFragment(homeFragment);
            }
            if (item.getItemId() == dashboard) {
                switchFragment(dashBoardFragment);
                toolbar.inflateMenu(R.menu.hamburger_menu);
                toolbar.setTitle(R.string.dashboard);
                toolbar.setVisibility(View.VISIBLE);
            }
            if (item.getItemId() == findFriends) {
                switchFragment(findFriendsFragment);
                toolbar.inflateMenu(R.menu.explore_menu);
                toolbar.setTitle(R.string.explore);
                toolbar.setVisibility(View.VISIBLE);

                SearchManager searchManager = (SearchManager) HomePageActivity.this.getSystemService(Context.SEARCH_SERVICE);
                MenuItem searchItem = toolbar.getMenu().findItem(R.id.search);

                if (searchItem != null) {
                    searchView = (SearchView) searchItem.getActionView();
                }
                if (searchView != null) {
                    searchView.setQueryHint("Search here...");
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(HomePageActivity.this.getComponentName()));
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
                            // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                            return false;
                        }
                    });
                }
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