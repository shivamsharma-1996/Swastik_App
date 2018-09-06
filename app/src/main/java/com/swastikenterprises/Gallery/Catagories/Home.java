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

public class Home extends Fragment
{
    private View home_view;
    private RecyclerView rec;
    public Home(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        home_view = inflater.inflate(R.layout.gallery_fragment_home, container, false);
        initData();
        return  home_view;
    }

    private void initData() {
        rec = home_view.findViewById(R.id.rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rec.setHasFixedSize(true);
        rec.setAdapter(new CatagoryItmsAdapter(getTitleList(), getContext()));
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    public List<String> getTitleList() {
        List titleList = new ArrayList();
        titleList.add("Yoga matts & Yoga Brick");
        titleList.add("Bedsheets");
        titleList.add("Exterior Curtains & Blinds");
        titleList.add("Bird Feeder");
        titleList.add("Bird Home");
        titleList.add("Bird Net");
        titleList.add("Bird Spikes");
        titleList.add("Bird Cage");
        titleList.add("Artificial Grass & Plants");
        titleList.add("Door Matts");
        titleList.add("Automatic Air Dispensers");
        titleList.add("Shade Sail & Green Shade Net");
        titleList.add("Acrylic Shelf & Mobile Stands");
        titleList.add("Terracotta Items");
        return titleList;
    }

}