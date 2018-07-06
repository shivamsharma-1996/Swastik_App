package com.swastikenterprises.Utility;

import android.content.Context;
import android.widget.Toast;

final public class Utills
{

    private Utills()
    {
    }

    public static void makeToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
