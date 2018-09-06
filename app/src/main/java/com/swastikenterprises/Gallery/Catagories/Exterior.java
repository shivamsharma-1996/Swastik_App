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

public class Exterior extends Fragment
{

    private View exterior_view;
    private RecyclerView rec;
    public Exterior()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        exterior_view = inflater.inflate(R.layout.gallery_fragment_interior, container, false);
        initData();
        return  exterior_view;
    }

    private void initData()
    {
        rec = exterior_view.findViewById(R.id.rec);
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
        titleList.add("Exterior Claddings");
        titleList.add("Stone Mosaic, Murals & Pebbles");  //Stone Mosaic, Murals & Pebbles
        titleList.add("Artificial Stones");
        titleList.add("Steel Gates & Railings");
        titleList.add("Toughened Glass Fabricate");
        titleList.add("FRP & ferrocement Sculpture");
        titleList.add("Wooden Pergolas & Gazebo");
        return titleList;
    }


}