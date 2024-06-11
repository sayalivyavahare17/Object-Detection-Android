package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView listView=findViewById(R.id.listview1);
        ObjectListAdapter objectListAdapter=new ObjectListAdapter(ListActivity.this,CameraActivity.objectresults);
        listView.setAdapter(objectListAdapter);

    }
}