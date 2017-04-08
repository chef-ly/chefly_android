package com.se491.chef_ly.activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.se491.chef_ly.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


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
        return false;
    }

}

