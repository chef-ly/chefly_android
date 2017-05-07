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

    Context currentActivity;
    // TODO - THIS is a hack fix it
    public VoiceRecognizer(Context currentActivity){
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

    public void runRec(){
        Toast.makeText(currentActivity, "Starting recognizer", Toast.LENGTH_SHORT).show();
        runRecognizerSetup();
    }

    public void startRec(){
        switchSearch(KWS_SEARCH);
        Log.e("DEBUG", "Starting voice Recognizer");
    }
    public void stopRec(){
        recognizer.stop();
        Log.e("DEBUG", "Stopping voice Recognizer");
    }

    public void killRec() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
            Log.e("DEBUG", "Killed Voice Recognizer");
        }
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
                    Log.e("DEBUG", e.toString());
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(currentActivity, "Failed to init recognizer " + result, Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(currentActivity, "Recognizer initialized!", Toast.LENGTH_SHORT).show();
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }



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

        String text = hypothesis.getHypstr();

        //Toast.makeText(currentActivity, "parital "+ text, Toast.LENGTH_LONG).show();

        if (text.equals(KEYPHRASE)) {
            // Start search listening for menu options
            switchSearch(MENU_SEARCH);
            EventBus.getDefault().post(new VoiceInstructionEvent("listen"));
        }
        else if (text.equals(QUESTION_SEARCH))
            // TODO - implement getting text of the question and passing here
            switchSearch(QUESTION_SEARCH);

            //((TextView) findViewById(R.id.text)).setText(text);

    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        //((TextView) findViewById(R.id.text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();

            //here is where you should be making calls and doing stuff, not partial
            makeText(currentActivity, "Full: " + text, Toast.LENGTH_SHORT).show();

            Log.e("DEBUG", "Full Recognizer received " + text);

            if (text.equals(FORWARD)) {

                EventBus.getDefault().post(new VoiceInstructionEvent(text));
            }
            else if (text.equals(BACK)) {

                EventBus.getDefault().post(new VoiceInstructionEvent(text));

            } else if (text.equals("repeat")) {

                EventBus.getDefault().post(new VoiceInstructionEvent(text));

            } else if (text.equals("ingredients")) {
                EventBus.getDefault().post(new VoiceInstructionEvent(text));
            } else if (text.equals("directions")) {
                EventBus.getDefault().post(new VoiceInstructionEvent(text));
            } else if (text.equals(QUESTION_SEARCH)) {

                //EventBus.getDefault().post(new VoiceInstructionEvent(text));
                switchSearch(QUESTION_SEARCH);
            }


        }
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

        Log.e("DEBUG", "Starting the search "+searchName);
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else {

            recognizer.startListening(searchName, 10000);
        }
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

        // Create grammar-based search for cooking words
        File cookingGrammar = new File(assetsDir, "cooking.gram");
        recognizer.addGrammarSearch(QUESTION_SEARCH, cookingGrammar);
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
