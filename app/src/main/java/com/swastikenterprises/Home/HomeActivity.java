package com.swastikenterprises.Home;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.swastikenterprises.Home.Dashboard.Favourites.Favourite;
import com.swastikenterprises.Home.Dashboard.Gallery;
import com.swastikenterprises.Home.Dashboard.Home.Home;
import com.swastikenterprises.Home.Dashboard.Offer.Offers;
import com.swastikenterprises.Home.Dashboard.Service.Service;
import com.swastikenterprises.R;
import com.swastikenterprises.helper.BottomNavigationViewHelper;
import com.swastikenterprises.helper.PrefManager;
import com.swastikenterprises.login.LoginActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    // GoogleApiClient mGoogleApiClient;

    private DrawerLayout navDrawer;
    private String name, email;

    private PrefManager prefManager;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swastik_home);

        prefManager = new PrefManager(this);
        if (prefManager.getUserEmail() != null && prefManager.getUserName() != null) {
            name = prefManager.getUserName();
            email = prefManager.getUserEmail();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String offerFragment = bundle.getString("tag");

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (offerFragment != null && offerFragment.equals("offer")) {
                Offers offerFrag = new Offers();
                fragmentTransaction.replace(android.R.id.content, offerFrag);

            } else {
                // Activity was not launched with a menuFragment selected -- continue as if this activity was opened from a launcher (for example)
                Home standardFragment = new Home();
                fragmentTransaction.replace(android.R.id.content, standardFragment);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Swastik App");

        init();

        navDrawer = findViewById(R.id.drawerLayout);


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);                    //disable "hamburger to arrow" drawable
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.drawer_toggle);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navDrawer.isDrawerVisible(GravityCompat.START)) {
                    navDrawer.closeDrawer(GravityCompat.START);
                } else {
                    navDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.tvName);
        TextView userEmail = headerView.findViewById(R.id.tvEmail);
        userName.setText(name);
        userEmail.setText(email);

        loadFragment(new Home());

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        //Log.i("user0", String.valueOf(mGoogleApiClient.isConnected()));

        // Toast.makeText(this, "home come", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new Home();
                item.setIcon(R.drawable.ic_home);
                return loadFragment(fragment);

            case R.id.nav_gallery:
                fragment = new Gallery();
                return loadFragment(fragment);

            case R.id.nav_favourite:
                fragment = new Favourite();
                return loadFragment(fragment);


            case R.id.nav_services:
                fragment = new Service();
                return loadFragment(fragment);

            case R.id.nav_logout:
                Log.i("main", "login");
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                prefManager.clearSharedPref();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;

            case R.id.nav_offer:
                fragment = new Offers();
                return loadFragment(fragment);

            case R.id.nav_rate_me:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=com.swastikenterprises" + getPackageName())));
                }
                //  Toast.makeText(this, "nav_rate_me", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Swastik App");
                    String sAux = "Interior Decor Selection become Easy just click below Link and download Mobile app to find Various Segments what u want(HOME Decor,Furnishing,HOME utility & Gifts)\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.swastikenterprises";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
        }
        navDrawer.closeDrawers();
        return true;
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void init() {
       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(HomeActivity.this, "internet off!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();*/

        // Log.i("user1", String.valueOf(mGoogleApiClient.isConnected()));

    }


    @Override
    protected void onPause() {
        super.onPause();
        // Log.i("user2", String.valueOf(mGoogleApiClient.isConnected()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Log.i("user3", String.valueOf(mGoogleApiClient.isConnected()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Log.i("user4", String.valueOf(mGoogleApiClient.isConnected()));
        // Log.i("user4", String.valueOf((FirebaseAuth.getInstance().getCurrentUser()!= null)));
    }
}
