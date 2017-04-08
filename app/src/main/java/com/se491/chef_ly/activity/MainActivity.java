package com.se491.chef_ly.activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;
import com.se491.chef_ly.R;
import com.se491.chef_ly.model.User;
import com.se491.chef_ly.utils.AlarmReceiver;
import com.se491.chef_ly.utils.CheflyTimer;

import java.util.concurrent.locks.Lock;

import static android.view.View.X;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String urlString = "https://pure-fortress-13559.herokuapp.com/user/register";

    private EditText username;
    private EditText password;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        Toast.makeText(getApplicationContext(), "Welcome To Chef.ly", Toast.LENGTH_SHORT).show();
        //displayDataItems(category); in a list for to manage sliding navigation

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

//        Auth0 auth0 = new Auth0("AcrZOhtTF6oQEPQAL93Eud0HuLWKQ8fb", "athina.auth0.com");
//        lock = Lock.newBuilder(auth0, callback)
//                // Add parameters to the Lock Builder
//                .build(this);
    }

    private void setupViews() {
        Button signInBtn;
        TextView continueAsGuest;
        TextView signUp;

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(this);
        continueAsGuest = (TextView) findViewById(R.id.continueAsGuest);
        continueAsGuest.setOnClickListener(this);
        signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

    }
    private void login(String email, String password) {
        Auth0 auth0 = new Auth0("AcrZOhtTF6oQEPQAL93Eud0HuLWKQ8fb", "athina.auth0.com");
        AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);
        String connectionName = "Username-Password-Authentication";
        client.login(email, password, connectionName)
                .start(new BaseCallback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials payload) {
                        // Store credentials
                        // Navigate to your main activity
                        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipeListIntent.putExtra("name", "aaa");
                        startActivity(recipeListIntent);
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        // Show error to user

                    }
                });
        // proper login
    }
    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.signInBtn:
      Editable user = username.getText();
              Editable pword = password.getText();
               // login(email,password);

//                if (user.length() == 0) {
//                    Toast.makeText(this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
//                } else if (pword.length() == 0) {
//                    Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
//                } else {
//                    login(user,password);
//                    boolean isAllowed = authenticate(user, pword);
//                    if (isAllowed) {
//                       // curl -X POST -d "access_token=123456789" \
//                       // "http://localhost:3000/api/auth/facebook"
//
//                        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
//                        recipeListIntent.putExtra("name", user.toString());
//                        startActivity(recipeListIntent);
//                    } else {
//                        Toast.makeText(this, "Invalid Username or password", Toast.LENGTH_SHORT).show();
//                    }
//                }

                break;
            case R.id.continueAsGuest:

                Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                recipeListIntent.putExtra("name", "guest");
                startActivity(recipeListIntent);
                break;
            case R.id.signUp:
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            default:

        }
    }

    private boolean authenticate(Editable user, Editable password) {
        return User.authenticateExisting(user.toString(), password.toString());
    }

}

