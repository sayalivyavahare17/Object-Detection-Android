package org.tensorflow.lite.examples.detection;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class OCR extends AppCompatActivity implements RecognitionListener {
    Button btn_recognized;
    TextView tv_text,returnedText;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextRecognizer textRecognizer;
    String stringResult = null;
    TextToSpeech textToSpeech;

    private ProgressBar progressBar;
    ImageView imageView1;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);


        btn_recognized = findViewById(R.id.btn_recognized);
        tv_text = findViewById(R.id.tv_text);

        returnedText = (TextView) findViewById(R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.VISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        progressBar.setIndeterminate(true);
        speech.startListening(recognizerIntent);
        surfaceView = findViewById(R.id.camera);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });


        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PackageManager.PERMISSION_GRANTED);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {


            }
        });

     startCameraSource();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            textToSpeech.stop();

            //   Toast.makeText(this, "key press"+event.getKeyCode(), Toast.LENGTH_SHORT).show();
        }
        if(event.getKeyCode()==KeyEvent.KEYCODE_VOLUME_UP)
        {

            textToSpeech.speak(tv_text.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,null);

            //   Toast.makeText(this, "key press"+event.getKeyCode(), Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
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

        returnedText.setText(text);
        //  textToSpeech.speak(returnedText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);

        if(text.contains("read document")||text.contains("red document")||text.contains("read a document")||text.contains("speak"))
        {
            Toast.makeText(this, "reading text ..", Toast.LENGTH_SHORT).show();
            textToSpeech.speak(tv_text.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,null);

           // TextRecognizer();

         // startCameraSource();
          //  Intent intent=new Intent(OCR.this,OCR.class);
          //  startActivity(intent);
            // Intent intent =new Intent(MainActivity.this,Assistant.class);
            // startActivity(intent);
        }

        speech.startListening(recognizerIntent);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
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
    protected void onDestroy() {

        super.onDestroy();
        cameraSource.release();
    }

    private void TextRecognizer() {
        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setRequestedPreviewSize(2500, 2500).build();
        //.setRequestedPreviewSize(1280, 1024).build();
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(OCR.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                SparseArray<TextBlock> sparseArray=detections.getDetectedItems();
                StringBuilder stringBuilder=new StringBuilder();

                for(int i=0; i<sparseArray.size();++i)
                {
                    TextBlock textBlock=sparseArray.valueAt(i);
                    if (textBlock!=null && textBlock.getValue()!=null)
                    {
                        stringBuilder.append(textBlock.getValue()+" ");

                    }
                }

                final String stringText=stringBuilder.toString();
                Handler handler=new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        stringResult=stringText;
                        resultObtained();
                    }
                });
            }
        });
    }
    private void resultObtained()
    {
        setContentView(R.layout.activity_dashboard);
        tv_text.setText(stringResult);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           // textToSpeech.speak(stringResult,TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }




    private void startCameraSource(){
        final TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()){
            Log.w("Tag" ,"Dependencies not loaded yet");
        }
        else {
            cameraSource=new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1280,1024)
                    .setAutoFocusEnabled(true).setRequestedFps(2.0f).build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                    try{
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(OCR.this,new String[]{Manifest.permission.CAMERA},100);
                            return;
                        }
                        cameraSource.start(surfaceView.getHolder());
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                    //detect all text from camera
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items= detections.getDetectedItems();
                    if (items.size()!=0)
                    {
                        tv_text.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder=new StringBuilder();
                                for (int i=0;i<items.size();i++)
                                {
                                    TextBlock item=items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");

                                }
                                tv_text.setText(stringBuilder);
                            }
                        });
                    }
                }
            });
        }

    }
}