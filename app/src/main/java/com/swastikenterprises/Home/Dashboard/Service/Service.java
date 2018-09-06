package com.swastikenterprises.Home.Dashboard.Service;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.swastikenterprises.R;

public class Service extends Fragment {

    private View mView;
    /*  private enduser_sheet enduser_sheet;
      private Contractor_sheet contractor_sheet;
      private suppliar_sheet suppliar_sheet;*/
    private BottomSheetDialogFragment bsdf;
    private Button bt_sbmit;

    private RadioGroup rg;

    public Service() {
        //Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_service, container, false);

        initViews();
        return mView;
    }

    private void initViews()
    {
        bt_sbmit = mView.findViewById(R.id.bt_sbmit);
        rg = mView.findViewById(R.id.rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.r1:
                        bsdf = new enduser_sheet();
                        break;

                    case R.id.r2:
                        bsdf = new Contractor_sheet();
                        break;

                    case R.id.r3:
                        bsdf = new suppliar_sheet();
                        break;

                    case R.id.r4:
                        bsdf = new housewives();
                        break;
                }
            }
        });


        bt_sbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bsdf != null) {
                    bsdf.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "1");
                } else {
                    Toast.makeText(getContext(), "Please Select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }


}
