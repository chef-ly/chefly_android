package com.se491.chef_ly.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;

import java.util.ArrayList;
import java.util.Locale;



import static android.widget.Toast.makeText;

public class GetCookingActivity extends AppCompatActivity implements GetCookingFragment.OnFragmentInteractionListener {

    private Button exit;

    private TextView stepText;
    private TextToSpeech textToSpeech;
    private ImageButton btnSpeak;
    private ViewPager pager;

    private final String TAG = "GetCookingActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String[] directions;
    private int step;
    private boolean hasDirections = false;
    private int numberSteps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cooking);

        //View Pager
        pager = (ViewPager)findViewById(R.id.viewpager);

        // Gesture Detector to repeat the current step when it detects a single tap on the screen
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(hasDirections) {
                    if (step < directions.length) {
                        read(directions[step]);
                    } else {
                        read(getResources().getString(R.string.bonAppetit));
                    }

                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }
        });

        // set on touch listener to ViewPager
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                step = position;
                updateStepText();
                read(directions[position]);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // Text view to display the current step number
        stepText = (TextView) findViewById(R.id.step) ;
        //Exit Button
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

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

                    }
                }
            }
        });


        //TODO - run recognizer from VoceRecognizer after text is done being read
        //runRecognizerSetup();
        //Toast.makeText(GetCookingActivity.this, "Starting recognizer", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        directions = i.getStringArrayExtra("directions");
        numberSteps = directions.length;
        if(numberSteps > 0){

            hasDirections = true;
            step = 0;


            updateStepText();
            ArrayList<GetCookingFragment> frags = new ArrayList<>();
            for(int j = 0; j < numberSteps; j++){
                frags.add(GetCookingFragment.newInstance(String.valueOf(j), directions[j]));
            }

            pager.setAdapter(new CookingPagerAdapter(getSupportFragmentManager(), frags));

        }
    }

    @TargetApi(21)
    @SuppressWarnings("deprecation")
    private void read(String s) {
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, "1");
        } else {
            textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }

        //create new tts listener
        ttsUtteranceListener speechListener = new ttsUtteranceListener();
        textToSpeech.setOnUtteranceProgressListener(speechListener);
    }

    private void updateStepText(){
        String sb = getResources().getString(R.string.step) +
                " " +
                String.valueOf(step + 1) +
                " " +
                getResources().getString(R.string.of) +
                " " +
                String.valueOf(numberSteps);
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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ttsUtteranceListener extends UtteranceProgressListener {

//        private List<SpeechRecognizer> recognizers;
//
//        public void addRecognizer(SpeechRecognizer rec1){
//            recognizers.add(rec1);
//        }

        @Override
        public void onDone(String ID){
            Log.e("DEBUG", "The TTS is done speaking "+ ID);
            //TODO - figure out how to call something that will start the recognizer there.
        }

        @Override
        public void onError(String ID){

        }

        @Override
        public void onStart(String ID) {
            //recognizer.stop();
            Log.e("DEBUG", "The TTS is starting to speak!");

        }
    }

    private class CookingPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<GetCookingFragment> pages;

        CookingPagerAdapter(android.support.v4.app.FragmentManager m, ArrayList<GetCookingFragment> p){
            super(m);
            pages = p;
        }
        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int position) {
            return pages.get(position);
        }


    }
}

