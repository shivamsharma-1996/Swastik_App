package com.swastikenterprises.Gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swastikenterprises.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GalleryActivity extends AppCompatActivity
{
    private Toolbar mToolbar;

    private RecyclerView rec_gallery;
    private GalleryAdapter galleryAdapter;
    private String catagory;
    TextView tvTitle;

     CollapsingToolbarLayout collapsingToolbar;

    //firebase
    DatabaseReference mRootRef, mRootRef1;
    List<String> imgList = new ArrayList<>();
    private ProgressBar progress1, progress2;

    private FirebaseRecyclerAdapter<GalleryGridModel, GalleryHolder> firebaseRecyclerAdapter;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();

        if(bundle!= null)
        {
            catagory = bundle.getString("catagory");
            Log.i("catagory", catagory);
        }
        setContentView(R.layout.activity_gallery);
        tvTitle = findViewById(R.id.love_music);
        progress1 =  findViewById(R.id.progress1);
        progress2 =  findViewById(R.id.progress2);


        if(catagory.equals("Wallpapers Asian Paints(nilaya) \n No. of Brands"))
        {
            mRootRef = FirebaseDatabase.getInstance().getReference().child("Wallpapers Asian Paints");
            mRootRef1 = FirebaseDatabase.getInstance().getReference().child("INTRO").child("Wallpapers Asian Paints");
        }
        else if(catagory.equals("Customized Wallpapers"))
        {
            mRootRef = FirebaseDatabase.getInstance().getReference().child("Customized Wallpaers");
            mRootRef1 = FirebaseDatabase.getInstance().getReference().child("INTRO").child("Customized Wallpapers");
        }
        else if(catagory.equals("Stone Mosaic, Murals & Pebbles"))
        {
            mRootRef = FirebaseDatabase.getInstance().getReference().child("Stone Mosaic & Murals");
            mRootRef1 = FirebaseDatabase.getInstance().getReference().child("INTRO").child("Stone Mosaic & Murals");
        }
        else if(catagory.equals("Exterior Curtains & Blinds"))
        {
            mRootRef = FirebaseDatabase.getInstance().getReference().child("Exterior Curtians & Blinds");
            mRootRef1= FirebaseDatabase.getInstance().getReference().child("INTRO").child("Exterior Curtians & Blinds");
        }
        else if(catagory.equals("Interlock Flooring \n (Gym & Sports)"))
        {
            mRootRef = FirebaseDatabase.getInstance().getReference().child("Interlock Flooring");
            mRootRef1 = FirebaseDatabase.getInstance().getReference().child("INTRO").child("Interlock Flooring");
        }
        else if(catagory.equals("Shade Sail & Green Shade Net"))
        {
            mRootRef = FirebaseDatabase.getInstance().getReference().child("Shades Sail");
            mRootRef1 = FirebaseDatabase.getInstance().getReference().child("INTRO").child("Shades Sail");
        }
        else
        {
            mRootRef = FirebaseDatabase.getInstance().getReference().child(catagory);
            mRootRef1 =  FirebaseDatabase.getInstance().getReference().child("INTRO").child(catagory);
        }

        mRootRef.keepSynced(true);
        mRootRef1.keepSynced(true);


        mRootRef1.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                progress1.setVisibility(View.INVISIBLE);
                //Log.i("fvfb", String.valueOf(dataSnapshot));
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                title = map.get("title").toString();
                tvTitle.setText(title);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle(catagory);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setIcon(R.drawable.ic_close);

        init();

        initCollapsingToolbar();
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar()
    {
       collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if (scrollRange == -1)
                {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0)
                {
                    collapsingToolbar.setTitle(catagory);
                    isShow = true;
                }
                else if (isShow)
                {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    private void init()
    {
        rec_gallery = findViewById(R.id.rec_gallery);
        rec_gallery.setLayoutManager(new GridLayoutManager(this, 2));
        rec_gallery.setHasFixedSize(true);

        mRootRef.child("Custom_Wallpaper").keepSynced(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (getFragmentManager().getBackStackEntryCount() != 0) {
                getFragmentManager().popBackStack();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart()
    {
        super.onStart();

    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GalleryGridModel, GalleryHolder>(new FirebaseRecyclerOptions.Builder<GalleryGridModel>()
            .setQuery(mRootRef, GalleryGridModel.class)
            .build())
    {
        @Override
        public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(GalleryActivity.this).inflate(R.layout.gallery_item_layout, parent, false);
            return new GalleryHolder(view);
        }
        @Override
        protected void onBindViewHolder(@NonNull final GalleryHolder galleryHolder, int position, @NonNull final GalleryGridModel galleryGridModel)
        {
            galleryHolder.setImage(galleryGridModel.getImg());


            galleryHolder.galleryView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent fullScreenIntent = new Intent(GalleryActivity.this, FullScreenActivity.class);
                    fullScreenIntent.putExtra("catagory",catagory);
                    fullScreenIntent.putExtra("clicked_image",galleryGridModel.getImg());
                    startActivity(fullScreenIntent);
                    finish();
                }
            });
        }

        @Override
        public void onDataChanged()
        {
            super.onDataChanged();

            if(progress2 != null)
            {
                progress2.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(DatabaseError e)
        {
            Log.i("onError", String.valueOf(e));
        }
    };
        firebaseRecyclerAdapter.startListening();
        rec_gallery.setAdapter(firebaseRecyclerAdapter);
}


    @Override
    public void onStop()
    {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();

    }


    @Override
    public void onBackPressed()
    {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0)
        {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        }
        else
            {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}