package com.example.personalizedecommerceapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.Shop.MapActivity;
import com.example.personalizedecommerceapp.fragment.FragmentHomeScreen;
import com.example.personalizedecommerceapp.fragment.OrderFragment;
import com.example.personalizedecommerceapp.fragment.RecommendationFragment;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.UserPref;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener,
        View.OnClickListener, PopupMenu.OnMenuItemClickListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView tvTitle;
    private ImageButton btnAction;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        initToolbar();
        initUI();

    }

    private void initToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            tvTitle = toolbar.findViewById(R.id.tvTitle);
            ImageButton btnBack = toolbar.findViewById(R.id.btnBack);
            btnBack.setVisibility(View.GONE);
            btnAction = toolbar.findViewById(R.id.btnAction);
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setOnClickListener(this);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUI() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menuHome);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuHome) {
            tvTitle.setText(Constants.HOME_SCREEN);
            setFragment(new FragmentHomeScreen());
        } else if (itemId == R.id.menuRecommendation) {
            tvTitle.setText(Constants.RECOMMENDATION_SCREEN);
                setFragment(new RecommendationFragment());
        } else if (itemId == R.id.menuViewOrders) {
            tvTitle.setText(Constants.ORDER_SCREEN);
            setFragment(new OrderFragment());
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAction) {
            onClickBtnAction();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuLogout) {
            onClickMenuLogout();
        } else if (itemId == R.id.menuProfile) {
            onClickMenuProfile();
        }
        else if (itemId == R.id.changepass) {
            startActivity(new Intent(BottomNavigationActivity.this,ChangePass.class));
        }
        else if (itemId == R.id.preference) {
            startActivity(new Intent(BottomNavigationActivity.this,PreferenceActivity.class));
        }
        return false;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void onClickBtnAction() {
        PopupMenu popupMenu = new PopupMenu(this, btnAction);
        popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    private void onClickMenuLogout() {
        if (alertDialog!=null) alertDialog.cancel();
        alertDialog  = DialogUtils.logoutDialog(this);
        alertDialog.show();
    }

    private void onClickMenuProfile() {
        startActivity(new Intent(BottomNavigationActivity.this,ProfileActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();

        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                call();
            }
        },2000);
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

    public GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

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
                        status.startResolutionForResult(BottomNavigationActivity.this, 10);

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
    public void onLocationChanged(Location location) {
        UserPref.setLoc(BottomNavigationActivity.this,location.getLatitude()+","+location.getLongitude());
    }

    public void call()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();

            }
        } else {

            buildGoogleApiClient();

        }
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
//                            call();
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