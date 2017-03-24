package com.se491.chef_ly.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;

import java.util.ArrayList;
import java.util.Locale;

public class GetCookingActivity extends Activity implements View.OnClickListener {

    private Button prev;
    private Button next;
    private Button repeat;
    private Button exit;
    private TextView text;
    private TextView stepText;
    private TextToSpeech textToSpeech;
    private ImageButton btnSpeak;

    private final String TAG = "GetCookingActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String[] directions;
    private int disabledBackground;
    private int disabledText;
    private int btnGray;
    private int white;
    private int step;
    private boolean hasDirections = false;

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

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR ) {
                    textToSpeech.setLanguage(Locale.US);

                    // Causes nullPointerException on Nexus_5_API_22
                   // Log.d(TAG, "Quality -> " + textToSpeech.getVoice().getQuality());
                    if(directions.length > 0){
                        read(directions[0]);
                    }else{
                        next.setText(getResources().getString(R.string.done));
                    }

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        directions = i.getStringArrayExtra("directions");
        if(directions.length > 0){
            hasDirections = true;
            step = 0;
            String step1 = directions[0];
            text.setText(step1);

            updateStepText();
        }



    }

    @Override
    public void onClick(View view) {

            switch(view.getId()){
                case R.id.prev:
                    if(hasDirections) {
                        next.setEnabled(true);
                        next.setClickable(true);
                        next.setBackgroundColor(btnGray);
                        next.setTextColor(white);
                        next.setText(getResources().getString(R.string.next));
                        if (step != 0) {
                            step--;
                            updateStepText();
                            String prevStep = directions[step];
                            text.setText(prevStep);
                            read(prevStep);
                            if (step == 0) {
                                prev.setClickable(false);
                                prev.setEnabled(false);
                                prev.setBackgroundColor(disabledBackground);
                                prev.setTextColor(disabledText);
                            }
                        }
                        break;
                    }
                case R.id.next:
                    if(hasDirections) {
                        prev.setEnabled(true);
                        prev.setClickable(true);
                        prev.setBackgroundColor(btnGray);
                        prev.setTextColor(white);
                        step++;
                        if (step < directions.length) {
                            updateStepText();
                            String nextStep = directions[step];
                            text.setText(nextStep);
                            read(nextStep);
                        } else if (step == directions.length) {
                            String t = getResources().getString(R.string.bonappetit);
                            text.setText(t);
                            read(t);
                            next.setText(getResources().getString(R.string.done));
                        } else {
                            setResult(RESULT_OK);
                            finish();
                            break;
                        }
                        break;
                    }else{
                        next.setText(getResources().getString(R.string.done));
                    }
                case R.id.repeat:
                    if(hasDirections) {
                        if (step < directions.length) {
                            read(directions[step]);
                        } else {
                            read(getResources().getString(R.string.bonappetit));
                        }

                        break;
                    }
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
    private static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private void updateStepText(){
        String sb = getResources().getString(R.string.step) +
                " " +
                String.valueOf(step + 1) +
                " " +
                getResources().getString(R.string.of) +
                " " +
                String.valueOf(directions.length);
        stepText.setText(sb);

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Toast.makeText(this, result.get(0), Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }
}