package com.se491.chef_ly.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static android.widget.Toast.makeText;

public class GetCookingActivity extends Activity implements View.OnClickListener, RecognitionListener {

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

        //Pocketsphinx vars and code
        // Prepare the data for UI
        captions = new HashMap<String, Integer>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(FORWARD, R.string.forward_caption);
        captions.put(BACK, R.string.back_caption);
        captions.put(QUESTION_SEARCH, R.string.question_caption);

        //((TextView) findViewById(R.id.text))
        //        .setText("Preparing the recognizer");

        Toast.makeText(this, "Preparing the recognizer", Toast.LENGTH_LONG).show();

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }



        // -- end pocketsphinx

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR ) {
                    textToSpeech.setLanguage(Locale.US);

                    // Causes nullPointerException on Nexus_5_API_22
                   // Log.d(TAG, "Quality -> " + textToSpeech.getVoice().getQuality());
                    if(directions.length > 0){
                        //read(directions[0]);
                    }else{
                        next.setText(getResources().getString(R.string.done));
                    }

                }
            }
        });


        //TODO - run recognizer after text is done being read
        runRecognizerSetup();
    }

    // --- PocketSphinx functions
    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(GetCookingActivity.this);
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
                    ((TextView) findViewById(R.id.text))
                            .setText("Failed to init recognizer " + result);
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runRecognizerSetup();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

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
            Toast.makeText(this, "parital "+ text, Toast.LENGTH_LONG).show();
            //next.performClick();
            switchSearch(KWS_SEARCH);
        }
        else if (text.equals(BACK)) {

            prev.performClick();
            switchSearch(KWS_SEARCH);
        }
        else if (text.equals(QUESTION_SEARCH))
            // TODO - implement getting text of the question and passing here
            switchSearch(QUESTION_SEARCH);
        else
            //((TextView) findViewById(R.id.text)).setText(text);
            // TODO- dont show text from here.  ONly show text from Final results
            Toast.makeText(this, "parital "+ text, Toast.LENGTH_LONG).show();
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
            makeText(getApplicationContext(), "full " + text, Toast.LENGTH_SHORT).show();

            if (text.equals(FORWARD)) {
                //TODO - DONT make calls here in partial

                next.performClick();
                switchSearch(KWS_SEARCH);
            }
            else if (text.equals(BACK)) {

                prev.performClick();
                switchSearch(KWS_SEARCH);
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

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
        //((TextView) findViewById(R.id.text)).setText(caption);
        Toast.makeText(this, caption, Toast.LENGTH_LONG).show();
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
        ((TextView) findViewById(R.id.text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }


    // -- END POCKETSPHINX FUNCTIONS

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
                            String t = getResources().getString(R.string.bonAppetit);
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
                            read(getResources().getString(R.string.bonAppetit));
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