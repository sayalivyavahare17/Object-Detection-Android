package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    TextView text1,text2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        text1=findViewById(R.id.text1);
      //  text2=findViewById(R.id.text2);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                    textToSpeech.speak(text1.getText().toString(),TextToSpeech.QUEUE_FLUSH,null );
                }
            }
        });
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, Help.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3*1000);
    }
}