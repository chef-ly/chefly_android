package com.se491.chef_ly.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.lock.Lock;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.BuildConfig;
import com.se491.chef_ly.R;
import com.se491.chef_ly.application.CheflyApplication;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.RecipeList;
import com.se491.chef_ly.utils.CredentialsManager;
import com.se491.chef_ly.utils.GetRecipesFromServer;
import com.se491.chef_ly.utils.NetworkHelper;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class  MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<RecipeList>, View.OnClickListener{

    private EditText username;
    private EditText password;
    private final String TAG = "MainActivity";
    private Lock mLock;
    private Handler splashHandler;
    private final int INTROACTIVITYCODE = 1775;


    private static final String urlString ="http://www.chef-ly.com/list/random/10?p=0";
    //TODO update urlFacsString

    private RecipeList serverRecipes;


    /* Used to handle permission request from PocketSphinx*/
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private final int RECIPELISTID = 1201;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        serverRecipes = new RecipeList();



        if(!BuildConfig.DEBUG){
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            // Check if we need to display our OnboardingFragment
            if (!sharedPreferences.getBoolean("FirstTimeRun", false)) {
                Log.d(TAG, "First time run-> True");
                // The user hasn't seen the OnboardingFragment yet, so show it
                startActivityForResult(new Intent(this, IntroActivity.class), INTROACTIVITYCODE);
            }else{
                Log.d(TAG, "First time run-> False");
                startRecipeLoader();
            }
        }else{
            Log.d(TAG, "First time run-> True");
            // The user hasn't seen the OnboardingFragment yet, so show it
            startActivityForResult(new Intent(this, IntroActivity.class), INTROACTIVITYCODE);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        /*  Register receiver for Alarm
        IntentFilter filter = new IntentFilter();
        filter.addAction("alarm");
        final CheflyTimer c = CheflyTimer.getInstance(getApplicationContext());
        registerReceiver(new AlarmReceiver(c), filter);
        // Timer test

        c.setTimer("test",60, getApplicationContext());
        c.setTimer("test2",10, getApplicationContext());
        //  simulate checking a timer after 30 seconds
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "time remaining = " + c.getTimerStatus("test"), Toast.LENGTH_SHORT).show();
            }
        },30*1000);
        ////////////////////////////////////////////////////////////////////////////////////////////
        */


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INTROACTIVITYCODE){
            startRecipeLoader();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(BuildConfig.DEBUG){
            RefWatcher refWatcher = CheflyApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Credentials credentials = CredentialsManager.getCredentials(this);
        TextView skip = (TextView) findViewById(R.id.continueAsGuest);
        if (skip != null) {
            if (credentials.getAccessToken() == null) {
                skip.setText(getString(R.string.continueAsGuest));
                skip.setPaintFlags(skip.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }else {
                String name = CredentialsManager.getUsername(this);
                String msg = getString(R.string.welcomeback) + (name != null? " " + name : "");
                skip.setText(msg);
                skip.setPaintFlags(skip.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            }
        }

    }

    private void startRecipeLoader(){

        // Get recipes from server
        if(NetworkHelper.hasNetworkAccess(MainActivity.this)) //returns true if internet available
        {

            //Start AsyncTaskLoader to get recipes from server
            RequestMethod requestPackage = new RequestMethod();

            requestPackage.setEndPoint(urlString);
            requestPackage.setMethod("GET"); //  or requestPackage.setMethod("POST");
            //  serverConnection();
            Bundle bundle = new Bundle();
            bundle.putParcelable("requestPackage", requestPackage);

            getSupportLoaderManager().initLoader(RECIPELISTID, bundle,this).forceLoad();

            splashHandler = new Handler();
            splashHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //ViewGroup group = (ViewGroup) findViewById(R.id.activity_main);

                    setContentView(R.layout.activity_main);
                }
            }, 5000);

        }
        else
        {
            //Toast.makeText(RecipeListActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            Log.d(TAG, "No Internet Connection");
        }

    }


    private void setupViews() {
        try{
            Button signInBtn;
            TextView continueAsGuest;
            TextView signUp;
            ImageButton webLoginButton = (ImageButton) findViewById(R.id.webLoginButton);
            webLoginButton.setOnClickListener(this);
            ImageButton googleLoginButton = (ImageButton) findViewById(R.id.googleLoginButton);
            googleLoginButton.setOnClickListener(this);
            username = (EditText) findViewById(R.id.useremail);
            password = (EditText) findViewById(R.id.password);
            signInBtn = (Button) findViewById(R.id.signInBtn);
            signInBtn.setOnClickListener(this);
            continueAsGuest = (TextView) findViewById(R.id.continueAsGuest);
            continueAsGuest.setOnClickListener(this);
            signUp = (TextView) findViewById(R.id.signUp);
            signUp.setOnClickListener(this);

        }catch (NullPointerException e){
            Log.d(TAG, "Failed to set views");
        }


    }

    private void login(final String emailOrUsername, String password) {

        Log.d(TAG, "LOGIN ENTERED");
        Login login = new Login(emailOrUsername, password);
        login.execute(new RequestMethod());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Thanks, now you can talk to chef.ly!", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "You didn't grant chef.ly permission to use the mic.", Toast.LENGTH_LONG).show();
        }
    }


