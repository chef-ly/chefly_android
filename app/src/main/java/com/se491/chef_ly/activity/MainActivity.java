package com.se491.chef_ly.activity;

import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.se491.chef_ly.R;


public class  MainActivity extends AppCompatActivity implements View.OnClickListener{

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



    }

    private void setupViews() {
        Button signInBtn;
        TextView continueAsGuest;
        TextView signUp;
        Button webLoginButton = (Button) findViewById(R.id.webLoginButton);
        webLoginButton.setOnClickListener(this);
        Button googleLoginButton = (Button) findViewById(R.id.googleLoginButton);
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

    private void login(String emailOrUsername, String password) {



        Log.d(TAG, "LOGIN ENTERED");
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);
        String connectionName = getString(R.string.auth0_databaseConnection);
        client.login(emailOrUsername, password, connectionName)
                .start(new BaseCallback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        Log.d(TAG, "LOGIN SUCCESS!");
                        // Store credentials- how do we want to do this? Store in shared preferences?
                       // CredentialsManager.saveCredentials(MainActivity.this, credentials);
                        // Navigate to your next activity
                        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipeListIntent.putExtra("name", "aaa");
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
        // proper login
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
            case R.id.googleLoginButton:
                //googleLogin();
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


}

