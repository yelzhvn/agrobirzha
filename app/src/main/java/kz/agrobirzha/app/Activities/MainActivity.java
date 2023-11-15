package kz.agrobirzha.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import kz.agrobirzha.app.Fragments.CartFragment;
import kz.agrobirzha.app.Fragments.FavoriteFragment;
import kz.agrobirzha.app.Fragments.HomeFragment;
import kz.agrobirzha.app.Fragments.ListFragment;
import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.Helpers.SessionManager;
import kz.agrobirzha.app.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    Fragment selectedFragment = null;
    NavigationView navigationView;
    TextView nav_username, nav_email;
    View headerLayout;
    private SQLiteHandler db;
    private SessionManager session;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_list:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_favorite:
                    selectedFragment = new FavoriteFragment();
                    break;
                case R.id.navigation_cart:
                    selectedFragment = new CartFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu navigationMenu = navigationView.getMenu();
        headerLayout = navigationView.getHeaderView(0);
        navigationMenu.findItem(R.id.nav_signin).setVisible(true);
        navigationMenu.findItem(R.id.nav_signup).setVisible(true);
        navigationMenu.findItem(R.id.nav_myitems).setVisible(false);
        navigationMenu.findItem(R.id.nav_logout).setVisible(false);
        navigationMenu.findItem(R.id.nav_ploffer).setVisible(false);
        headerLayout.findViewById(R.id.nav_plsEnter).setVisibility(View.VISIBLE);
        headerLayout.findViewById(R.id.nav_email).setVisibility(View.GONE);
        headerLayout.findViewById(R.id.nav_avatar).setVisibility(View.GONE);
        headerLayout.findViewById(R.id.nav_username).setVisibility(View.GONE);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signup) {
            Intent intent = new Intent(MainActivity.this, SideMenuActivity.class);
            intent.putExtra("page", "signup");
            startActivity(intent);
        } else if (id == R.id.nav_signin) {
            Intent intent = new Intent(MainActivity.this, SideMenuActivity.class);
            intent.putExtra("page", "signin");
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            logoutUser();
        } else if (id == R.id.nav_ploffer) {
            Intent intent = new Intent(MainActivity.this, SideMenuActivity.class);
            intent.putExtra("page", "ploffer");
            startActivity(intent);
        } else if (id == R.id.nav_myitems) {
            Intent intent = new Intent(MainActivity.this, SideMenuActivity.class);
            intent.putExtra("page", "myitems");
            startActivity(intent);
        }else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(MainActivity.this, SideMenuActivity.class);
            intent.putExtra("page", "contacts");
            startActivity(intent);
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, SideMenuActivity.class);
            intent.putExtra("page", "about");
            startActivity(intent);
        }else if (id == R.id.nav_dbsettings) {
            Intent intent = new Intent(MainActivity.this, AndroidDatabaseManager.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (session.isLoggedIn()) {
            Menu navigationMenu = navigationView.getMenu();
            navigationMenu.findItem(R.id.nav_signin).setVisible(false);
            navigationMenu.findItem(R.id.nav_signup).setVisible(false);
            navigationMenu.findItem(R.id.nav_myitems).setVisible(true);
            navigationMenu.findItem(R.id.nav_logout).setVisible(true);
            navigationMenu.findItem(R.id.nav_ploffer).setVisible(true);
            headerLayout.findViewById(R.id.nav_plsEnter).setVisibility(View.GONE);
            headerLayout.findViewById(R.id.nav_avatar).setVisibility(View.VISIBLE);
            headerLayout.findViewById(R.id.nav_username).setVisibility(View.VISIBLE);
            headerLayout.findViewById(R.id.nav_email).setVisibility(View.VISIBLE);
            nav_email = headerLayout.findViewById(R.id.nav_email);
            nav_username = headerLayout.findViewById(R.id.nav_username);
            HashMap<String, String> user = db.getUserDetails();

            String name = user.get("name");
            String email = user.get("email");
            nav_email.setText(name);
            nav_username.setText(email);
        }
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        finish();startActivity(getIntent());
    }
    public void replaceFragments(int CategoryId) {
        Bundle bundle = new Bundle();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        ListFragment fragobj = new ListFragment();
        if(CategoryId == 1) {
            bundle.putString("category", "1");
            fragobj.setArguments(bundle);
        }else if(CategoryId == 2){
            bundle.putString("category", "2");
            fragobj.setArguments(bundle);
        }else if(CategoryId == 3){
            bundle.putString("category", "3");
            fragobj.setArguments(bundle);
        }
        transaction.replace(R.id.fragment_container, fragobj);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
