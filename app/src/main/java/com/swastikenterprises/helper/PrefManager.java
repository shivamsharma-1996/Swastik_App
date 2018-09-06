package com.swastikenterprises.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager
{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private  Context _context;
 
    // shared pref mode
    int PRIVATE_MODE = 0;
 
    // Shared preferences file name
    private static final String PREF_NAME = "swastik-welcome";
 
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_VIDEO_LAUNCH = "IS_VIDEO_LAUNCH";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String SESION = "SESION";


    public PrefManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
 
    public void setFirstTimeLaunch(boolean isFirstTime)
    {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setSesion(boolean sesion)
    {
        editor.putBoolean(SESION, sesion);
        editor.commit();
    }


    public boolean getSesion()
    {
        return pref.getBoolean(SESION, false);
    }

 
    public boolean isFirstTimeLaunch()
    {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setVideoLaunch(boolean isVideoLaunch)
    {
        editor.putBoolean(IS_VIDEO_LAUNCH, isVideoLaunch);
        editor.commit();
    }

    public boolean isVideoLaunch()
    {
        return pref.getBoolean(IS_VIDEO_LAUNCH, false);
    }

    public void setUser_name(String name)
    {
        editor.putString(USER_NAME, name);
        editor.commit();
    }

    public  String getUserName()
    {
        return pref.getString(USER_NAME, "");
    }

    public  String getUserEmail()
    {
        return pref.getString(USER_EMAIL, "");
    }

    public void setEmail(String email)
    {
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public void clearSharedPref()
    {
        editor.clear();
        editor.commit();
    }
}