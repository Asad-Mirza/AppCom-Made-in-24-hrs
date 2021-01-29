package com.example.admin.appcom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class loginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    int RC_SIGN_IN  = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton  = findViewById(R.id.login_button);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            ProfileTracker profileTracker;


            @Override
            public void onSuccess(final LoginResult loginResult) {
                final ProgressDialog progressDialog  =new ProgressDialog(loginActivity.this);
                progressDialog.show();

                     final userPOJO p = new userPOJO();

                     profileTracker = new ProfileTracker() {
                         @Override
                         protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                             if (currentProfile!=null) {
                                 p.setImageURL(currentProfile.getProfilePictureUri(100, 100).toString());
                                 p.setName(currentProfile.getName());
                                 progressDialog.setMessage("Please wait");


                                 GraphRequest request = GraphRequest.newMeRequest(
                                         loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                             @Override
                                             public void onCompleted(JSONObject me, GraphResponse response) {


                                                 if (response.getError() != null) {

                                                     Toast.makeText(loginActivity.this, "Error in Email", Toast.LENGTH_SHORT).show();
                                                 } else {


                                                     String email = null;
                                                     try {
                                                         email = me.getString("email");
                                                     } catch (JSONException e) {

                                                         email = "";
                                                     }
                                                     // Toast.makeText(loginActivity.this, email, Toast.LENGTH_SHORT).show();
                                                     p.setEmail(email);
                                                     p.setSignInType("facebook");
                                                     sharedPrefData data = new sharedPrefData();
                                                     data.saveUserData(p, loginActivity.this);
                                                     progressDialog.dismiss();
                                                     startActivity(new Intent(loginActivity.this, MainActivity.class));

                                                     finish();


                                                 }
                                             }
                                         });
                                 request.executeAsync();
                             }
                         }
                     };











            }





            @Override
            public void onCancel() {
                Toast.makeText(loginActivity.this, "Error please try again", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(loginActivity.this, "Error please try again", Toast.LENGTH_SHORT).show();
            }
        });

// google sign in
        signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("76074027270-6cv2eo3pk95156njn4dcph1e7c242hqs.apps.googleusercontent.com")
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton = findViewById(R.id.sign_in_button);
       signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });





    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            ProgressDialog progressDialog  =new ProgressDialog(loginActivity.this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            final userPOJO p = new userPOJO();
            p.setName(account.getDisplayName());
            p.setEmail(account.getEmail());
            p.setImageURL(account.getPhotoUrl().toString());
            p.setSignInType("google");
            new sharedPrefData().saveUserData(p,loginActivity.this);
            progressDialog.dismiss();
            startActivity(new Intent(loginActivity.this,MainActivity.class));
            finish();

            // Signed in successfully, show authenticated UI.



        } catch (ApiException e) {
            Toast.makeText(this, "Error in Google Sign in", Toast.LENGTH_SHORT).show();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLEE", "signInResult:failed code=" + e.getLocalizedMessage());
            // updateUI(null);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {



            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }else
       callbackManager.onActivityResult(requestCode, resultCode, data);

       // super.onActivityResult(requestCode, resultCode, data);
    }
}
