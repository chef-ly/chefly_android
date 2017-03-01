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

public class EditActivity extends Activity {
    private TextView editIngredient;
    private TextView editDescription;
    private final String TAG = "EditActivity";
    private Button RemoveStep;
    private Button AddStep;
    private Button AddIngredientBtn;
    private Button removeIngredientBtn;
    private Button finished;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editIngredient = (TextView) findViewById(R.id.editIngredient);
        editDescription = (TextView) findViewById(R.id.EditDescription);

        RemoveStep = (Button) findViewById(R.id.RemoveStep);
        RemoveStep.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this,"Remove Step",Toast.LENGTH_LONG).show();
            }
        });
        AddStep = (Button) findViewById(R.id.AddStep);
        AddStep.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this,"New Step",Toast.LENGTH_LONG).show();
            }
        });
        AddIngredientBtn = (Button) findViewById(R.id.AddIngredientBtn);
        AddIngredientBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this,"Add Ingre",Toast.LENGTH_LONG).show();
            }
        });
        removeIngredientBtn = (Button) findViewById(R.id.removeIngredientBtn);
        removeIngredientBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this,"remove Ingre",Toast.LENGTH_LONG).show();
            }
        });

        finished = (Button) findViewById(R.id.finishedBtn);
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });


    }






}
