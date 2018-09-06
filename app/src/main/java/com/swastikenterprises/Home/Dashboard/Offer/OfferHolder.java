package com.swastikenterprises.Home.Dashboard.Offer;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.swastikenterprises.R;

public class OfferHolder extends RecyclerView.ViewHolder
    {
       public View v;
        public TextView t1,t2,t3;

        public OfferHolder(View itemView)
        {
            super(itemView);

            v= itemView;
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            t3 = itemView.findViewById(R.id.t3);
        }

        public void setT1(String s)
        {
            t1.setText(s);
        }
        public void setT2(String s)
        {
            t2.setText(s);
        }
    }
