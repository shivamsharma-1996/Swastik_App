package com.swastikenterprises.Gallery.Catagories;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swastikenterprises.R;

import java.util.ArrayList;
import java.util.List;

public class Interior extends Fragment
{
    private View interior_view;
    private RecyclerView rec;

    public Interior()
    {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        interior_view = inflater.inflate(R.layout.gallery_fragment_interior, container, false);
        initData();
        return  interior_view;
    }

    private void initData()
    {
        rec = interior_view.findViewById(R.id.rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rec.setHasFixedSize(true);
        rec.setAdapter(new CatagoryItmsAdapter(getTitleList(), getContext()));
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    public List<String> getTitleList()
    {
        List titleList = new ArrayList();

        titleList.add("Customized Wallpapers");
        titleList.add("Wallpapers Asian Paints(nilaya) \n No. of Brands");
        titleList.add("Customized WardrobSkin");
        titleList.add("Customized Glassfilm");
        titleList.add("Canvas Paintings");
        titleList.add("Customized Wall Stickers");
        titleList.add("Customized Curtains & Blinds");
        titleList.add("Customized Posters");
        titleList.add("Supreme Self Adhesive 3D Form");
        titleList.add("Glassfilm Decorative");
        titleList.add("Decorative Furniture");
        titleList.add("Wooden Flooring");
        titleList.add("Interlock Flooring \n (Gym & Sports)");
        titleList.add("Vinyl Flooring");
        titleList.add("Carpet Flooring");
        titleList.add("Pinewood Panels");
        titleList.add("Stretch Fabric Ceilings");
        return titleList;
    }
}