//for social connections like google and fb

    @Override
    protected void onNewIntent(Intent intent) {
        if (WebAuthProvider.resume(intent)) {
            return;
        }
        super.onNewIntent(intent);
    }


    private void socialLogin(String connection) {  //getString(R.string.auth0_domain
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id),getString(R.string.auth0_domain));
        WebAuthProvider.init(auth0)
                .withConnection(connection)
                .start(MainActivity.this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull Dialog dialog) {
                        dialog.show();
                    }

                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        //Show error to the user
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "LOGIN FAIL");
                                String errorMsg = "Sign in request failed";
                                showToast(errorMsg);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {
                        // Navigate to your next activity

                        startRecipeListActivity("aaa");
                    }
                });
    }



    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
Log.d(TAG, "On Click - " + v.getId());
        switch (v.getId()) {

            case R.id.signInBtn:
                Editable user = username.getText();
                Editable pword = password.getText();
                if (user.length() == 0) {
                    Toast.makeText(this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
                } else if (pword.length() == 0) {
                    Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                } else {
                    login(user.toString(), pword.toString());
                }
                password.getText().clear();
                break;
            case R.id.continueAsGuest:
                startRecipeListActivity("guest");

                break;
            case R.id.webLoginButton:
                socialLogin("facebook");
                break;
            case R.id.googleLoginButton:
                socialLogin("google-oauth2");
                break;
            case R.id.signUp:
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            default:

        }
    }

    /*private boolean authenticate(Editable user, Editable password) {
        return User.authenticateExisting(user.toString(), password.toString());
    }*/

