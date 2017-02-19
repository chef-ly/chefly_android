package com.se491.chef_ly.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import com.se491.chef_ly.R;
import com.se491.chef_ly.http.MyService;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.Recipe;
import com.se491.chef_ly.model.User;
import com.se491.chef_ly.utils.NetworkHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText username;
    private EditText password;
    private Button signInBtn;
    private TextView continueAsGuest;
    private TextView signUp;
    private final String TAG = "MainActivity";
   private static final String urlString ="https://pure-fortress-13559.herokuapp.com/list";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        Toast.makeText(getApplicationContext(), "Welcome To Chef.ly", Toast.LENGTH_SHORT).show();
        //displayDataItems(category); in a list for to manage sliding navigation

        if(NetworkHelper.hasNetworkAccess(MainActivity.this)) //returns true if internet available
        {
            //Toast.makeText(MainActivity.this,"Internet Connection",Toast.LENGTH_LONG).show();
            Log.d(TAG,"Internet Connection" );

        }
        else {
            //Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            Log.d(TAG,"No Internet Connection" );

        }
    }

    private void setupViews() {

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(this);
        continueAsGuest = (TextView) findViewById(R.id.continueAsGuest);
        continueAsGuest.setOnClickListener(this);
        signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

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
                    boolean isAllowed = authenticate(user, pword);
                    if (isAllowed) {
                        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipeListIntent.putExtra("name", user.toString());
                        startActivity(recipeListIntent);
                    } else {
                        Toast.makeText(this, "Invalid Username or password", Toast.LENGTH_SHORT).show();
                    }
                }

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

