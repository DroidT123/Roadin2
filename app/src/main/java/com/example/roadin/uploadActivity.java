package com.example.roadin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import com.coderzpassion.firebasesample.model.User;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class uploadActivity extends AppCompatActivity {
    EditText loc, desc1, desc2;
    Button save;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseError databaseError;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loc = (EditText) findViewById(R.id.loc);
        desc1 = (EditText) findViewById(R.id.desc1);
        desc2 = (EditText) findViewById(R.id.desc2);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = loc.getText().toString().trim();
                String description1 = desc1.getText().toString().trim();
                String description2 = desc2.getText().toString().trim();
                String detail=location+description1+description2;

                if (mAuth.getCurrentUser() != null) {

                    mDatabase.child("Details").setValue(detail, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(uploadActivity.this, "Data is saved successfully",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }

        });
    }
}




