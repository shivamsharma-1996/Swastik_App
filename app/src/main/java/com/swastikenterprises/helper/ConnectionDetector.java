package com.swastikenterprises.helper;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionDetector
{
    private Context context;
    public ConnectionDetector(Context context)
    {
        this.context = context;
    }

    public   boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() == null;

    }
}
