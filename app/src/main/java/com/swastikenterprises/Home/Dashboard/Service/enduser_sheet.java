package com.swastikenterprises.Home.Dashboard.Service;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.swastikenterprises.R;
import com.swastikenterprises.helper.Validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class enduser_sheet extends BottomSheetDialogFragment
{
    private View sheetView;
    private Button btnSubmit;
    private ImageView iv_close;
    private ProgressBar progress;

    static String lookingFor = "", workType = "", requirement = "", name ="", email="", contact="", city="", area="", landmark="", address="", remark="";
    private Spinner s2,s3;
    private EditText e4,e5,e6,e7,e8,e9,e10,e11;
    private DatabaseReference mRootRef;
    private TextView tv_lookingFor;

    public enduser_sheet()
    {

    }


 
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        sheetView =  inflater.inflate(R.layout.fragment_enduser, container, false);
        initViews();
        return sheetView;
    }

    private void initViews()
    {
        e4 = sheetView.findViewById(R.id.e4);
        e5 = sheetView.findViewById(R.id.e5);
        e6 = sheetView.findViewById(R.id.e6);
        e7 = sheetView.findViewById(R.id.e7);
        e8 = sheetView.findViewById(R.id.e8);
        e9 = sheetView.findViewById(R.id.e9);
        e10 = sheetView.findViewById(R.id.e10);
        e11 = sheetView.findViewById(R.id.e11);
        //You can exit the loop as you find a reference

        mRootRef = FirebaseDatabase.getInstance().getReference().child("JOIN_US").child("End_User");
        progress = sheetView.findViewById(R.id.progress);

        tv_lookingFor =  sheetView.findViewById(R.id.tv_lookingFor);
        s2 =  sheetView.findViewById(R.id.spinner2);
        s3 =  sheetView.findViewById(R.id.spinner3);

        btnSubmit = sheetView.findViewById(R.id.btnSubmit);
        iv_close = sheetView.findViewById(R.id.iv_close);


        final String[] select_qualification = getResources().getStringArray(R.array.end_user1);



        tv_lookingFor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final List<String> selectedItems = new ArrayList<>();

                final CharSequence[] dialogList = Arrays.asList(select_qualification).toArray(new CharSequence[Arrays.asList(select_qualification).size()]);
                final android.app.AlertDialog.Builder builderDialog = new android.app.AlertDialog.Builder(getContext());
                builderDialog.setTitle("Select Item");
                int count = dialogList.length;
                boolean[] is_checked = new boolean[count];

                builderDialog.setMultiChoiceItems(dialogList, is_checked, new DialogInterface.OnMultiChoiceClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton, boolean isChecked)
                            {
                                if (isChecked)
                                {
                                    selectedItems.add(String.valueOf(whichButton));
                                    Log.i("add", selectedItems.toString());
                                }
                                else
                                {
                                    for(int i = 0; i < selectedItems.size(); i++)
                                    {
                                        if(selectedItems.get(i).equals(String.valueOf(whichButton)))
                                        {
                                            selectedItems.remove(i);
                                            Log.i("rem", selectedItems.toString());
                                        }
                                    }
                                }
                            }
                        });

                builderDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                List lookingForList = new ArrayList();
                                for(int i = 0 ; i<selectedItems.size();i++)
                                {
                                    lookingForList.add(select_qualification[Integer.parseInt(selectedItems.get(i))]);
                                }
                                tv_lookingFor.setText(lookingForList.toString().replace("[", "").replace("]", ""));
                            }
                        });

                builderDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                tv_lookingFor.setText("");
                            }
                        });
                android.app.AlertDialog alert = builderDialog.create();
                alert.show();
            }
        });

        String[] s2data= getResources().getStringArray(R.array.end_user2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, s2data);
        adapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        s2.setPrompt("Select Work Type");
        s2.setAdapter(adapter2);


        String[] s3data= getResources().getStringArray(R.array.end_user3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, s3data);
        adapter3.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        s3.setPrompt("Select Requiremnet");
        s3.setAdapter(adapter3);

        tv_lookingFor.setText(lookingFor);
        s2.setSelection ( Arrays.asList(s2data).indexOf(workType) );
        s3.setSelection ( Arrays.asList(s3data).indexOf(requirement) );
        e4.setText(name);
        e5.setText(email);
        e6.setText(contact);
        e7.setText(city);
        e8.setText(area);
        e9.setText(landmark);
        e10.setText(address);
        e11.setText(remark);
    }

    @Override
    public void onStart()
    {
        super.onStart();


        //for round corners of sheet
        getDialog().getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(R.drawable.sheet_title_bg1);



        iv_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
            dismiss();
            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Spinner spinner = (Spinner) parent;
                workType = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Spinner spinner = (Spinner) parent;
                requirement = spinner.getSelectedItem().toString();
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
                lookingFor = tv_lookingFor.getText().toString();
                name = e4.getText().toString();
                email = e5.getText().toString();
                contact = e6.getText().toString();
                city = e7.getText().toString();
                area = e8.getText().toString();
                landmark = e9.getText().toString();
                address = e10.getText().toString();
                remark = e11.getText().toString();

                if(Validation.emptyValidate(lookingFor) || Validation.emptyValidate(workType) || Validation.emptyValidate(requirement) ||
                Validation.emptyValidate(name) || Validation.emptyValidate(email) || Validation.emptyValidate(contact) ||
                Validation.emptyValidate(city) || Validation.emptyValidate(area) || Validation.emptyValidate(landmark)
                 ||  Validation.emptyValidate(address))
                {
                    Toast.makeText(getContext(), "All required fields should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    //subscription cancelled here
                    progress.setVisibility(View.VISIBLE);

                    Object timestamp = ServerValue.TIMESTAMP;
                    Map map = new HashMap();
                    map.put("investment", workType);
                    map.put("requirement", requirement);
                    map.put("name", name);
                    map.put("email", email);
                    map.put("contact", contact);
                    map.put("city", city);
                    map.put("area", area);
                    map.put("landmark", landmark);
                    map.put("address", address);
                    map.put("remark", remark);
                    map.put("timestamp", timestamp);
                    map.put ("lookingFor",  tv_lookingFor.getText());


                   ;
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