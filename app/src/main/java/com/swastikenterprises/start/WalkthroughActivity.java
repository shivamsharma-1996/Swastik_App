package com.swastikenterprises.start;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swastikenterprises.R;
import com.swastikenterprises.login.LoginActivity;

public class WalkthroughActivity extends AppCompatActivity
{
  /*  private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;*/

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int layouts;
    private FloatingActionButton btnSkip, btnNext;
    private PrefManager prefManager;

    private int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch())
        {
            launchHomeScreen();
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_walkthrough);

        viewPager =  findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip =    findViewById(R.id.btn_skip);
        btnNext =    findViewById(R.id.btn_next);

        images =new int[]
                {
                        R.drawable.img_first,
                        R.drawable.img_second,
                        R.drawable.img_third,
                        R.mipmap.ic_launcher
                };

        layouts = R.layout.slide_layout;
        addBottomDots(0);
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter(images);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchHomeScreen();

                Toast.makeText(WalkthroughActivity.this, "clcick", Toast.LENGTH_SHORT).show();
                Intent facebookIntent = getOpenFacebookIntent(WalkthroughActivity.this);
                startActivity(facebookIntent);

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < 4)
                {
                    //move to next screen
                    viewPager.setCurrentItem(current);
                }
                else
                {
                    //Got it button cicked
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage)
    {
        dots = new TextView[4];

        int colorsActive = getResources().getColor(R.color.activeDot);
        int colorsInactive = getResources().getColor(R.color.inactiveDot);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
        {
            dots[currentPage].setTextColor(colorsActive);
        }
    }

    private int getItem(int i)
    {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen()
    {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WalkthroughActivity.this, LoginActivity.class));
        finish();
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageSelected(int position)
        {
            addBottomDots(position);

            if (position == 4 - 1)
            {
                //last page. make button text to GOT IT
                //btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            }
            else
            {
                //still pages are left
               // btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {

        }
    };


    private void changeStatusBarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;
        private ImageView img;
        private int[] images;

        public MyViewPagerAdapter(int[] images)
        {
            this.images=images;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View view = getLayoutInflater().inflate(R.layout.slide_layout, container, false);
            img  = view.findViewById(R.id.img);
            //img.setImageResource(images[position]);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount()
        {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj)
        {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public static Intent getOpenFacebookIntent(Context context)
    {
        try
        {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/254175194653125")); //Trys to make intent with FB's URI
        }
        catch (Exception e)
        {
            Log.i("ece", String.valueOf(e));
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/IamSwastikenterprises/")); //catches and opens home_form url to the desired page
           //https://www.facebook.com/profile.php?id=100004383791076
        }
    }
}


