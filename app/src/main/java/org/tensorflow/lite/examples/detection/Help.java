package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.Locale;

public class Help extends AppCompatActivity implements RecognitionListener {

    TextView text1, text2, text3, text4, text5;
    TextToSpeech tts,textToSpeech, textToSpeech1, textToSpeech2, textToSpeech3;
    TextRecognizer textRecognizer;
    String stringResult = null;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5=findViewById(R.id.text5);

        //progressBar.setVisibility(View.VISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
       // progressBar.setIndeterminate(true);
        speech.startListening(recognizerIntent);
        //surfaceView = findViewById(R.id.camera);



        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);

                }

            }
        });


    /**    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Help.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        }, 18000);**/
    }

    @Override
    protected void onStart() {
        super.onStart();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
               if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.speak(text1.getText().toString(), TextToSpeech.QUEUE_FLUSH,null, null);

                    String text2 = "1. Say Open OCR  for text recognizer";
                    textToSpeech.speak(text2, TextToSpeech.QUEUE_ADD, null,null);
                    String text3 = "2. Say Read Document to listen recognized text";
                    textToSpeech.speak(text3, TextToSpeech.QUEUE_ADD,null, null);
                    String text4 = "3. Say Detect Object for object detection";
                    textToSpeech.speak(text4, TextToSpeech.QUEUE_ADD,null, null);
                    String text5 = "4. Press low volume button to skip or move to dashboard";
                    textToSpeech.speak(text5, TextToSpeech.QUEUE_ADD,null, null);
                }


            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if(event.getKeyCode()==KeyEvent.KEYCODE_VOLUME_DOWN)
         {
             textToSpeech.stop();
             Intent intent1=new Intent(Help.this,Dashboard.class);
             startActivity(intent1);
          //   Toast.makeText(this, "key press"+event.getKeyCode(), Toast.LENGTH_SHORT).show();
         }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
          speech.destroy();
            Log.i(LOG_TAG, "destroy");

        }


    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        //progressBar.setIndeterminate(false);
       // progressBar.setMax(10);
    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
       // progressBar.setIndeterminate(true);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
      //  returnedText.setText(errorMessage);
    }
    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }
    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";

        //returnedText.setText(text);
        //  textToSpeech.speak(returnedText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);

        if(text.contains("skip"))
        {
              textToSpeech.stop();
              Intent intent1=new Intent(Help.this,Dashboard.class);
              startActivity(intent1);

        }

        speech.startListening(recognizerIntent);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
      //  progressBar.setProgress((int) rmsdB);
    }

    public  String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        speech.startListening(recognizerIntent);
        return message;
    }
}