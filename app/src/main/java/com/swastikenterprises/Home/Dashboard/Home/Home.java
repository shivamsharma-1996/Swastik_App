package com.swastikenterprises.Home.Dashboard.Home;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swastikenterprises.Dashboard.SliderPagerAdapter;
import com.swastikenterprises.R;
import com.swastikenterprises.helper.Validation;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;


public class Home extends Fragment
{
    private View homeView;
    private SliderPagerAdapter sliderPagerAdapter;


    private TextView sendMail;
    private LinearLayout ll_wa, ll_call;
    private TextView tv_opp;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressBar progress;
    private TextView iv_address;


    //pdf
    final static int PICK_PDF_CODE = 2342;

    //dialog
    EditText etEmail, etProfile;
    TextView tv_upload, tv_ref_id, title, subtitle;
    ImageView iv_upload;
    LinearLayout ll_input, ll_upload;
    TextView bt_cancel, bt_submit;
    Button btDone;

    private Uri pdfFileUri = null;

    private String email = "", profile = "";

    //firebase
    private DatabaseReference mCVRef;

    public Home()
    {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        homeView =  inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return homeView;
    }

    private void init()
    {

        mCVRef = FirebaseDatabase.getInstance().getReference().child("Opportunity");

        builder = new AlertDialog.Builder(getContext());
        tv_opp = homeView.findViewById(R.id.tv_opp);

        iv_address = homeView.findViewById(R.id.iv_address);
        ll_wa = homeView.findViewById(R.id.ll_wa);

        ll_call =  homeView.findViewById(R.id.ll_call);
        sendMail =  homeView.findViewById(R.id.send_mail);


        final ViewPager viewPager = homeView.findViewById(R.id.viewpager);
        sliderPagerAdapter = new SliderPagerAdapter(getContext());
        viewPager.setAdapter(sliderPagerAdapter);

        TabLayout tabLayout = homeView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){

                    @Override
                    public void run()
                    {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%7);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 2500, 2500);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9829654259"));
                startActivity(intent);
            }
        });


        ll_wa.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openWhatsApp();
        }
    });
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "swastiktrading94@gmail.com");
                startActivity(Intent.createChooser(intent, "Send Email"));
                }
        });

        iv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /*Intent mapIntent, chooser;
                mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(Uri.parse("geo:19.076, 72.8777"));
                chooser = Intent.createChooser(mapIntent, "Launch Maps");
                startActivity(chooser);*/

                String url = "https://www.google.com/maps/dir/?api=1&destination=" + "26.2217775" + "," + "73.0050492" + "&travelmode=driving";
                //https://www.google.com/maps/search/?api=1&query=58.698017,-152.522067
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(mapIntent);

            }
        });



        tv_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDialog();
            }
        });
    }

    private void showDialog()
    {
        builder = new AlertDialog.Builder(getContext());
        View dialogView = this.getLayoutInflater().inflate(R.layout.opportunity_dialog, null);

        tv_ref_id = dialogView.findViewById(R.id.tv_ref_id);
        title = dialogView.findViewById(R.id.title);
        subtitle = dialogView.findViewById(R.id.subtitle);
        ll_input = dialogView.findViewById(R.id.ll_input);
        ll_upload = dialogView.findViewById(R.id.ll_upload);
        etEmail = dialogView.findViewById(R.id.etEmail);
        etProfile = dialogView.findViewById(R.id.etProfile);
        tv_upload = dialogView.findViewById(R.id.tv_upload);
        iv_upload = dialogView.findViewById(R.id.iv_upload);
        progress = dialogView.findViewById(R.id.progress);
        bt_cancel = dialogView.findViewById(R.id.btCancel);
        bt_submit= dialogView.findViewById(R.id.btSubmit);
        btDone = dialogView.findViewById(R.id.btDone);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.setCanceledOnTouchOutside(false);

        ll_upload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                choosePDF();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email =   etEmail.getText().toString().trim();
               profile =  etProfile.getText().toString().trim();

               if(Validation.emptyValidate(email))
               {
                   Toast.makeText(getContext(), "Email Required", Toast.LENGTH_SHORT).show();
                   return;
               }
               else if(Validation.emptyValidate(profile))
                {
                    Toast.makeText(getContext(), "Current Profile Name Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(pdfFileUri == null)
               {
                   Toast.makeText(getContext(), "Please Upload your Resume", Toast.LENGTH_SHORT).show();
                   return;
               }

                progress.setVisibility(View.VISIBLE);
                uploadFile(pdfFileUri);

            }
        });


        bt_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
    }

    private void choosePDF()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            //if a file is selected
            if (data.getData() != null)
            {
                pdfFileUri = data.getData();
                String uriString = data.getData().toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;

                if (uriString.startsWith("content://"))
                {
                    Cursor cursor = null;
                    try {
                        cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                        if (cursor != null && cursor.moveToFirst())
                        {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                }
                else if (uriString.startsWith("file://"))
                {
                    displayName = myFile.getName();
                }

                tv_upload.setText(displayName);


            }

            }
            else
            {
                Toast.makeText(getContext(), "No file chosen", Toast.LENGTH_SHORT).show();
                ll_upload.setEnabled(true);
                etEmail.setEnabled(true);
                etProfile.setEnabled(true);
            }

        }


    private void uploadFile(Uri data)
    {
        final String key = mCVRef.push().getKey();
        final StorageReference sRef = FirebaseStorage.getInstance().getReference().child("Resume/" +  key + ".pdf");
        UploadTask uploadTask = sRef.putFile(data);

       /* Task<Uri> urlTask = */uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return sRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                {
                    Uri downloadUri = task.getResult();
                    Log.i("downlad", String.valueOf(downloadUri));
                    ll_upload.setEnabled(true);

                    Opportunity opportunity = new Opportunity(email,profile, tv_upload.getText().toString(), downloadUri.toString(), ServerValue.TIMESTAMP);

                    mCVRef.child(key).setValue(opportunity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                progress.setVisibility(View.GONE);

                                ll_input.setVisibility(View.INVISIBLE);
                                title.setText("Successfully Uploaded!");
                                subtitle.setText("Take a screenshot of this Reffrence ID");
                                tv_ref_id.setVisibility(View.VISIBLE);
                                tv_ref_id.setText(/*"Reffrence ID\n" + */key);
                                btDone.setVisibility(View.VISIBLE);
                                ll_upload.setEnabled(true);
                            }
                            else
                            {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Try after some time", Toast.LENGTH_LONG).show();
                                ll_upload.setEnabled(true);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception exception)
                                {
                                    Toast.makeText(getContext(), "please try again!", Toast.LENGTH_LONG).show();
                                    progress.setVisibility(View.GONE);
                                }
                            });;

                } else {
                    ll_upload.setEnabled(true);
                    Toast.makeText(getContext(), "Try after some time", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void openWhatsApp()
    {
        String smsNumber = "919928091259";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled)
        {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net"); //phone number without "+" prefix
            startActivity(sendIntent);
        }
        else
        {
            Uri uri = Uri.parse("market:details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri)
    {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed;
    }


}
