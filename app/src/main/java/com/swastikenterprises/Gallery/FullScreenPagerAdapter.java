package com.swastikenterprises.Gallery;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.swastikenterprises.R;

import java.util.List;


class FullScreenPagerAdapter extends PagerAdapter {

    private final Activity activity;
    private List<String> imgList;
    private ProgressBar progressBar;
    PhotoViewAttacher mPhotoViewAttacher;

    FullScreenPagerAdapter(Activity ctx, List<String> imgList) {
        this.activity = ctx;
        this.imgList = imgList;
    }

    @Override
    public int getCount() {
        return imgList != null ? imgList.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final View convertView = LayoutInflater.from(activity).inflate(R.layout.full_screen_single_layout, container, false);
        ImageView img = convertView.findViewById(R.id.img);
        mPhotoViewAttacher = new PhotoViewAttacher(img);
        progressBar = convertView.findViewById(R.id.loading_layout);
        Picasso.get().load(imgList.get(position)).into(img, new Callback() {
            @Override
            public void onSuccess() {
                mPhotoViewAttacher.update();
               // progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
        convertView.setTag(position);

        container.addView(convertView);
        return convertView;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}