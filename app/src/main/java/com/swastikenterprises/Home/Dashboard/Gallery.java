package com.swastikenterprises.Home.Dashboard;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swastikenterprises.Gallery.Catagories.TabPagerAdapter;
import com.swastikenterprises.R;

public class Gallery extends Fragment
{
    View galleryView;

    private ViewPager mViewPager;
    private TabPagerAdapter tabPagerAdapter;
    private TabLayout mTabLayout;

    public Gallery()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

       /* mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Design Catagories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        galleryView = inflater.inflate(R.layout.fragment_gallery, container, false);


        init();



        return galleryView;
    }

    private void init()
    {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);

        mViewPager =  galleryView.findViewById(R.id.tabPager);
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager() , getContext());
        mViewPager.setAdapter(tabPagerAdapter);

        mTabLayout =  galleryView.findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager,true);
    }




}
