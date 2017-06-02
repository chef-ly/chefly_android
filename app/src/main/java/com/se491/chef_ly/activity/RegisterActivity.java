package com.se491.chef_ly.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.InputType;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.se491.chef_ly.application.CheflyApplication;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.MyService;
import com.se491.chef_ly.http.RequestMethod;

import android.util.Log;
import com.se491.chef_ly.R;
import com.se491.chef_ly.utils.CredentialsManager;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private final String TAG = "RegisterActivity";

    private static final String urlString = "https://chefly.auth0.com/dbconnections/signup";

    private String token;

    private Button nextButton;
    private TextView instruction;
    private EditText input;

    private String userEmail;
    private String username;
    private String password;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //try {
            token = intent.getStringExtra(MyService.MY_SERVICE_RESPONSE_STRING);
            Log.d(TAG, token);

            if (token != null) {
                // Direct user to login screen
                Toast.makeText(getApplicationContext(), "Registration is successful", Toast.LENGTH_SHORT).show();
            //    Intent recipeListIntent = new Intent(RegisterActivity.this, RecipeListActivity.class);
                Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
//            else if(token.exist){ is already in CredentialsManager.getCredentials()?
//                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
//                Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
//                startActivity(loginIntent);
//                finish();
//            }
            else {
                Toast.makeText(getApplicationContext(), "There was a problem while registering your account", Toast.LENGTH_SHORT).show();
               //the username or email already exist
                //go back to repeat register steps
                Intent mainIntent = new Intent(RegisterActivity.this, RegisterActivity.class);
                startActivity(mainIntent);
                finish();
            }
            //} catch (JSONException e) {
            //    e.printStackTrace();
            //}
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
                try {
                    handleRegisterButtonClick();
                } catch (UnirestException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

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

        if(BuildConfig.DEBUG){
            RefWatcher refWatcher = CheflyApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }
    }

    private void handleRegisterButtonClick() throws UnirestException, InterruptedException, ExecutionException {
        CharSequence currentInstruction = instruction.getText();

        if (currentInstruction.equals(getResources().getText(R.string.enterUserEmail))) {
            userEmail = input.getText().toString(); //save user email
            if(emailExists()) {
                String existsMsg = "Hmm, that email is registered. Did you already sign up?";
                Toast.makeText(RegisterActivity.this, existsMsg, Toast.LENGTH_SHORT).show();
                input.setText("");
                instruction.setText("Please enter your email");
            } else {
                instruction.setText(R.string.chooseUsername); // update instruction
                input.setText(""); // clear input

            }
        } else if (currentInstruction.equals(getResources().getText(R.string.chooseUsername))) {
            //check if username exists
            username = input.getText().toString();
            instruction.setText(R.string.createPassword);
            input.setText("");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // hide password
            nextButton.setText(R.string.next);
        } else if (currentInstruction.equals(getResources().getText(R.string.createPassword))) {
            password = input.getText().toString();
            if (isPasswordValid(password)) {
                registerNewUser();
            } else {
                Toast.makeText(getApplicationContext(), "The password is too weak, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    public static boolean isPasswordValid(String password){
        Pattern p =
                Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

        Matcher m = p.matcher(password);
        return(m.matches());
    }


    private boolean emailExists() throws InterruptedException, ExecutionException{
        Log.d(TAG, "Checking if email exists in user database...");
        String userEmailExists = null;

        //call Chefly server
        userEmailExists = new UserCheck(userEmail)
                            .execute(new RequestMethod())
                            .get();
        //check JSON response

        if(userEmailExists.equals("true")) {
            Log.d(TAG, "user email exists returns true");
            return true;
        } else if(userEmailExists.equals("false")) {
            Log.d(TAG, "user email exists returns false");
            return false;
        }
        Log.d(TAG, "user email exists DID NOT RETURN TRUE OR FALSE");
        return false;
    }



    private void registerNewUser() throws UnirestException {
        Log.d(TAG, "Register new user entered");


        RequestMethod requestPackage = new RequestMethod();
        requestPackage.setEndPoint(urlString);
        requestPackage.setParam("client_id", getResources().getText(R.string.auth0_client_id).toString());
        requestPackage.setParam("connection", getResources().getText(R.string.auth0_databaseConnection).toString());
        requestPackage.setParam("email",userEmail);
        requestPackage.setParam("password",password);
        requestPackage.setParam("username", username);
        requestPackage.setMethod("POST");
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
        startService(intent);
    }

    private class UserCheck extends AsyncTask<RequestMethod, Integer, String> {
        private String email;
        private String statusMessage;
        private String response;

        UserCheck(String email){
            this.email= email;
        }

        @Override
        protected String doInBackground(RequestMethod... requestMethod) {
            String urlString = "https://chefly-dev.herokuapp.com/exists?email=";
            urlString = urlString.concat(this.email);
            RequestMethod rm = new RequestMethod();
            rm.setEndPoint(urlString);
            rm.setMethod("GET");
            String response = "";
            Log.d(TAG, urlString);
            try {
                HttpConnection http = new HttpConnection();
                response = http.downloadFromFeed(rm);
                this.statusMessage = http.getStatusMessage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Log.d(TAG, email + " " + response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null || response.equals("")) {
                Log.d(TAG, "Invalid Login HTTP Response");

            } else {
                Log.d(TAG, "USER EXISTS RESPONSE: " + response);
                Log.d(TAG, "STATUS MESSAGE: " + this.getStatusMessage());
                this.response = response;
                if (this.getStatusMessage().contains("OK")) {
                    Log.d(TAG, "USER EXISTS SUCCESS");

                } else {
                    Log.d(TAG, "USER EXISTS FAIL");
                    String errorMsg = "Username check failed";
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }

        String getReponse(){
            return this.response;
        }

        String getStatusMessage() { return this.statusMessage; }

    }
}