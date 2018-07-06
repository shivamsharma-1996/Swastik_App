package com.swastikenterprises.Gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import com.swastikenterprises.R;
import java.io.File;
import java.io.FileOutputStream;


public class FullScreenActivity extends AppCompatActivity {

    private static final String TAG = FullScreenActivity.class.getSimpleName();
    private int selectedImg;
    PhotoView mPhoto_view;

    //Toolbar
    private Toolbar fullScreen_toolbar;
    ActionBar actionBar;

    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);      //full scree mode
         requestWindowFeature(1);
         getWindow().setFlags(1024, 1024);                                  //also for full scree mode

        setContentView( R.layout.activity_full_screen);

        mPhoto_view = findViewById(R.id.photo_view);

        fullScreen_toolbar = findViewById(R.id.fullScreen_toolbar);
        setSupportActionBar(fullScreen_toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));



        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            selectedImg = bundle.getInt("clicked_image");
            Log.i(TAG, String.valueOf(selectedImg));
         //   mPhoto_view.setImageResource(selectedImg);
            mPhoto_view.setImageResource(R.drawable.customwardrob);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.fullscreen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_fav:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(this, "fav is Selected", Toast.LENGTH_SHORT).show();
                //item.setIcon(R.drawable.ic_favorite);
                if(!isFavourite)
                {
                    item.setIcon(R.drawable.ic_favorite);
                     isFavourite = true;
                    // createFavourite();
                }
                else
                {
                    item.setIcon(R.drawable.ic_nonfavorite);
                    isFavourite = false;
                   // createUnFavourite();
                }

                return true;

            case R.id.menu_share:
                Toast.makeText(this, "share is Selected", Toast.LENGTH_SHORT).show();
                shareContent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void shareContent()
    {
        Bitmap bitmap =getBitmapFromView(mPhoto_view);
        try {
            File file = new File(this.getExternalCacheDir(),"logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, "Catagories to be send");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }



}

