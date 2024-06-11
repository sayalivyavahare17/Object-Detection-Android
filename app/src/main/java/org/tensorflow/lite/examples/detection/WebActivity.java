package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent=WebActivity.this.getIntent();
        String s1 = intent.getStringExtra("object");

        WebView web1= findViewById(R.id.web1);
        web1.loadUrl("https://www.google.com/search?q="+s1);
      //  web1.loadUrl("https://www.google.com/");

    }
}