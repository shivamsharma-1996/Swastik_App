package com.swastikenterprises.Home.Dashboard.Favourites;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swastikenterprises.Gallery.FullScreenActivity;
import com.swastikenterprises.Gallery.GalleryHolder;
import com.swastikenterprises.R;

public class Favourite extends Fragment
{
    private View offerView;
    private RecyclerView rec_offer;
    private DatabaseReference mRootRef;
    private ProgressBar mProgressDialog;
    private FirebaseRecyclerAdapter<favouriteModel, GalleryHolder> firebaseRecyclerAdapter;
    private boolean isFav = true;
    private TextView tv_nodata;

    public Favourite() 
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        offerView = inflater.inflate(R.layout.fragment_favourite, container, false);
        mProgressDialog = offerView.findViewById(R.id.progress);

        init();
        return offerView;
    }

    private void init()
    {
        rec_offer = offerView.findViewById(R.id.rec_offer);
        rec_offer.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rec_offer.setHasFixedSize(true);

        tv_nodata = offerView.findViewById(R.id.tv_nodata);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("Favourites").keepSynced(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        mRootRef.child("Favourites").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    tv_nodata.setVisibility(View.GONE);
                }
                else
                {
                    tv_nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<favouriteModel, GalleryHolder>(new FirebaseRecyclerOptions.Builder<favouriteModel>()
                .setQuery(mRootRef.child("Favourites").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), favouriteModel.class)
                .build())
        {
            @Override
            public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.favourite_single_item, parent, false);
                return new GalleryHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final GalleryHolder GalleryHolder, final int position, @NonNull final favouriteModel model)
            {
                GalleryHolder.setImage(model.getUrl());


                Log.i("pos", String.valueOf(position));
                GalleryHolder.galleryView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        GalleryHolder.iv_fav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                new  AlertDialog.Builder(getContext())
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                            }
                                        })
                                        .setMessage("DO you really want to remove it from your Favourite list?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                mProgressDialog.setVisibility(View.VISIBLE);

                                                Log.i("position", String.valueOf(position));

                                                firebaseRecyclerAdapter.getRef(position).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            firebaseRecyclerAdapter.notifyDataSetChanged();
                                                            mProgressDialog.setVisibility(View.GONE);
                                                            Toast.makeText(getContext(), "Succesfully removed from favourite list", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {

                                                        }
                                                    }
                                                });
                                                    }
                                                })
                                        .create().show();
                            }
                        });
                        GalleryHolder.galleryView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Intent fullScreenIntent = new Intent(getContext(), FullScreenActivity.class);
                                fullScreenIntent.putExtra("clicked_image",model.getUrl());
                                startActivity(fullScreenIntent);
                            }
                        });
                    }
                });
            }

            @Override
            public void onDataChanged()
            {
                super.onDataChanged();

                mProgressDialog.setVisibility(View.GONE);
            }

            @Override
            public void onError(DatabaseError e)
            {
                Log.i("onError", String.valueOf(e));
            }
        };
        firebaseRecyclerAdapter.startListening();
        rec_offer.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onStop()
    {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
