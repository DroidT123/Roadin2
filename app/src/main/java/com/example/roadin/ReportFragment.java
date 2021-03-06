package com.example.roadin;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {


    public ReportFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "Report_Frag";

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private String mLatitudeLabel = "LAT";
    private String mLongitudeLabel = "LNG";

    Button submit;
    EditText desc1,desc2;
    FirebaseDatabase mdatabase;
    DatabaseReference ref;
    //details details;
    String pid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        // Prompt the user for permission.
        getLocationPermission();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        mLatitudeText = (TextView) view.findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) view.findViewById((R.id.longitude_text));
        submit=(Button) view.findViewById(R.id.button4) ;
        desc1=(EditText) view.findViewById(R.id.desc1) ;
        desc2=(EditText) view.findViewById(R.id.desc2) ;
        mdatabase = FirebaseDatabase.getInstance();
        ref = mdatabase.getReference("details");
        //details = new details();

        submit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                getValues();
                //Intent myIntent = new Intent(MainActivity.this,
                //  NewActivity.class);
                //startActivity(myIntent);
            }
        });

    }

    private void getValues()
    {
        String d1 = desc1.getText().toString().trim();
        String d2 = desc2.getText().toString().trim();
        String l1=mLatitudeText.getText().toString();
        String l2=mLongitudeText.getText().toString();
        Double d3 = Double.parseDouble(l1);
        Double d4 = Double.parseDouble(l2);
        //Toast.makeText(getContext(),d1, Toast.LENGTH_SHORT).show();
        if (!(TextUtils.isEmpty(d1))||!(TextUtils.isEmpty(d2))) {
            mdatabase = FirebaseDatabase.getInstance();
            ref = mdatabase.getReference("details");
            pid = ref.push().getKey();
           // Toast.makeText(getContext(), pid.toString(), Toast.LENGTH_SHORT).show();
            Map details = new HashMap();
            details.put("lat",d3);
            details.put("lon",d4);
            details.put("place",d1);
            details.put("desc",d2);
            ref.child(pid).setValue(details);
            Toast.makeText(this.getActivity(), "data uploaded", Toast.LENGTH_SHORT).show();
            /* startActivity(new Intent(MainActivity.this,New_Activity.class)); */
        } else {
            Toast.makeText(this.getActivity(), "you should enter data in all fields", Toast.LENGTH_SHORT).show();
        }


    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this.getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            mLatitudeText.setText(String.format(Locale.ENGLISH, "%f",
                                    mLastKnownLocation.getLatitude()));
                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%f",
                                    mLastKnownLocation.getLongitude()));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }



}
