package com.se491.chef_ly.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.utils.DialogPopUp;
import com.se491.chef_ly.utils.VoiceInstructionEvent;
import com.se491.chef_ly.utils.VoiceRecognizer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Locale;
import java.text.BreakIterator;
import java.util.zip.Inflater;

public class GetCookingActivity extends AppCompatActivity implements GetCookingFragment.OnFragmentInteractionListener {

    private Button exit;

    private TextView stepText;
    private TextToSpeech textToSpeech;
    private ImageButton btnSpeak;
    private ViewPager pager;

    private DialogPopUp ingredientsPopup;
    private DialogPopUp directionsPopup;
    private final String TAG = "GetCookingActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ArrayList<String> directions;
    private int step;
    private boolean hasDirections = false;
    private int numberSteps;


    private boolean ingredientsShowing = false;
    private boolean directionsShowing = false;


    private int width ;
    private int height ;


    private VoiceRecognizer voiceRec = new VoiceRecognizer(GetCookingActivity.this);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cooking);


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        //View Pager
        pager = (ViewPager)findViewById(R.id.viewpager);

        // Gesture Detector to repeat the current step when it detects a single tap on the screen
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(hasDirections) {
                    if (step < directions.size()) {
                        read(directions.get(step));
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
                read(directions.get(position));
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

            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR ) {
                    textToSpeech.setLanguage(Locale.US);

                    // Causes nullPointerException on Nexus_5_API_22
                   // Log.d(TAG, "Quality -> " + textToSpeech.getVoice().getQuality());
                    if(directions.size() > 0){
                        read(directions.get(0));

                    }
                }
            }
        });


        // TODO replace with speech recognizer
        Button ingredBtn = (Button) findViewById(R.id.ingredientsBtn);
        ingredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ingredientsPopup.show(fm, "Ingredients");
                getSupportFragmentManager().executePendingTransactions();
                if(ingredientsPopup.getDialog().getWindow() != null)
                    ingredientsPopup.getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);
            }
        });

        // TODO replace with speech recognizer
        Button direcBtn = (Button) findViewById(R.id.directionsBtn);
        direcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                directionsPopup.show(fm, "Directions");
                getSupportFragmentManager().executePendingTransactions();
                if(directionsPopup.getDialog().getWindow() != null)
                    directionsPopup.getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);

            }
        });

        voiceRec.runRec();
    }

    // TODO - add onPause and onResume where the speech and recognizer are stopped

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        //  Get ingredient list for popup
        ArrayList<String> ingredients = i.getStringArrayListExtra("ingredients");
        if(ingredients != null){
            ingredientsPopup = DialogPopUp.newInstance(getResources().getString(R.string.ingredients), ingredients);
            ingredientsPopup.showTitle(false);


        }else{
            DialogPopUp ingredientsPopup = DialogPopUp.newInstance(getResources().getString(R.string.ingredients), new ArrayList<String>());
        }

        // Split up the directions to more "digestible" (wink wink) pieces
        String[] rawDirections = i.getStringArrayExtra("directions");

        directions = new ArrayList<>();

        for (int x = 0; x < rawDirections.length; x++) {
            BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
            String source = rawDirections[x];
            iterator.setText(source);
            int start = iterator.first();
            for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                directions.add(source.substring(start,end));
            }
        }

        numberSteps = directions.size();
        if(numberSteps > 0){
            directionsPopup = DialogPopUp.newInstance(getResources().getString(R.string.directions), directions);
            directionsPopup.showTitle(false);

            hasDirections = true;

            updateStepText();
            ArrayList<GetCookingFragment> frags = new ArrayList<>();
            for(int j = 0; j < numberSteps; j++){
                frags.add(GetCookingFragment.newInstance(String.valueOf(j), directions.get(j)));
            }

            pager.setAdapter(new CookingPagerAdapter(getSupportFragmentManager(), frags));
            Log.d(TAG, "Step ----> " + step);
            pager.setCurrentItem(step);

        }
        // register with EventBus to get events from VoiceRec
        EventBus.getDefault().register(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();

        // Unregister EventBus
        EventBus.getDefault().unregister(this);

        Log.d(TAG,"OnDestroy <><><><><><><><><><><>");
    }

    // This method will be called when the VoiceRec class sends a nextInstruction event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VoiceInstructionEvent event){

        if (event.getInstruction().equals("next")){
            updateStepText();
            pager.setCurrentItem(step+1, true);

        } else if (event.getInstruction().equals("back")) {

            if (ingredientsShowing){
                ingredientsPopup.dismiss();
                //directionsPopup.dismiss();
                ingredientsShowing = false;
            }else if (directionsShowing) {
                directionsPopup.dismiss();
                directionsShowing = false;
            }
            else {

                updateStepText();
                pager.setCurrentItem(step - 1, true);
            }


        } else if (event.getInstruction().equals("repeat")) {
            read(directions.get(step));

        } else if (event.getInstruction().equals("ingredients")) {
            FragmentManager fm = getSupportFragmentManager();
            ingredientsPopup.show(fm, "Ingredients");
            ingredientsShowing = true;
        } else if (event.getInstruction().equals("directions")) {
            FragmentManager fm = getSupportFragmentManager();
            directionsPopup.show(fm, "Directions");
            directionsShowing = true;
        } else if (event.getInstruction().equals("question")){
            //TODO - make chefly icon at bottom of screen blow to show hes listening

        }
        else {
                Toast.makeText(this, "Can you please say that again?", Toast.LENGTH_LONG).show();
        }


        Log.e("DEBUG", "Received VoiceInstructionEvent " + event.getInstruction());
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

        @Override
        public void onDone(String ID){
            Log.e("DEBUG", "The TTS is done speaking "+ ID);

            voiceRec.startRec();
        }

        @Override
        public void onError(String ID){

        }

        @Override
        public void onStart(String ID) {
            //recognizer.stop();
            Log.e("DEBUG", "The TTS is starting to speak!");
            voiceRec.stopRec();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

