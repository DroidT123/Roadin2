package com.example.roadin;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MFragment extends Fragment implements OnMapReadyCallback {


    //GoogleMap map;
    private static final String TAG = "DDDDDDDDDDDDDDDDD";
    private String sign = "";
    private String sval = "";
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    //   private LocationRequest mLocationRequest;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 19;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;


    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;       //1 sec
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;



    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    /**
     * Tracks the status of the location updates request.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Variables For Firebase Database @ref  and GeoFire @geoFire
     */
    DatabaseReference ref;
    private DatabaseReference mDatabase;
    GeoFire geoFire;

    public MFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_m, container, false);


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        mSettingsClient = LocationServices.getSettingsClient(this.getActivity());

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);

                        // <<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>
        ref = FirebaseDatabase.getInstance().getReference("Markers");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Demo");
        geoFire = new GeoFire(ref);
        TextView msgView = (TextView)view.findViewById(R.id.tbox);
                             // <<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>
    }

                     // <<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>

    public void geoFireExample()
    {
        double lat = mCurrentLocation.getLatitude();
        double lng = mCurrentLocation.getLongitude();

        //Update loc to firebase
        /*
        geoFire.setLocation("You", new GeoLocation(lat, lng), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {

            }
        });

    */

         final Snackbar snackbar1 = Snackbar
                .make(getView().findViewById(R.id.mapfrag), "Demo Location Entered.", Snackbar.LENGTH_LONG);


        LatLng myLoc = new LatLng(lat,lng);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(myLoc.latitude,myLoc.longitude),0.01f);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                //snackbar1.show();
                showAlert();
                Log.i("INSIDEEEEEE","WORKING");

            }

            @Override
            public void onKeyExited(String key) {
                Snackbar snackbar2 = Snackbar
                        .make(getView().findViewById(R.id.mapfrag), "Demo Location Exited.", Snackbar.LENGTH_LONG);

                snackbar2.show();
                Log.i("OUTSIDE","WORKING");
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.i("DEMOLOC","MOVED INSIDE");

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR_DLOC",""+error);

            }
        });


    }
                                 // <<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>

    // ----------------ALERT BUILDER -----------------------------//
    public void showAlert() {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_layout,null);
        TextView msgView = (TextView)view.findViewById(R.id.tbox);


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setView(view);

        builder.setTitle("ALERT");

        String uri = "@drawable/img"+sval;  // where myresource (without the extension) is the file
        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        ImageView imageSign = (ImageView) view.findViewById(R.id.imageView2);
        Drawable res = getResources().getDrawable(imageResource);
        imageSign.setImageDrawable(res);

       // builder.setMessage(sign);
        msgView.setText(sign);
        builder.setIcon(R.mipmap.ic_launcher);
        final AlertDialog dialog= builder.create();
        // builder.show();
        dialog.show();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                timer.cancel(); //this will cancel the timer of the system
            }
        }, 10000); // the timer will count 10 seconds....

    }


    // ----------------ALERT BUILDER -----------------------------//





    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            getLocationPermission();

        } else {
            getDeviceLocation();
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            //outState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;


            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setBuildingsEnabled(false);
        mMap.setMinZoomPreference(12f);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        if (!(mLocationPermissionGranted)) {

            Snackbar snackbar = Snackbar
                    .make(getView().findViewById(R.id.mapfrag), "Location Permisson Needed", Snackbar.LENGTH_INDEFINITE);

            snackbar.show();
        }

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.child("key").getValue(String.class);
                    Double lat = ds.child("lat").getValue(Double.TYPE);
                    Double lng = ds.child("lon").getValue(Double.TYPE);
                    sign = ds.child("sign").getValue(String.class);
                    sval = ds.child("sval").getValue(String.class);

                   // Log.i(TAG,sval);


                 //   String str = key+""+lat+" "+lng+"";
                  //  Log.d("TAG", key + "  " + lat + "" +lng);
                    LatLng marker = new LatLng(lat,lng);
                    mMap.addCircle(new CircleOptions()
                            .center(marker)
                            .radius(5)
                            .strokeColor(Color.BLUE)
                            .fillColor(0x220000FF)
                            .strokeWidth(5.0f));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);


        // **************<<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>**********************************
        /*
        LatLng demoLoc = new LatLng(40.7451,-73.9887);
        mMap.addCircle(new CircleOptions()
                        .center(demoLoc)
                        .radius(20)
                        .strokeColor(Color.BLUE)
                        .fillColor(0x220000FF)
                        .strokeWidth(5.0f));
*/
        /*
        if(mCurrentLocation != null) {

            LatLng demoLoc = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(demoLoc.latitude, demoLoc.longitude), 0.05f);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    Snackbar snackbar1 = Snackbar
                            .make(getView().findViewById(R.id.mapfrag), "Demo Location Entered.", Snackbar.LENGTH_INDEFINITE);

                    snackbar1.show();
                    Log.i("INSIDEEEEEE", "WORKING");

                }

                @Override
                public void onKeyExited(String key) {
                    Snackbar snackbar1 = Snackbar
                            .make(getView().findViewById(R.id.mapfrag), "Demo Location Exited.", Snackbar.LENGTH_LONG);

                    snackbar1.show();
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    Log.i("DEMOLOC", "MOVED INSIDE");

                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    Log.e("ERROR_DLOC", "" + error);

                }
            });
        }
        */
        // **************<<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>**********************************

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
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
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
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
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateLocationUI();
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
        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    //---------------------------------------------------------------------------------------------------//

    /* These settings are appropriate for mapping applications that show real-time location
    * updates.
    */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();


        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                updateUI();
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission

                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this.getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MFragment.this.getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                //mRequestingLocationUpdates = false;
                        }

                        updateUI();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        mRequestingLocationUpdates = true ;

        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
           getLocationPermission();
        }

        updateUI();
    }



    @Override
    public void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                      //  setButtonsEnabledState();
                    }
                });
    }

    private void updateUI() {

        Location location = mCurrentLocation;
        updateLocationUI();
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(mMap.getCameraPosition().target)
                    .zoom(DEFAULT_ZOOM)
                    .bearing(30)
                    .tilt(45)
                    .build()));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        // **************<<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>**********************************
        if (mCurrentLocation != null) {

            geoFireExample();
        }

        // **************<<<<<<<<<<<<<<<<<<<<TEST >>>>>>>>>>>>>>>>>>**********************************
    }


}
