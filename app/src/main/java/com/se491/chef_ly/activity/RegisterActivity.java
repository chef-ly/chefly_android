package com.se491.chef_ly.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.text.InputType;
import android.widget.Toast;

import com.se491.chef_ly.R;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private final String TAG = "RegisterActivity";

    private Button nextButton;
    private TextView instruction;
    private EditText input;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nextButton = (Button) findViewById(R.id.registerButton);
        nextButton.setOnClickListener(this);
        instruction = (TextView) findViewById(R.id.registerInstruction);
        input = (EditText) findViewById(R.id.registerInput);
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
                if (registerNewUser()) {
                    // Direct user to list view
                    Intent recipeListIntent = new Intent(RegisterActivity.this, RecipeListActivity.class);
                    recipeListIntent.putExtra("name", username);
                    startActivity(recipeListIntent);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "There was a problem while registering your account", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Your passwords did not match. Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            // TODO error handler......
        }
    }

    private boolean registerNewUser() {
        return false; // TODO send http request to register new user
    }
}
