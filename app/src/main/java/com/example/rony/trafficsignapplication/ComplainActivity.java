package com.example.rony.trafficsignapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class ComplainActivity extends AppCompatActivity {

    TextView position;
    EditText Text;
    private Intent i= getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        String pos=i.getStringExtra("Position");

        position= (TextView) findViewById(R.id.textView);
        Text= (EditText) findViewById(R.id.editText);
        position.setText(pos);
    }

    public void onSubmit(View view) {
        String str=Text.getText().toString();
        Toast.makeText(ComplainActivity.this,str,Toast.LENGTH_LONG).show();
    }
}
