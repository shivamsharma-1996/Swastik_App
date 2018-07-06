package com.swastikenterprises.Gallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;
import com.swastikenterprises.R;

import java.util.ArrayList;
import java.util.List;


public class GalleryActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView rec_gallery;
    private GalleryAdapter galleryAdapter;
    private String catagory;



    //firebase
    DatabaseReference mRootRef;
    List<String> imgList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null)
        {
            catagory = bundle.getString("catagory");
        }

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(catagory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();



    }


    private void init()
    {
        mRootRef = FirebaseDatabase.getInstance().getReference();

        rec_gallery = findViewById(R.id.rec_gallery);
        rec_gallery.setLayoutManager(new GridLayoutManager(this, 2));
        rec_gallery.setHasFixedSize(true);


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        if(catagory.equals("Customized Wallpaers"))
        {
            mRootRef.child("Custom_Wallpaper").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Log.i("dataSnapshot", String.valueOf((dataSnapshot.getValue(GalleryGridModel.class)).getImg()));
                    String url = (dataSnapshot.getValue(GalleryGridModel.class)).getImg();
                    imgList.add((url));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            galleryAdapter = new GalleryAdapter( this, imgList);

            rec_gallery.setAdapter(galleryAdapter);
        }
        else
        {


            mRootRef.child(catagory).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String url = (dataSnapshot.getValue(GalleryGridModel.class)).getImg();
                    imgList.add((url));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }


    }

    /*private List<GalleryGridModel> getPopulateList()
    {
        List list = new ArrayList();
        list.add(new GalleryGridModel(R.drawable.wallpaper1, catagory + 1, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper2, catagory + 2, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper3, catagory + 3, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper4, catagory + 4, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper5, catagory + 5, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper6, catagory + 6, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper7, catagory + 7, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper8, catagory + 8, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper9, catagory + 9, catagory));
        list.add(new GalleryGridModel(R.drawable.wallpaper10, catagory + 10, catagory));
        return list;
    }*/
}
