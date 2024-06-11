package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.util.ArrayList;

public class ObjectListAdapter extends ArrayAdapter<String>
{

    ArrayList<Detector.Recognition> results;
    Activity context;
    public ObjectListAdapter(Activity context, ArrayList<Detector.Recognition> results) {
        super(context, R.layout.list_object);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.results=results;

    }

    @Override
    public int getCount() {
        return results.size();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_object, null,true);



        TextView txtname = (TextView) rowView.findViewById(R.id.txtname);
        CardView cardView=(CardView)rowView.findViewById(R.id.card1);
        Button btgoogle=(Button)rowView.findViewById(R.id.btgoogle);
        Button bt3d=(Button)rowView.findViewById(R.id.bt3d);


        Detector.Recognition result =results.get(position);

        txtname.setText(result.getTitle()+" Confidence:"+result.getConfidence());
/*
        btgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+result.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("object",result.getTitle());
                context.startActivity(intent);

            }
        });

        bt3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+result.getTitle(), Toast.LENGTH_SHORT).show();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://clara.io/library?gameCheck=true&public=true&query="+result.getTitle()));
                context.startActivity(browserIntent);

            }
        });
*/
        return rowView;

    };

}
