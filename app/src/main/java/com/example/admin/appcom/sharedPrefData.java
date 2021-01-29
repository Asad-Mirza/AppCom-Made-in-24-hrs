package com.example.admin.appcom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.File;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Asad Mirza on 22-02-2018.
 */

public class sharedPrefData {

    SharedPreferences pref;
    static private final String userFLAG = "initialized";

    static private final String sharedPreName = "USER_PROFILE";

    public void saveUserData(userPOJO u, Context mContext) {


        pref = mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("isLogIn", true);
        edt.putString("name", u.getName());
        edt.putString("photo", u.getImageURL());
        edt.putString("email", u.getEmail());
        edt.putLong("phone", u.getMobile_no());
        edt.putString("type", u.getSignInType());

        boolean commit = edt.commit();
        if (!commit)
            mContext.startActivity(new Intent(mContext, loginActivity.class));

    }


    userPOJO getUserData(final Context mContext) {

        pref = mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);


        final userPOJO u = new userPOJO();
        String name = pref.getString("name", null);
        String photo = pref.getString("photo", null);
        String email = pref.getString("email", null);
        long phone = pref.getLong("phone", 0);
        String type = pref.getString("type", null);

        u.setName(name);
        u.setMobile_no(phone);
        u.setImageURL(photo);
        u.setEmail(email);
        u.setSignInType(type);


        return u;


    }

    void checkLogin(Context context) {
        pref = context.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);
        boolean flag = pref.getBoolean("isLogIn", false);
        if (flag) context.startActivity(new Intent(context, MainActivity.class));
        else
            context.startActivity(new Intent(context, loginActivity.class));


    }
    void logOutUser(final Context context){
        pref = context.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);
        String type = pref.getString("type", null);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("isLogIn", false);
        if (type.equals("google")){
            deleteCache(context);
            context.startActivity(new Intent(context,loginActivity.class));
            ((Activity)context).finish();
           /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken("76074027270-6cv2eo3pk95156njn4dcph1e7c242hqs.apps.googleusercontent.com")
                    .build();
          GoogleApiClient  mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


           GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
           mGoogleApiClient.connect();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            context.startActivity(new Intent(context,loginActivity.class));
                            ((Activity)context).finish();
                        }
                    });
*/

        }else  if (type.equals("facebook")){
            LoginManager.getInstance().logOut();
            context.startActivity(new Intent(context,loginActivity.class));
            ((Activity)context).finish();



        }



    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}
