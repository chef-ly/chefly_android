package com.se491.chef_ly.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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
import android.accounts.AccountManager;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.jwt.JWT;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.internal.configuration.OAuthConnection;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.R;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;

import com.se491.chef_ly.utils.CredentialsManager;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.RecipeList;
import com.se491.chef_ly.utils.CredentialsManager;
import com.se491.chef_ly.utils.GetRecipesFromServer;
import com.se491.chef_ly.utils.NetworkHelper;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;


public class  MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<RecipeList>, View.OnClickListener{

    private EditText username;
    private EditText password;
    private final String TAG = "MainActivity";
    private Lock mLock;
    private Handler splashHandler;


    private static final String urlString ="https://chefly-prod.herokuapp.com/list/random/10";
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


        //Toast.makeText(getApplicationContext(), "Welcome To Chef.ly", Toast.LENGTH_SHORT).show();


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


        // Request permissions from the user here so that everything works better on GetCookingActivity
        Log.e("DEBUG", "Starting check");
       // int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.e("DEBUG", "Requesting permissions!");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);

            return;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void setupViews() {
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

    }

    private void login(final String emailOrUsername, String password) {

        Log.d(TAG, "LOGIN ENTERED");
        Login login = new Login(emailOrUsername, password);
        login.execute(new RequestMethod());

    }


        @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks, now you can talk to chef.ly!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "You didn't grant chef.ly permission to use the mic.", Toast.LENGTH_LONG).show();
            }
        }


//for social connections like google and fb

    @Override
    protected void onNewIntent(Intent intent) {
        //Check if the result belongs to a pending web authentication
        if (WebAuthProvider.resume(intent)) {
            return;
        }
        super.onNewIntent(intent);
    }


    private void socialLogin(String connection) {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
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

                        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipeListIntent.putExtra("name", "aaa");
                        recipeListIntent.putExtra("recipeList", serverRecipes);
                        startActivity(recipeListIntent);
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
        Toast.makeText(this, "onClick " + v.getId(), Toast.LENGTH_SHORT).show();
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

                break;
            case R.id.continueAsGuest:
                Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                recipeListIntent.putExtra("name", "guest");
                recipeListIntent.putExtra("recipeList", serverRecipes);
                startActivity(recipeListIntent);
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


    //TODO: Find way to store the login strings in resources. Formatting for HTTP calls gets in way. */
    class Login extends AsyncTask<RequestMethod, Integer, String> {
        private String emailOrUsername;
        private String password;
        private String response;
        private String statusMessage;

        public Login(String emailOrUsername, String password) {
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
                this.statusMessage = http.getStatusMessage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.equals(null) | response.equals("")) {
                Log.d(TAG, "Invalid Login HTTP Response");

            } else {
                Log.d(TAG, "LOGIN TOKEN RESPONSE: " + response);
                Log.d(TAG, "STATUS MESSAGE: " + this.getStatusMessage());
                this.response = response;
                if (this.getStatusMessage().contains("OK")) {
                    Log.d(TAG, "LOGIN SUCCESS");
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Type type = new TypeToken<Credentials>() {
                    }.getType();

                    //is this legal?
                    Credentials credentials = gson.fromJson(this.getReponse(), type);
                    //store login credentials
                    CredentialsManager.saveCredentials(MainActivity.this, credentials);
                    CredentialsManager.saveUsername(MainActivity.this, emailOrUsername);

                    // Navigate to your next activity
                    Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                    recipeListIntent.putExtra("name", emailOrUsername);
                    recipeListIntent.putExtra("recipeList", serverRecipes);
                    startActivity(recipeListIntent);

                } else {
                    Log.d(TAG, "LOGIN FAIL");
                    String errorMsg = "Sign in request failed";
                    showToast(errorMsg);
                }
            }
        }

        public String getReponse(){
            return this.response;
        }

        public String getStatusMessage() { return this.statusMessage; }

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

