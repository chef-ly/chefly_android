package com.se491.chef_ly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private Button signInBtn;
    private TextView continueAsGuest;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();

        // test call to get Recipe list
        //new GetRecipeList(this);
        new GetRecipe(this, 2);

    }

    private void setupViews(){

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(this);
        continueAsGuest = (TextView) findViewById(R.id.continueAsGuest);
        continueAsGuest.setOnClickListener(this);
        signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.signInBtn:

                Editable user = username.getText();
                Editable pword = password.getText();
                if(user.length() == 0){
                    Toast.makeText(this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
                }else if(pword.length() == 0){
                    Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                }else{
                    boolean isAllowed = authenticate();
                    if(isAllowed){
                        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipeListIntent.putExtra("name",user.toString());
                        startActivity(recipeListIntent);
                    }else{
                        Toast.makeText(this, "Invalid Username or password", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.continueAsGuest:
                Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                recipeListIntent.putExtra("name","guest");
                startActivity(recipeListIntent);
                break;
            case R.id.signUp:
                //TODO
                Toast.makeText(this, "Sign up clicked", Toast.LENGTH_SHORT).show();
                break;
            default:

        }
    }
    private boolean authenticate(){
        //TODO contact server and authenticate user
        return true;
    }
}


