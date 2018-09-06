package com.swastikenterprises.start;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.swastikenterprises.Home.HomeActivity;
import com.swastikenterprises.R;
import com.swastikenterprises.helper.ConnectionDetector;
import com.swastikenterprises.helper.PrefManager;
import com.swastikenterprises.login.LoginActivity;

public class SplashActivity extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT = 3000;
    private PrefManager prefManager;
    private ConnectionDetector cd;
    FullScreenVideoView vidHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(SplashActivity.this);

        cd = new ConnectionDetector(this);

        if(cd.isNetworkConnected())
        {
            //not connected
            new AlertDialog.Builder(this)
                    .setMessage("Please Connect your Device to Network to continue!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }).setCancelable(false)
                    .create()
                    .show();
        }
        else
        {
            if(!prefManager.isVideoLaunch())
            {
                setContentView(R.layout.activity_splash2);

                Button skip  = findViewById(R.id.btSkip);

                skip.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        jump();
                    }
                });

                try
                {
                    vidHolder = findViewById(R.id.fullScreenVideoView);

                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.swastik);
                    vidHolder.setVideoURI(video);
                    vidHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                    {
                        public void onCompletion(MediaPlayer mp)
                        {
                            jump();
                        }
                    });
                    vidHolder.start();
                }
                catch(Exception ex)
                {
                    jump();
                }
            }
            else if(prefManager.isVideoLaunch())
            {
                setContentView(R.layout.activity_splash1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        if (!prefManager.isFirstTimeLaunch())
                            {
                                if(prefManager.getSesion())
                                {
                                    Log.i("evbv", "1");
                                    //Toast.makeText(SplashActivity.this, "showsplash12", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
                                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //Toast.makeText(SplashActivity.this, "showsplash22", Toast.LENGTH_SHORT).show();
                                    startActivity(homeIntent);
                                    finish();
                                }
                                else
                                {
                                    Log.i("evbv", "2");
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                   // Toast.makeText(SplashActivity.this, "showsplash32", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            else
                        {
                            Intent i = new Intent(SplashActivity.this, WalkthroughActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(i);
                            finish();
                        }
                        }
                }, SPLASH_TIME_OUT);
            }
            }
        }


    @Override
    protected void onPause()
    {
        super.onPause();
       try
       {
           vidHolder.pause();
       }
       catch (Exception e)
       {
           jump();
       }

    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        try
        {

            vidHolder.resume();
        }
        catch(Exception ex)
        {
            jump();
        }


    }

    private void jump()
    {
            Intent i = new Intent(SplashActivity.this, WalkthroughActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return;
    }

}
