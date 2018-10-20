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

public class Furniture extends Fragment
{
    private View interior_view;
    private RecyclerView rec;

    public Furniture()
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
        titleList.add("Home furniture \n Indoor-Outdoor both");
        titleList.add("Office furniture");
        titleList.add("School & College Furniture");
        titleList.add("Hotel & Restaurant Furniture");
        titleList.add("Bank or Corporate Furniture");
        titleList.add("Healthcare Furniture");
        return titleList;
    }
}



