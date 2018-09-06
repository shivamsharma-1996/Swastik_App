package com.swastikenterprises.Home.Dashboard.Service;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class housewives extends BottomSheetDialogFragment {
    private View sheetView;
    private Button btnSubmit;
    private ImageView iv_close;
    private ProgressBar progress;

    static String lookingFor = "", investment = "", name = "", email = "", contact = "", state = "", city = "", address = "", area = "", remark = "";
    private Spinner s1,s2, s3;
    private EditText e1, e2, e3, e4, e5, e6;
    private DatabaseReference mRootRef;
    private TextView tv_lookingFor;
    private CheckBox cbTermsConditions;
    public housewives() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sheetView = inflater.inflate(R.layout.fragment_housewives_sheet, container, false);
        initViews();
        return sheetView;
    }

    private void initViews() {
        cbTermsConditions = sheetView.findViewById(R.id.cb_terms);
        e1 = sheetView.findViewById(R.id.e1);
        e2 = sheetView.findViewById(R.id.e2);
        e3 = sheetView.findViewById(R.id.e3);
        e4 = sheetView.findViewById(R.id.e4);
        e5 = sheetView.findViewById(R.id.e5);
        e6 = sheetView.findViewById(R.id.e6);
        s1 = sheetView.findViewById(R.id.spinner1);
        s2 = sheetView.findViewById(R.id.spinner2);
        s3 = sheetView.findViewById(R.id.spinner3);

        //You can exit the loop as you find a reference

        mRootRef = FirebaseDatabase.getInstance().getReference().child("JOIN_US").child("Freelancer_Channel_Partners");
        progress = sheetView.findViewById(R.id.progress);

        tv_lookingFor = sheetView.findViewById(R.id.tv_lookingFor);
        s1 = sheetView.findViewById(R.id.spinner1);
        s2 = sheetView.findViewById(R.id.spinner2);
        s3 = sheetView.findViewById(R.id.spinner3);

        btnSubmit = sheetView.findViewById(R.id.btnSubmit);
        iv_close = sheetView.findViewById(R.id.iv_close);


        final String[] select_qualification = getResources().getStringArray(R.array.housewives1);

        tv_lookingFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> selectedItems = new ArrayList<>();

                final CharSequence[] dialogList = Arrays.asList(select_qualification).toArray(new CharSequence[Arrays.asList(select_qualification).size()]);
                final android.app.AlertDialog.Builder builderDialog = new android.app.AlertDialog.Builder(getContext());
                builderDialog.setTitle("Select Items");
                int count = dialogList.length;
                boolean[] is_checked = new boolean[count];

                builderDialog.setMultiChoiceItems(dialogList, is_checked, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(String.valueOf(whichButton));
                            Log.i("add", selectedItems.toString());
                        } else {
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i).equals(String.valueOf(whichButton))) {
                                    selectedItems.remove(i);
                                }
                            }
                        }
                    }
                });

                builderDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List lookingForList = new ArrayList();
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    lookingForList.add(select_qualification[Integer.parseInt(selectedItems.get(i))]);
                                }
                                tv_lookingFor.setText(lookingForList.toString().replace("[", "").replace("]", ""));
                            }
                        });

                builderDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_lookingFor.setText("");
                            }
                        });
                android.app.AlertDialog alert = builderDialog.create();
                alert.show();
            }
        });

        String[] s1data = getResources().getStringArray(R.array.housewives2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, s1data);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        s1.setPrompt("Choose Investment");
        s1.setAdapter(adapter1);

        String[] s2data = getResources().getStringArray(R.array.housewives3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, s2data);
        adapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        s2.setPrompt("Choose State");
        s2.setAdapter(adapter2);

        String[] s3data = getResources().getStringArray(R.array.housewives4);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, s3data);
        adapter3.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        s3.setPrompt("Choose City");
        s3.setAdapter(adapter3);



        tv_lookingFor.setText(lookingFor);
        s1.setSelection(Arrays.asList(s1data).indexOf(investment));
        s2.setSelection(Arrays.asList(s1data).indexOf(state));
        s3.setSelection(Arrays.asList(s1data).indexOf(city));
        e1.setText(name);
        e2.setText(email);
        e3.setText(contact);
        e4.setText(area);
        e5.setText(address);
        e6.setText(remark);
    }

    @Override
    public void onStart() {
        super.onStart();


        //for round corners of sheet
        getDialog().getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(R.drawable.sheet_title_bg1);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                investment = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                state = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                city = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cbTermsConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    showTermsAlertDialog();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookingFor = tv_lookingFor.getText().toString();
                name = e1.getText().toString();
                email = e2.getText().toString().trim();
                contact = e3.getText().toString().trim();
                area = e4.getText().toString().trim();
                address = e5.getText().toString().trim();
                remark = e6.getText().toString();


                if (Validation.emptyValidate(lookingFor) || Validation.emptyValidate(investment) ||
                        Validation.emptyValidate(name) || Validation.emptyValidate(contact) ||
                        Validation.emptyValidate(state) || Validation.emptyValidate(area) || Validation.emptyValidate(address)|| Validation.emptyValidate(city) ) {
                    Toast.makeText(getContext(), "All required fields should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!cbTermsConditions.isChecked())
                {
                    Toast.makeText(getContext(), "before submitting, you must agree Terms & Conditions!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    progress.setVisibility(View.VISIBLE);

                    Object timestamp = ServerValue.TIMESTAMP;
                    Map map = new HashMap();
                    map.put("lookingFor", tv_lookingFor.getText());
                    map.put("investment", investment);
                    map.put("name", name);
                    map.put("email", email);
                    map.put("contact", contact);
                    map.put("state", state);
                    map.put("area", area);
                    map.put("address", address);
                    map.put("city", city);
                    map.put("remark", remark);
                    map.put("timestamp", timestamp);

                    mRootRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Successfully Submitted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "There is a problem, try after a while!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }

    private void showTermsAlertDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.terms_and_condition_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        Button agreeBtn = dialogView.findViewById(R.id.agreeBtn);
        Button denyBtn = dialogView.findViewById(R.id.denyBtn);

        final AlertDialog alertDialog = dialogBuilder.create();

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                cbTermsConditions.setChecked(false);
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

}