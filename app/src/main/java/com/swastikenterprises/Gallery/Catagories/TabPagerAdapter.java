package com.swastikenterprises.Gallery.Catagories;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;

import com.swastikenterprises.helper.ConnectionDetector;


/**
 * Created by shivam sharma on 8/9/2017.
 */

public class TabPagerAdapter extends FragmentPagerAdapter
{
    private Context context;
    private ConnectionDetector cd;

     AlertDialog alertDialog;
     AlertDialog.Builder builder;

    public TabPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        cd = new ConnectionDetector(context);

        if(cd.isNetworkConnected())
        {
            //not connected
             builder =  new AlertDialog.Builder(context);
            builder.setMessage("Please Connect your Device to Network to continue!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            alertDialog.dismiss();
                        }
                    });
             alertDialog = builder.create();
            alertDialog.show();

        }
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
            case 3:
                Furniture furniture = new Furniture();
                return furniture;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
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
            case 3:
                return "Furniture";
            default:
                return null;
        }
    }
}

