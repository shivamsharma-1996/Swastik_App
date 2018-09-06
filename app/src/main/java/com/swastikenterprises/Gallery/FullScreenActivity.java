package com.swastikenterprises.Gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.swastikenterprises.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class FullScreenActivity extends AppCompatActivity {

    private static final String TAG = FullScreenActivity.class.getSimpleName();
    private String selectedImg;
    PhotoViewAttacher mPhotoViewAttacher;
    private ImageView img;
    private ProgressBar mProgressDialog;


    //Toolbar
    private Toolbar fullScreen_toolbar;
    ActionBar actionBar;
    private DatabaseReference mRootRef;

    private boolean isFavourite = false;
    Menu mMenu;

    private String catagory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);      //full scree mode
         requestWindowFeature(1);
         getWindow().setFlags(1024, 1024);                                  //also for full scree mode


        Bundle bundle = getIntent().getExtras();

        if(bundle!= null)
        {
            catagory = bundle.getString("catagory");
        }

        setContentView( R.layout.activity_full_screen);

        img = findViewById(R.id.img);
        mPhotoViewAttacher = new PhotoViewAttacher(img);
        mProgressDialog = findViewById(R.id.progress);


        fullScreen_toolbar = findViewById(R.id.fullScreen_toolbar);
        setSupportActionBar(fullScreen_toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);


        if (bundle!=null)
        {
            selectedImg = bundle.getString("clicked_image");
            Log.i(TAG, String.valueOf(selectedImg));
            Picasso.get().load(selectedImg).into(img, new Callback() {
                @Override
                public void onSuccess()
                {
                    mProgressDialog.setVisibility(View.GONE);
                    mPhotoViewAttacher.update();
                }

                @Override
                public void onError(Exception e)
                {

                }
            });
        }



        init();

    }

    private void init()
    {
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Favourites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        mMenu = menu;
        Query q = mRootRef.orderByChild("url").equalTo(selectedImg);
        q.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount() != 0)
                {
                    mMenu.findItem(R.id.menu_fav).setIcon(R.drawable.ic_favorite);
                    isFavourite = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.fullscreen_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
                case android.R.id.home:
                    onBackPressed();
                    return true;


            case R.id.menu_fav:

                if(!isFavourite)
                {
                    item.setIcon(R.drawable.ic_favorite);
                     isFavourite = true;
                    createFavourite();
                }
                else
                {
                    item.setIcon(R.drawable.ic_nonfavorite);
                    isFavourite = false;
                    createUnFavourite();
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

    private void createUnFavourite()
    {
        Query q = mRootRef.orderByChild("url").equalTo(selectedImg);
        q.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
                {
                    appleSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(FullScreenActivity.this, "favourite is cancalled", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createFavourite()
    {
        Map m = new HashMap<>();
        m.put("url", selectedImg);
        mRootRef.push().setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(FullScreenActivity.this, "favourite is Selected", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        super.onBackPressed();
        Intent i = new Intent(this, GalleryActivity.class);
        i.putExtra("catagory", catagory);
        startActivity(i);
        finish();

    }

    private void shareContent()
    {
        Bitmap bitmap =getBitmapFromView(img);
        try {
            File file = new File(this.getExternalCacheDir(),"logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           // intent.putExtra(Intent.EXTRA_TEXT, "Catagories to be send");
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

