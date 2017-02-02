package com.se491.chef_ly.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.se491.chef_ly.R;

import java.util.Locale;

public class GetCookingActivity extends Activity implements View.OnClickListener {

    private Button prev;
    private Button next;
    private Button repeat;
    private Button exit;
    private TextView text;
    private TextView stepText;
    private TextToSpeech textToSpeech;

    private String[] directions;
    private int disabledBackground;
    private int disabledText;
    private int btnGray;
    private int white;
    private int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cooking);

        disabledBackground = getColor(this, R.color.disabledBtn);
        disabledText = getColor(this, R.color.disableText);
        btnGray = getColor(this, R.color.btnGray);
        white = getColor(this, R.color.white);

        prev = (Button) findViewById(R.id.prev);
        prev.setOnClickListener(this);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        repeat = (Button) findViewById(R.id.repeat);
        repeat.setOnClickListener(this);
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(this);
        text = (TextView) findViewById(R.id.text);
        stepText = (TextView) findViewById(R.id.step);

        prev.setClickable(false);
        prev.setEnabled(false);
        prev.setBackgroundColor(disabledBackground);
        prev.setTextColor(disabledText);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                    read(directions[0]);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        directions = i.getStringArrayExtra("directions");

        step = 0;
        String step1 = directions[0];
        text.setText(step1);

        updateStepText();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.prev:
                next.setEnabled(true);
                next.setClickable(true);
                next.setBackgroundColor(btnGray);
                next.setTextColor(white);
                if(step != 0){
                    step--;
                    updateStepText();
                    String prevStep = directions[step];
                    text.setText(prevStep);
                    read(prevStep);
                    if(step == 0){
                        prev.setClickable(false);
                        prev.setEnabled(false);
                        prev.setBackgroundColor(disabledBackground);
                        prev.setTextColor(disabledText);
                    }
                }
                break;
            case R.id.next:
                prev.setEnabled(true);
                prev.setClickable(true);
                prev.setBackgroundColor(btnGray);
                prev.setTextColor(white);
                if(step < directions.length-1){
                    step++;
                    updateStepText();
                    String nextStep = directions[step];
                    text.setText(nextStep);
                    read(nextStep);
                }else{
                    String t = getResources().getString(R.string.bonappetit);
                    text.setText(t);
                    read(t);
                    next.setClickable(false);
                    next.setEnabled(false);
                    next.setBackgroundColor(disabledBackground);
                    next.setTextColor(disabledText);
                }
                break;
            case R.id.repeat:
                if(step < directions.length-1){
                    read(directions[step]);
                }else{
                    read(getResources().getString(R.string.bonappetit));
                }

                break;
            case R.id.exit:
                setResult(RESULT_OK);
                finish();
                break;
            default:
                //TODO

        }
    }
    @TargetApi(21)
    @SuppressWarnings("deprecation")
    private void read(String s){
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH, null, "1");
        } else {
            textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH, null);
        }



    }
    @TargetApi(23)
    @SuppressWarnings("deprecation")
    private static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private void updateStepText(){
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.step));
        sb.append(" ");
        sb.append(String.valueOf(step+1));
        sb.append(" ");
        sb.append(getResources().getString(R.string.of));
        sb.append(" ");
        sb.append(String.valueOf(directions.length));
        stepText.setText(sb.toString());

    }
}
