package com.example.personalizedecommerceapp.Shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.personalizedecommerceapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static int REQUEST_LOCATION = 199;
    public GoogleApiClient mGoogleApiClient;
    public double currentLongitude;
    public double currentLatitude;
    Marker mCurrLocationMarker;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    boolean flag = true;
    RelativeLayout affectedAreaLay;
    String TAG = "RESPONSE:-";
    SearchView searchView;
    private GoogleMap mMap;
    String lat, lon;
    Button btn;
    String cordinates;
    String latlng;
    String lat1, lon1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchView = (SearchView) findViewById(R.id.idSearchView);
        btn = (Button) findViewById(R.id.submit_map);




        //details

        Intent intent = getIntent();

        if (latlng != null) {
            latlng = intent.getStringExtra("map");
            String[] latlngn = latlng.split(",");
            lat1 = latlngn[0];
            lon1 = latlngn[1];
            Log.d("lat1", latlng);
            Log.d("lng", lon1);


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            LatLng latLng = new LatLng(Double.parseDouble(lat1), Double.parseDouble(lon1));

            Log.d("latlongs", String.valueOf(latLng));
            lat = String.valueOf(latLng.latitude);
            lon = String.valueOf(latLng.longitude);
            // on below line we are adding marker to that position.

            cordinates = lat + "," + lon;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Here"));

            // below line is to animate camera to that position.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


        } else {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lat != null) {
                    Intent intent = new Intent();
                    intent.putExtra("value", cordinates);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Select Location first", Toast.LENGTH_SHORT).show();
                }

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mMap.clear();

                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    Log.d("latlongs", String.valueOf(latLng));
                    lat = String.valueOf(latLng.latitude);
                    lon = String.valueOf(latLng.longitude);
                    // on below line we are adding marker to that position.

                    cordinates = lat + "," + lon;
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();

            }
        }
        super.onStop();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (flag) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            flag = false;

            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            Log.d("loc", String.valueOf(location.getLongitude()));
            Log.d("loc", String.valueOf(location.getLatitude()));

            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());

            cordinates = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng).title("HERE!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(200);
        mLocationRequest.setFastestInterval(200);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(MapActivity.this, REQUEST_LOCATION);

                    } catch (IntentSender.SendIntentException e) {
                        Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();

            }
        } else {

            buildGoogleApiClient();

        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                googleMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("HERE!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Log.d("latlong", String.valueOf(latLng.latitude));
                lat = String.valueOf(latLng.latitude);
                lon = String.valueOf(latLng.longitude);

                cordinates = lat + "," + lon;

                Toast.makeText(MapActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();

                if (lat != null) {
                    Intent intent = new Intent();
                    intent.putExtra("value", cordinates);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Select Location first", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {


    }

    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(this, "Permission Required To Continue", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }


}