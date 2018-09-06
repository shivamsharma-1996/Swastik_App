package com.swastikenterprises.Home.Dashboard.Offer;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.swastikenterprises.R;

import java.util.HashMap;
import java.util.Map;


public class Offers extends Fragment
{
    private View offerView;
    private RecyclerView rec_offer;
    private DatabaseReference mRootRef;
    private ProgressBar mProgressDialog;
    private FirebaseRecyclerAdapter<OfferModel, OfferHolder> firebaseRecyclerAdapter;
    private TextView tv_nodata;

    public Offers()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        offerView = inflater.inflate(R.layout.fragment_offers, container, false);
        mProgressDialog = offerView.findViewById(R.id.progress);

        init();
        return offerView;
    }

    private void init()
    {
        tv_nodata = offerView.findViewById(R.id.tv_nodata);


        rec_offer = offerView.findViewById(R.id.rec_offer);
        rec_offer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rec_offer.setHasFixedSize(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("OFFERS").keepSynced(true);
    }


    @Override
    public void onStart()
    {
        super.onStart();

        mRootRef.child("OFFERS").addValueEventListener(new ValueEventListener() {
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

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OfferModel, OfferHolder>(new FirebaseRecyclerOptions.Builder<OfferModel>()
                .setQuery(mRootRef.child("OFFERS"), OfferModel.class)
                .build())
        {
            @Override
            public OfferHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.offer_single_layout, parent, false);
                return new OfferHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final OfferHolder OfferHolder, int position, @NonNull final OfferModel offerModel)
            {
                final String offerKey = getRef(position).getKey();
                OfferHolder.setT1(offerModel.getTitle());
                OfferHolder.setT2(offerModel.getBody());



                OfferHolder.t3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                      /*Intent fullScreenIntent = new Intent(getContext(), FullScreenActivity.class);
                        fullScreenIntent.putExtra("clicked_image",OfferModel.getImg());
                        startActivity(fullScreenIntent);*/

                        new  AlertDialog.Builder(getContext())
                              .setTitle(offerModel.getTitle())
                                .setNegativeButton("IGNORE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setMessage("Click on Submit to show interest in this offer!")
                                .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        mProgressDialog.setVisibility(View.VISIBLE);
                                        Map m = new HashMap();
                                        m.put("Timestamp", ServerValue.TIMESTAMP);
                                        mRootRef.child("OFFERS_INTERESTED_USERS").child(offerKey).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                Toast.makeText(getContext(), "Thanks, Our Representative Will Contact You Soon", Toast.LENGTH_SHORT).show();
                                                mProgressDialog.setVisibility(View.GONE);

                                                }
                                        });
                                    }
                                })
                              .create().show();



                        //Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
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
