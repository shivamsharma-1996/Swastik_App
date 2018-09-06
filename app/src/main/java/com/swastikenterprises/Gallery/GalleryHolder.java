package com.swastikenterprises.Gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.swastikenterprises.R;

public class GalleryHolder extends RecyclerView.ViewHolder
{
    public View galleryView;
    public ImageView image, iv_fav;
    private ProgressBar mProgress;



    public GalleryHolder(View iteadCardView)
    {
        super(iteadCardView);
        galleryView = iteadCardView;
        image = galleryView.findViewById(R.id.image);
        mProgress = galleryView.findViewById(R.id.galleryImgProgress);
        iv_fav = galleryView.findViewById(R.id.iv_fav);
    }

    public void removeFav()
    {

    }
    public void setImage(final String imgURL)
    {
        Picasso.get().load(imgURL).networkPolicy(NetworkPolicy.OFFLINE).
                into(image, new Callback() {
                    @Override
                    public void onSuccess()
                    {
                        mProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Picasso.get()
                                .load(imgURL).into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                mProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                });

    }

}
