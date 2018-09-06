package com.swastikenterprises.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swastikenterprises.DB.User;
import com.swastikenterprises.Home.HomeActivity;
import com.swastikenterprises.R;
import com.swastikenterprises.helper.PrefManager;


public class LoginActivity extends AppCompatActivity
{
    private ProgressBar mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersRef;//

    private User user = new User();;


    private static final int RC_SIGN_IN = 234;

    private static final String TAG = new LoginActivity().getClass().getSimpleName();

    GoogleSignInClient mGoogleSignInClient;


    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if(prefManager.getSesion())
        {
            Log.i("evbv", "1");
            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        }
        setContentView(R.layout.activity_login);

       // Toast.makeText(this, "LOgin", Toast.LENGTH_SHORT).show();


                mAuth = FirebaseAuth.getInstance();
        mUsersRef = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestId()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

        mProgress = findViewById(R.id.progress);
        findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mProgress.setVisibility(View.VISIBLE);

                signIn();
            }
        });
    }



    private void signIn()
    {

        mGoogleSignInClient.revokeAccess();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e)
            {
                //this exception ,ight be due to internet problem or user open acccnt chooser & then cancel it
                //Toast.makeText(MainActivity.this, e.getMessage() + "you have to choose an account", Toast.LENGTH_SHORT).show();

                mProgress.setVisibility(View.GONE);
            }
        }
    }




    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount)
    {
        String userName = "";
        String userEmail = "";
        Log.i(TAG, "firebaseAuthWithGoogle:" + googleSignInAccount.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        if (googleSignInAccount != null)
        {
            userName = googleSignInAccount.getDisplayName();
            userEmail = googleSignInAccount.getEmail();
            //userPhoto = String.valueOf(googleSignInAccount.getPhotoUrl());
            //String personGivenName = googleSignInAccount.getGivenName();
            //String personId = googleSignInAccount.getId();
            //Uri personPhoto = googleSignInAccount.getPhotoUrl();
            //Log.i("accnt details", personName + personGivenName + personEmail + personId + personPhoto);

            user.setEmail(userEmail);
            user.setName(userName);


            final String finalUserName = userName;
            final String finalUserEmail = userEmail;
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        prefManager.setSesion(true);
                        subscribe();
                         FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                         Intent accntSetup_intent = new Intent(LoginActivity.this, HomeActivity.class);

                        accntSetup_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);


                        prefManager.setUser_name(finalUserName);
                        prefManager.setEmail(finalUserEmail);


                        mProgress.setVisibility(View.GONE);
                        startActivity(accntSetup_intent);
                    }
                    else
                        {
                            mProgress.setVisibility(View.GONE);
                        Log.i(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Please check you internet.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void subscribe()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("offers");
        //Toast.makeText(LoginActivity.this, "subscribed", Toast.LENGTH_SHORT).show();
    }
}
