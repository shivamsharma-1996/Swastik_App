package com.swastikenterprises.Dashboard;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swastikenterprises.DB.User;
import com.swastikenterprises.Gallery.Catagory.CatagoryActivity;
import com.swastikenterprises.R;
import com.swastikenterprises.login.LoginActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DatabaseReference mRootRef;


    GoogleApiClient mGoogleApiClient;

    private DrawerLayout navDrawer;
    private CircleImageView img_fb;
    private ImageView img_wa;


    private TextView tv_opp;


    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

        setContentView(R.layout.activity_swastik_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Swastik App");


        init();


        navDrawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.drawer_toggle);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (navDrawer.isDrawerVisible(GravityCompat.START))
                {
                    navDrawer.closeDrawer(GravityCompat.START);
                }
                else
                {
                    navDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
        mDrawerToggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void init()
    {

        builder = new AlertDialog.Builder(this);

        tv_opp = findViewById(R.id.tv_opp);


        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SliderPagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);


        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("abc").setValue("true");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(HomeActivity.this, "internet off!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        img_fb = findViewById(R.id.img_fb);
        img_wa = findViewById(R.id.img_wa);


        /*NestedScrollView nsv = findViewById(R.id.nsv);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if (scrollY != oldScrollY)
                {
                    tv_opp.setVisibility(View.GONE);
                }
                else
                {
                    tv_opp.setVisibility(View.VISIBLE);
                }
            }
        });*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.nav_fav)
        {
//
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                startActivity(new Intent(this, HomeActivity.class));

                break;
            case R.id.nav_gallery:
              //  finish();
                startActivity(new Intent(this, CatagoryActivity.class));

                break;
            case R.id.nav_favourite:
                //startActivity(new Intent(this, Favourite.class));

                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                mGoogleApiClient.disconnect();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;

            case R.id.nav_share:
                break;
            case R.id.nav_rate_me:
                break;
            case R.id.nav_bug_report:
                break;

        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        img_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openWhatsApp("9928091259@s.whatsapp.net");

                openWhatsApp();
            }
        });

        img_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        tv_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }




    private void openWhatsApp()
    {
        String smsNumber = "919928091259";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled)
        {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);
        }
        else
        {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri)
    {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed;
    }





    @SuppressLint("RestrictedApi")
    public void showDialog()
    {
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);


        final EditText et = new EditText(this);
//        builder.setTitle("Opportunity to work with us!");
//        builder.setMessage("We need below details");


        View view = getLayoutInflater().inflate(R.layout.home_form, null);
        builder.setView(view);

        /*builder.setPositiveButton("Submit", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
               //Toast.makeText(HomeActivity.this, etResult, Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {

            }
        });
*/
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