//    public void serverConnection(){
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("requestPackage", requestPackage);
//
//        getSupportLoaderManager().initLoader(1, bundle,this).forceLoad();
//        splashHandler = new Handler();
//        splashHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //ViewGroup group = (ViewGroup) findViewById(R.id.activity_main);
//
//                setContentView(R.layout.activity_main);
//            }
//        }, 5000);
//    }

    //  LoaderManager callback method
    @Override
    public Loader<RecipeList> onCreateLoader(int id, Bundle args) {
        RequestMethod rm = args.getParcelable("requestPackage");
        return  new GetRecipesFromServer(getApplicationContext(), rm);
    }
    //  LoaderManager callback method

    @Override
    public void onLoadFinished(Loader<RecipeList> loader, RecipeList data) {
        int id = loader.getId();
        if(id == RECIPELISTID){
            serverRecipes = data;
            splashHandler.removeCallbacksAndMessages(null);
            setContentView(R.layout.activity_main);
            setupViews();

           Credentials credentials = CredentialsManager.getCredentials(this);

            if (credentials.getAccessToken() != null ) {
                if( credentials.getExpiresIn() > 0){
                    TextView skip = (TextView) findViewById(R.id.continueAsGuest);
                    String name = CredentialsManager.getUsername(this);
                    String msg = getString(R.string.welcomeback) + (name != null? " " + name : "");
                    skip.setText(msg);
                    skip.setPaintFlags(skip.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                }
            }
        }
        Log.d(TAG, "OnLoadFinished " + loader.getId());
    }
    //  LoaderManager callback method
    @Override
    public void onLoaderReset(Loader<RecipeList> loader) {

        if(getTaskId() == RECIPELISTID){
            serverRecipes = new RecipeList();
        }
    }

    private void startRecipeListActivity(String name){
        /*//TODO - Remove shared pref setting of FirstTimeRun
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean("FirstTimeRun", false);
        sharedPreferencesEditor.apply();*/

        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
        recipeListIntent.putExtra("name", name);
        recipeListIntent.putExtra("recipeList", serverRecipes);
        Log.d(TAG, "ServerRecipes size = " + serverRecipes.size());
        startActivity(recipeListIntent);


    }


    private class Login extends AsyncTask<RequestMethod, Integer, String> {
        private String emailOrUsername;
        private String password;
        private String response;
        private String statusMessage;

        Login(String emailOrUsername, String password) {
            this.emailOrUsername = emailOrUsername;
            this.password = password;
        }

        @Override
        protected String doInBackground(RequestMethod... requestMethod) {
            String urlString = "https://chefly.auth0.com/oauth/token";
            RequestMethod rm = new RequestMethod();
            rm.setEndPoint(urlString);

            rm.setParam("audience", "chefly-api");
            rm.setParam("scope", "userinfo openid");
            rm.setParam("username", emailOrUsername);
            rm.setParam("password", password);

            rm.setParam("client_id", getString(R.string.auth0_client_id));
            rm.setParam("grant_type", "password");
            rm.setMethod("POST");
            String response = "";
            try {
                HttpConnection http = new HttpConnection();

                response = http.downloadFromFeed(rm);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null || response.equals("")) {
                Log.d(TAG, "Invalid Login HTTP Response");

            } else {
                Log.d(TAG, "LOGIN TOKEN RESPONSE: " + response);
                this.response = response;
                if (!response.startsWith("Error")) {
                    Log.d(TAG, "LOGIN SUCCESS");
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Type type = new TypeToken<Credentials>() {
                    }.getType();

                    //is this legal?
                    Credentials credentials = gson.fromJson(response, type);
                    //store login credentials
                    CredentialsManager.saveCredentials(MainActivity.this, credentials);
                    CredentialsManager.saveUsername(MainActivity.this, emailOrUsername);

                    startRecipeListActivity(emailOrUsername);

                } else {
                    String responseCode = response.substring(6);
                    Log.d(TAG, responseCode);
                    if(!NetworkHelper.hasNetworkAccess(MainActivity.this)){
                        Toast.makeText(MainActivity.this, "Login Failed No Internet Connection", Toast.LENGTH_SHORT).show();
                    }else if(responseCode.startsWith("4")){
                        Toast.makeText(MainActivity.this, "Invalid Credentials, Please try again", Toast.LENGTH_LONG).show();
                    }else{
                        Log.d(TAG, "LOGIN FAIL - " + response);
                        String errorMsg = "Sign in request failed";
                        showToast(errorMsg);
                    }
                }
            }
        }

        String generateCodeChallenge() throws UnsupportedEncodingException, NoSuchAlgorithmException{
            SecureRandom sr = new SecureRandom();
            byte[] code = new byte[32];
            sr.nextBytes(code);
            String verifier = Base64.encodeToString(code, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
            byte[] bytes = verifier.getBytes("US-ASCII");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes, 0, bytes.length);
            byte[] digest = md.digest();
            String challenge = Base64.encodeToString(digest, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
            return challenge;
        }
    }
}
