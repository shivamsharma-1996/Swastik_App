package com.swastikenterprises.Home.Dashboard.Service;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.swastikenterprises.R;
import com.swastikenterprises.helper.Validation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Contractor_sheet extends BottomSheetDialogFragment
{
    private View sheetView;
    private Button btnSubmit;
    private EditText e1, e2, e3, e4, e5;
    private ImageView iv_close;
    private ProgressBar progress;
    private DatabaseReference mRootRef;


   static String lookingFor = "", email = "", contact = "", state = "", city = "", remark = "";
    private Spinner s1;
    public Contractor_sheet()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        sheetView =  inflater.inflate(R.layout.fragment_contractor_sheet, container, false);
        initViews();
        return sheetView;
    }

    private void initViews()
    {

        mRootRef = FirebaseDatabase.getInstance().getReference().child("JOIN_US").child("Contractor");
        progress = sheetView.findViewById(R.id.progress);

        e1 = sheetView.findViewById(R.id.e1);
        e2 = sheetView.findViewById(R.id.e2);
        e3 = sheetView.findViewById(R.id.e3);
        e4 = sheetView.findViewById(R.id.e4);
        e5 = sheetView.findViewById(R.id.e5);

        s1 =  sheetView.findViewById(R.id.spinner1);

        btnSubmit = sheetView.findViewById(R.id.btnSubmit);
        iv_close = sheetView.findViewById(R.id.iv_close);


        String[] s1data= getResources().getStringArray(R.array.contractor1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_item, s1data);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        s1.setPrompt("Looking For");
        s1.setAdapter(adapter1);

        s1.setSelection ( Arrays.asList(s1data).indexOf(lookingFor) );
        e1.setText(email);
        e2.setText(contact);
        e3.setText(state);
        e4.setText(city);
        e5.setText(remark);
    }


    @Override
    public void onStart()
    {
        super.onStart();

        getDialog().getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(R.drawable.sheet_title_bg1);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Spinner spinner = (Spinner) parent;
                lookingFor = spinner.getSelectedItem().toString();
                //Toast.makeText(getContext(), ""  + lookingFor, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                email = e1.getText().toString();
                contact = e2.getText().toString();
                state = e3.getText().toString();
                city = e4.getText().toString();
                remark = e5.getText().toString();

                if(Validation.emptyValidate(lookingFor) || Validation.emptyValidate(email) || Validation.emptyValidate(contact) || Validation.emptyValidate(state) || Validation.emptyValidate(city))
                {
                    Toast.makeText(getContext(), "All fields are required to be filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    //subscription cancelled here
                    progress.setVisibility(View.VISIBLE);

                    Object timestamp = ServerValue.TIMESTAMP;
                    Map map = new HashMap();
                    map.put("lookingFor", lookingFor);
                    map.put("email", email);
                    map.put("contact", contact);
                    map.put("state", state);
                    map.put("city", city);
                    map.put("remark", remark);
                    map.put("timestamp", timestamp);

                    mRootRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Successfully Submitted", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "There is a problem, try after a while!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }


}
