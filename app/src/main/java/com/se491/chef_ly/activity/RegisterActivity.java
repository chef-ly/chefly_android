package com.se491.chef_ly.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.InputType;
import android.widget.Toast;

import com.se491.chef_ly.http.MyService;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.User;
import android.util.Log;
import com.se491.chef_ly.R;
import com.se491.chef_ly.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private final String TAG = "RegisterActivity";

    private static String urlString = "https://pure-fortress-13559.herokuapp.com/user/register";

    private String token;

    private Button nextButton;
    private TextView instruction;
    private EditText input;

    private String username;
    private String password;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                token = intent.getStringExtra(MyService.MY_SERVICE_RESPONSE_STRING);
                Log.d(TAG, token);

                if (token != null) {
                    JSONObject jsonToken = new JSONObject(token);
                    User.saveAuthentication(username, token);

                    // Direct user to list view
                    Intent recipeListIntent = new Intent(RegisterActivity.this, RecipeListActivity.class);
                    recipeListIntent.putExtra("name", username);
                    startActivity(recipeListIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "There was a problem while registering your account", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nextButton = (Button) findViewById(R.id.registerButton);
        nextButton.setOnClickListener(this);
        instruction = (TextView) findViewById(R.id.registerInstruction);
        input = (EditText) findViewById(R.id.registerInput);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registerButton:
                handleRegisterButtonClick();
                break;
            default:
                // TODO error handler?
        }
    }

    @Override
    protected void onDestroy() { //unregister to listen the data
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    private void handleRegisterButtonClick() {
        CharSequence currentInstruction = instruction.getText();

        if (currentInstruction.equals(getResources().getText(R.string.chooseUsername))) {
            username = input.getText().toString(); //save username
            instruction.setText(R.string.createPassword); // update instruction
            input.setText(""); // clear input
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // hide password
        } else if (currentInstruction.equals(getResources().getText(R.string.createPassword))) {
            password = input.getText().toString();
            instruction.setText(R.string.createPasswordAgain); // update instruction
            input.setText(""); // clear input
            nextButton.setText(R.string.done); // Change text of button
        } else if (currentInstruction.equals(getResources().getText(R.string.createPasswordAgain))) {
            // check if password matches new input
            if (input.getText().toString().equals(password)) {
                registerNewUser();
            } else {
                Toast.makeText(getApplicationContext(), "Your passwords did not match. Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            // TODO error handler......
        }
    }

    private void registerNewUser() {
        RequestMethod requestPackage = new RequestMethod();

        requestPackage.setEndPoint(urlString);
        requestPackage.setParam("username", username);
        requestPackage.setParam("password", password); //filter data if i want
        requestPackage.setMethod("POST");
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
        startService(intent);
    }
}
