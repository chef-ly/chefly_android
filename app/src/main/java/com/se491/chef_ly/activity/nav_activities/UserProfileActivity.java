package com.se491.chef_ly.activity.nav_activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.utils.CredentialsManager;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView userImage;
    private EditText userName;
    private TextView email;

    private Drawable editTextBackground;
    private final int GET_IMAGE = 2012;
    private boolean editUserName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Button editImage;
        Button editName;
        Button updateLogin;

        editImage = (Button) findViewById(R.id.editImage);
        editImage.setOnClickListener(this);
        editName = (Button) findViewById(R.id.editUserName);
        editName.setOnClickListener(this);
        updateLogin = (Button) findViewById(R.id.updateLogin);
        updateLogin.setOnClickListener(this);

        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (EditText) findViewById(R.id.userName);
        userName.setCursorVisible(false);
        userName.setFocusable(false);
        userName.setFocusableInTouchMode(false);
        userName.setClickable(false);
        userName.setInputType(InputType.TYPE_NULL);
        userName.clearFocus();
        userName.setText(CredentialsManager.getUsername(this));
        editTextBackground = userName.getBackground();
        userName.setBackgroundResource(R.color.transparent);

        email = (TextView) findViewById(R.id.email);
        email.setText(CredentialsManager.getUsername(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_IMAGE){
            if(resultCode == Activity.RESULT_OK){
                Uri selectedImage = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    userImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(this, "Failed to load image, please try again", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Failed to load image, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editImage:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_IMAGE);
                break;
            case R.id.editUserName:
                Toast.makeText(this, "Edit user name -> " + editUserName, Toast.LENGTH_SHORT).show();
                if(!editUserName){
                    userName.setCursorVisible(true);
                    userName.setFocusable(true);
                    userName.setFocusableInTouchMode(true);
                    userName.setClickable(true);
                    userName.setInputType(InputType.TYPE_CLASS_TEXT);
                    userName.requestFocus(); //to trigger the soft input
                    userName.setBackground(editTextBackground);
                    editUserName = true;

                    //TODO - store new name and send to server

                }else{
                    userName.setCursorVisible(false);
                    userName.setFocusable(false);
                    userName.setFocusableInTouchMode(false);
                    userName.setClickable(false);
                    userName.setInputType(InputType.TYPE_NULL);
                    userName.clearFocus();
                    userName.setBackgroundResource(R.color.transparent);
                    editUserName = false;

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow((findViewById(R.id.UserProfileActivity)).getWindowToken(), 0);
                }

                break;
            case R.id.updateLogin:
                //TODO update user email & password

        }


    }
}
