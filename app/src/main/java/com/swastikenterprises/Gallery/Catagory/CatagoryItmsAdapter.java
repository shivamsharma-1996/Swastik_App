package com.swastikenterprises.Gallery.Catagory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swastikenterprises.Gallery.GalleryActivity;
import com.swastikenterprises.R;

import java.util.ArrayList;
import java.util.List;

public class CatagoryItmsAdapter extends RecyclerView.Adapter<CatagoryItmsAdapter.holder>
{
    private List<String> titleList;
    private Context context;

    public CatagoryItmsAdapter(List<String> titleList, Context context)
    {
        this.titleList = titleList;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view  = LayoutInflater.from(context).inflate(R.layout.catagory_list_grp, null);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position)
    {
        holder.tv_title.setText(titleList.get(position));

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public static class holder extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        public holder(View itemView)
        {
            super(itemView);
            tv_title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent galleryIntent = new Intent(tv_title.getContext(), GalleryActivity.class);
                    galleryIntent.putExtra("catagory", tv_title.getText().toString());
                 //   ((Activity)tv_title.getContext()).finish();
                    tv_title.getContext().startActivity(galleryIntent);
                }
            });
        }

    }
}
