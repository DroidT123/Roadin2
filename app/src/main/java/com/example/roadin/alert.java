package com.example.roadin;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class alert extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        //Button for click listener
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_layout,null);


        AlertDialog.Builder builder = new AlertDialog.Builder(alert.this);

        builder.setView(view);

        builder.setTitle("ALERT");
        builder.setIcon(R.mipmap.ic_launcher);

        /*
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(alert.this,"Done",Toast.LENGTH_SHORT);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        */
        final AlertDialog dialog= builder.create();
       // builder.show();
        dialog.show();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                timer.cancel(); //this will cancel the timer of the system
            }
        }, 5000); // the timer will count 5 seconds....

    }
}
