package com.swastikenterprises.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.swastikenterprises.DB.User;
import com.swastikenterprises.Dashboard.HomeActivity;
import com.swastikenterprises.R;


public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersRef;//

    private User user = new User();;


    //home_form constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = new LoginActivity().getClass().getSimpleName();

    //creating home_form GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mUsersRef = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestId()
                .requestEmail()
                .build();


        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach home_form click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signIn();
            }
        });
    }



    //this method is called on click
    private void signIn()
    {
        //getting the google signin intent
        /*if (mGoogleSignInClient.(Auth.GOOGLE_SIGN_IN_API))
        {
            mGoogleSignInClient.clearDefaultAccountAndReconnect();
        }*/
        mGoogleSignInClient.revokeAccess();
        //mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    // This IS the method where the result of clicking the signIn button will be handled
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN)
        {
            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e)
            {
                //this exception ,ight be due to internet problem or user open acccnt chooser & then cancel it
                //Toast.makeText(MainActivity.this, e.getMessage() + "you have to choose an account", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount)
    {


        String userName = "";
        String userEmail = "";
        String userPhoto = "";
        Log.i(TAG, "firebaseAuthWithGoogle:" + googleSignInAccount.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        if (googleSignInAccount != null) {
            userName = googleSignInAccount.getDisplayName();
            userEmail = googleSignInAccount.getEmail();
            userPhoto = String.valueOf(googleSignInAccount.getPhotoUrl());
            //String personGivenName = googleSignInAccount.getGivenName();
            //String personId = googleSignInAccount.getId();
            //Uri personPhoto = googleSignInAccount.getPhotoUrl();
            //Log.i("accnt details", personName + personGivenName + personEmail + personId + personPhoto);

            user.setEmail(userEmail);
            user.setName(userName);
            // user.setPhoto(userPhoto);


            //Now using firebase we are signing in the user here so, after completion of this task "credential are stored in firebase authentication service" with provider(as Google)
           /* final String finalUserEmail = userEmail;
            final String finalUserName = userName;*/


            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
//                       ?


                        //mUsersRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);

                         FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                         Intent accntSetup_intent = new Intent(LoginActivity.this, HomeActivity.class);
                        //accntSetup_intent.putExtra("email", finalUserEmail);
                        //accntSetup_intent.putExtra("name", finalUserName);

                        startActivity(accntSetup_intent);
                    }
                    else
                        {
                        // If sign in fails, display home_form message to the user.
                        Log.i(TAG, "signInWithCredential:failure", task.getException());
                        //Toast.makeText(MainActivity.this, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "Please check you internet.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
