package com.swastikenterprises.Dashboard;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swastikenterprises.R;

public class SliderPagerAdapter extends PagerAdapter {

    private int[] imgs = new int[]{R.drawable.slide8, R.drawable.slide4,R.drawable.slide9, R.drawable.slide5, R.drawable.slide6, R.drawable.slide7,  R.drawable.slide3};
    private Context ctx;

    public SliderPagerAdapter(Context context)
    {
        ctx = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position)
    {
        View view   = LayoutInflater.from(ctx).inflate(R.layout.slide_layout1, collection, false);
        ImageView img = view.findViewById(R.id.img);
        img.setImageResource(imgs[position]);
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view)
    {
        collection.removeView((View) view);
    }

    @Override
    public int getCount()
    {
        return 7;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return "";
    }
}