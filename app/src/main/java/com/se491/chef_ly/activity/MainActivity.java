package com.se491.chef_ly.activity;

import android.app.Dialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
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
import com.mashape.unirest.request.BaseRequest;
import com.se491.chef_ly.R;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.UserSessionManager;
import com.se491.chef_ly.utils.CredentialsManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private EditText username;
    private EditText password;
    private final String TAG = "MainActivity";
    private Lock mLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        Toast.makeText(getApplicationContext(), "Welcome To Chef.ly", Toast.LENGTH_SHORT).show();


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

//        // save users from having to re-enter their login credentials when relaunching the app
//        if(CredentialsManager.getCredentials(this).getIdToken() == null) {
//            // Prompt Login screen.
//        }
//        else {
//            AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
//            aClient.tokenInfo(CredentialsManager.getCredentials(this).getIdToken())
//                    .start(new BaseCallback<UserProfile, AuthenticationException>() {
//                        @Override
//                        public void onSuccess(UserProfile payload) {
//                            // Valid ID > Navigate to the app's MainActivity
//                            Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
//                            recipeListIntent.putExtra("name", "aaa");
//                            startActivity(recipeListIntent);
//                        }
//
//                        @Override
//                        public void onFailure(AuthenticationException error) {
//                            // Invalid ID Scenario
//                        }
//                    });
//        }

    }

    private void setupViews() {
        Button signInBtn;
        TextView continueAsGuest;
        TextView signUp;
        Button webLoginButton = (Button) findViewById(R.id.webLoginButton);
        webLoginButton.setOnClickListener(this);
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


        // get a valid refresh_token in the response

//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("scope", "openid offline_access");
//        Lock lock = Lock.newBuilder(auth0, callback)
//                .withAuthenticationParameters(parameters)
//                .build(this);
//        startActivity(lock.newIntent(this));

        Log.d(TAG, "LOGIN ENTERED");
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);
        String connectionName = getString(R.string.auth0_databaseConnection);
        client.login(emailOrUsername, password, connectionName)
                .setAudience("https://chefly.auth0.com/api/")
                .start(new BaseCallback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        Log.d(TAG, credentials.getType());
                        Log.d(TAG, credentials.getIdToken());
                        Log.d(TAG, credentials.getAccessToken());
                        Log.d(TAG, "LOGIN SUCCESS!");
                        Login login = new Login();
                        login.execute(new RequestMethod());
                        // Store credentials- how do we want to do this? Store in shared preferences?
                        CredentialsManager.saveCredentials(MainActivity.this, credentials);
                        CredentialsManager.saveUsername(MainActivity.this, emailOrUsername);

                        // Navigate to your next activity
                        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipeListIntent.putExtra("name", emailOrUsername);
                        startActivity(recipeListIntent);
                    }


                    @Override
                    public void onFailure(AuthenticationException error) {
                        // Show error to user
                        Log.d(TAG, "LOGIN FAIL");
                        String errorMsg = "Sign in request failed";
                        showToast(errorMsg);

                       // CredentialsManager.deleteCredentials(MainActivity.this);

                    }
                });

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

    private void socialLogin() {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        WebAuthProvider.init(auth0)
                .withConnection("facebook")
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
                startActivity(recipeListIntent);
                break;
            case R.id.webLoginButton:
                socialLogin();
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

    class Login extends AsyncTask<RequestMethod, Integer, String> {

        public Login() {

        }

        @Override
        protected String doInBackground(RequestMethod... requestMethod) {
            String urlString = "https://chefly.auth0.com/authorize";
            RequestMethod rm = new RequestMethod();
            rm.setEndPoint(urlString);
            //try {
                rm.setParam("audience", "https://chefly.auth0.com/api/");
                rm.setParam("redirect_uri", "http://chefly-dev.herokuapp.com/*");
                rm.setParam("scope", "read:user");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            rm.setParam("client_id", getString(R.string.auth0_client_id));
            try {
                rm.setParam("code_challenge", this.generateCodeChallenge() );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            rm.setParam("code_challenge_method", getString(R.string.auth0_code_challenge_method));
            rm.setParam("response_type", "code");
            rm.setMethod("GET");
            String response = "";
            try {
                response = HttpConnection.downloadFromFeed(rm);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            Log.d(TAG, "LOGIN TOKEN RESPONSE: " + response);
            WebView wv = new WebView(MainActivity.this);
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

