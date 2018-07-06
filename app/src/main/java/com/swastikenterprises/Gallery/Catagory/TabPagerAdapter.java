package com.swastikenterprises.Gallery.Catagory;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;




/**
 * Created by shivam sharma on 8/9/2017.
 */

public class TabPagerAdapter extends FragmentPagerAdapter
{
    private Context context;

    public TabPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                Interior interior = new Interior();
                return interior;
            case 1:
                Exterior exterior = new Exterior();
                return exterior;
            case 2:
                Home home = new Home();
                return home;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Interior\nExclusive";
            case 1:
                return "Exterior\nExclusive";
            case 2:
                return "Home\nUtility";
            default:
                return null;

        }
    }
}

