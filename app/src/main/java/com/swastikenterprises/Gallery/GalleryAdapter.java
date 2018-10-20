package com.swastikenterprises.Gallery;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.swastikenterprises.R;

import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.holder>
{
    private Context context;
    List<String> list;

    public GalleryAdapter(Context context, List<String> list)
    {
        this.context = context;
        this.list = list;
        Log.i("listadaptre", String.valueOf(this.list));
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_layout,null);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, final int position)
    {

        Picasso.get()
                .load(list.get(position))
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess()
                    {
                        holder.mProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Picasso.get()
                                .load(list.get(position))
                                .into(holder.image, new Callback() {
                                    @Override
                                    public void onSuccess()
                                    {
                                        holder.mProgress.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    }
                });

        holder.image.setVisibility(View.VISIBLE);


        //holder. `Logo.setImageResource(list.get(position).getTitle());
        //holder.tvTitle.setText(list.get(position).getTvTitle());

        holder.image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(view.getContext(), getAdapterPosition()+1, Toast.LENGTH_SHORT).show();
                Intent fullScreenIntent = new Intent( holder.image.getContext(), FullScreenActivity.class);
                Log.i("bp", String.valueOf(list.get(position)));
                /*fullScreenIntent.putExtra("clicked_image",list.get(position));
                holder.image.getContext().startActivity(fullScreenIntent);*/
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


    public static class holder extends RecyclerView.ViewHolder
    {
        private View view;
        private ImageView image;
        private TextView title;
        private TextView type;
        private ProgressBar mProgress;


        public holder(View itemView)
        {
            super(itemView);
            view = itemView;
            image = view.findViewById(R.id.image);
            mProgress = view.findViewById(R.id.progress);
           //title = view.findViewById(R.id.title);
//            type = view.findViewById(R.id.type);


        }
    }
}
