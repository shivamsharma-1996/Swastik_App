package com.swastikenterprises.Gallery.Catagory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.swastikenterprises.R;

public class CatagoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private TabPagerAdapter tabPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catagory_layout);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Design Catagories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager =  findViewById(R.id.tabPager);
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager() , this);
        mViewPager.setAdapter(tabPagerAdapter);

        mTabLayout =  findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        }
}
