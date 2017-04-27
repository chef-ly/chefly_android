package com.se491.chef_ly.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.view.View;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.GetCookingActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static android.widget.Toast.makeText;
import static com.se491.chef_ly.R.id.pager;

/**
 * Created by Wolf on 4/14/2017.
 */

public class VoiceRecognizer implements RecognitionListener {

    Activity currentActivity;
    // TODO - THIS is a hack fix it
    public VoiceRecognizer(Activity currentActivity){
        this.currentActivity = currentActivity;
    }
    // PocketSphinx vars
    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";
    private static final String FORWARD = "next";
    private static final String BACK = "back";
    private static final String QUESTION_SEARCH = "question";

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "hey chef";

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;


    //TODO - captions hashmap in onCreate//Pocketsphinx vars and code
    /*******************************
    // Prepare the data for UI
    captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(FORWARD, R.string.forward_caption);
        captions.put(BACK, R.string.back_caption);
        captions.put(QUESTION_SEARCH, R.string.question_caption);

    //((TextView) findViewById(R.id.text))
    //        .setText("Preparing the recognizer");

        Toast.makeText(this, "Preparing the recognizer", Toast.LENGTH_LONG).show();



    }
     *****************/

    public void checkAudioPermission(Activity currentActivity) {
        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
    }

    public void runRec(){
        Toast.makeText(currentActivity, "Starting recognizer", Toast.LENGTH_SHORT).show();
        runRecognizerSetup();
    }

    public void startRec(){
        switchSearch(KWS_SEARCH);
    }
    public void stopRec(){
        recognizer.stop();
    }

    // --- PocketSphinx functions
    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(currentActivity);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(currentActivity, "Failed to init recognizer " + result, Toast.LENGTH_SHORT).show();
                } else {
                    //TODO - move to the listener for textToSpeech to be done
                    Toast.makeText(currentActivity, "Recognizer initialized!", Toast.LENGTH_SHORT).show();
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }
//TODO - fix this
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                runRecognizerSetup();
//            } else {
//                finish();
//            }
//        }
//    }

    //TODO - fix this.  Do we need to destroy this?
//    @Override
//    public void onDestroy() {
//        ////super.onDestroy();
//
//        if (recognizer != null) {
//            recognizer.cancel();
//            recognizer.shutdown();
//        }
//    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;


        // TODO - this is where the implementation happens
        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE))
            switchSearch(MENU_SEARCH);
        else if (text.equals(FORWARD)) {
            //TODO - DONT make calls here in partial
            Toast.makeText(currentActivity, "parital "+ text, Toast.LENGTH_LONG).show();
            //next.performClick();
            switchSearch(KWS_SEARCH);
        }
        else if (text.equals(BACK)) {

            //prev.performClick();
            switchSearch(KWS_SEARCH);
        }
        else if (text.equals(QUESTION_SEARCH))
            // TODO - implement getting text of the question and passing here
            switchSearch(QUESTION_SEARCH);
        else
            //((TextView) findViewById(R.id.text)).setText(text);
            // TODO- dont show text from here.  ONly show text from Final results
            Toast.makeText(currentActivity, "PARTIAL "+ text, Toast.LENGTH_LONG).show();
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        //((TextView) findViewById(R.id.text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            //TODO - here is where you should be making calls and doing stuff, not partial
            makeText(currentActivity, "Full: " + text, Toast.LENGTH_SHORT).show();

            if (text.equals(FORWARD)) {
                //TODO - DONT make calls here

                //next.performClick();
                Log.e("DEBUG","Recognizer received NEXT");
                EventBus.getDefault().post(new VoiceInstructionEvent(true));


                switchSearch(KWS_SEARCH);
            }
            else if (text.equals(BACK)) {

                //prev.performClick();
                switchSearch(KWS_SEARCH);
            }

        }
    }

    public void sendSwipeEvent(){
        Log.e("DEBUG", "Sending Swipe");
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis()+ 10;
        float x = 0.0f;
        float y = 0.0f;
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.AXIS_HSCROLL,
                x,
                y,
                metaState);

        currentActivity.findViewById(R.id.viewpager).dispatchTouchEvent(motionEvent);
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        //TODO - fix get recsources()
        //String caption = currentActivity.getString(captions.get(searchName));
        //((TextView) findViewById(R.id.text)).setText(caption);
        //Toast.makeText(currentActivity, "Search Started", Toast.LENGTH_LONG).show();
    }

    public void initSwitchSearch(){
        switchSearch(KWS_SEARCH);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // TODO - do we need these defined as grammar searches? NO?
        // Create grammar-based search for digit recognition
        //File digitsGrammar = new File(assetsDir, "digits.gram");
        //recognizer.addGrammarSearch(FORWARD, digitsGrammar);

        // Create grammar-based search for digit recognition
        //File digits2Grammar = new File(assetsDir, "digits.gram");
        //recognizer.addGrammarSearch(BACK, digits2Grammar);

        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(QUESTION_SEARCH, languageModel);
    }

    @Override
    public void onError(Exception error) {
        Toast.makeText(currentActivity, "There was an error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }


    // -- END POCKETSPHINX FUNCTIONS
    // -- end pocketsphinx
}
