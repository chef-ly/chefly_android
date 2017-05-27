package com.se491.chef_ly.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.compat.BuildConfig;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.se491.chef_ly.R;
import com.se491.chef_ly.application.CheflyApplication;
import com.se491.chef_ly.utils.DialogPopUp;
import com.se491.chef_ly.utils.VoiceInstructionEvent;
import com.se491.chef_ly.utils.VoiceRecognizer;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

public class GetCookingActivity extends AppCompatActivity implements GetCookingFragment.OnFragmentInteractionListener {



    private TextView stepText;
    private TextToSpeech textToSpeech;
    private ViewPager pager;

    private DialogPopUp ingredientsPopup;
    private DialogPopUp directionsPopup;
    private final String TAG = "GetCookingActivity";
    //private final int REQ_CODE_SPEECH_INPUT = 100;
    private ArrayList<String> directions;
    private int step;
    private boolean hasDirections = false;
    private int numberSteps;
    private ttsUtteranceListener speechListener;

    private boolean ingredientsShowing = false;
    private boolean directionsShowing = false;

    /* Used to handle permission request from PocketSphinx*/
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private int width ;
    private int height ;


    private VoiceRecognizer voiceRec = new VoiceRecognizer(GetCookingActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cooking);

        speechListener = new ttsUtteranceListener();

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
        Button exit;
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        ImageButton btnSpeak;
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
                if(directionsPopup != null){
                    directionsPopup.show(fm, "Directions");
                    getSupportFragmentManager().executePendingTransactions();
                    if(directionsPopup.getDialog().getWindow() != null)
                        directionsPopup.getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);
                }else{
                    Log.d(TAG, "Directions pop up is null.  Directions size = " + directions.size());
                }
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            //return;
        } else {
            voiceRec.runRec();
        }
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
            ingredientsPopup = DialogPopUp.newInstance(getResources().getString(R.string.ingredients), new ArrayList<String>());
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

    @Override
    public void onPause(){
        super.onPause();
         //TODO - pause the voiceRec and TTS

        // stop voice rec
        if (voiceRec != null) {
            Log.e("DEBUG", "voiceRec is not null!");
           // voiceRec.stopRec();
        }

        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//
//        //TODO - resume voiceRec and TTS
//
//        // restart voice rec
//        if (voiceRec != null) {

//            voiceRec.startRec();
//        }
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        pager.clearOnPageChangeListeners();
        // calls cancel and shutdown on the recognizer
        voiceRec.killRec();
        voiceRec = null;
        textToSpeech.stop();
        textToSpeech.shutdown();
        speechListener= null;
        textToSpeech = null;

        // Unregister EventBus
        EventBus.getDefault().unregister(this);

        if(BuildConfig.DEBUG){
            RefWatcher refWatcher = CheflyApplication.getRefWatcher(this);
            refWatcher.watch(this);
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

    // This method will be called when the VoiceRec class sends a nextInstruction event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VoiceInstructionEvent event){
        String instruction = event.getInstruction();
        switch (instruction){
            case "next":
                updateStepText();
                pager.setCurrentItem(step+1, true);
                break;
            case "back":
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
                break;
            case "repeat":
                read(directions.get(step));
                break;
            case "ingredients":
                FragmentManager fm = getSupportFragmentManager();
                ingredientsPopup.show(fm, "Ingredients");
                ingredientsShowing = true;
                break;
            case "directions":
                fm = getSupportFragmentManager();
                directionsPopup.show(fm, "Directions");
                directionsShowing = true;
                break;
            case "question":
                //TODO - make chefly icon at bottom of screen blow to show hes listening
                break;
            case "listen":
                //btnSpeak.setImageDrawable(getResources().getDrawable(R.drawable.heartselected, getApplicationContext().getTheme()));
                //ImageView imgFp = (ImageView) findViewById(R.id.btnSpeak);
                //imgFp.setImageResource(0);
                //imgFp.setImageResource(R.drawable.heartselected);
                ImageView image = (ImageView) findViewById(R.id.btnSpeak);
                Glide.with(getApplicationContext())
                        .load(R.drawable.heartselected)
                        .asGif()
                        .crossFade()
                        .into(image);
                //((ImageView) v.findViewById(R.id.ImageView1)).setImageResource(0);
                break;
            default:
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

    private class ttsUtteranceListener extends UtteranceProgressListener {

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

            Log.e("DEBUG", "The TTS is starting to speak!");
            //btnSpeak.setImageDrawable(getResources().getDrawable(R.drawable.chefly, getApplicationContext().getTheme()));
            voiceRec.stopRec();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //runRecognizerSetup();
                voiceRec.runRec();
                Toast.makeText(this, "Thanks, now you can talk to chef.ly!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "You didn't grant chef.ly permission to use the mic.", Toast.LENGTH_LONG).show();
                finish();
            }
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

