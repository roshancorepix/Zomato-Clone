package com.example.zomatoclone.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zomatoclone.Interface.BottomSheetCloseListener;
import com.example.zomatoclone.R;
import com.example.zomatoclone.UI.Fragments.BottomSheetFragment;
import com.example.zomatoclone.UserLocation.CommonMethodForLocation;
import com.example.zomatoclone.Utils.PermissionUtils;
import com.example.zomatoclone.Utils.StatusBarUtils;
import com.example.zomatoclone.Utils.Util;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetCloseListener {

    private final String TAG = getClass().getSimpleName();
    public boolean isOpenBottomSheet = false;
    private ProgressBar buttonProgressbar;
    private Button useCurrentLocationButton;
    private TextView textManually;
    private RelativeLayout bottomSheetUseLocation;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View backgroundView;
    private ImageView closeBottomSheetButton;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng currentLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // change status bar color
        StatusBarUtils.setStatsBarColor(this, getWindow(), R.color.statusBarColor);
        setContentView(R.layout.activity_permission);
        // Bind ID with view method
        bindId();

        // Button click method
        textManually.setOnClickListener(this);
        useCurrentLocationButton.setOnClickListener(this);

    }

    private void bindId() {
        textManually = findViewById(R.id.tv_manual);
        useCurrentLocationButton = findViewById(R.id.btn_current_location);
        buttonProgressbar = findViewById(R.id.button_progressbar);
    }

    private void openLocationSettingBottomSheet(){
        Log.e(TAG,"openLocationSettingBottomSheet");
        isOpenBottomSheet = true;
        BottomSheetFragment bottomSheet = new BottomSheetFragment();
        bottomSheet.show(getSupportFragmentManager(),"BottomSheetFragment");
    }

    // Location Access method's
    private void checkPermission(){
        if (currentLatLng == null) {
            if (PermissionUtils.isPermissionGranted(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                if (PermissionUtils.isLocationEnable(this)){
                    setUpLocationListener();
                }else {
                    enableLocationSetting();
                }

            }else {
                PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, Util.REQUEST_LOCATION);
            }
        }
    }

   private void setUpLocationListener() {
        Log.e(TAG,"setUpLocationListener");

   }

    public void enableLocationSetting() {
        LocationRequest request = new LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder locationSettingBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);

        Task<LocationSettingsResponse> responseTask = LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingBuilder.build());

        responseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes
                                .RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException exception = (ResolvableApiException) e;
                                exception.startResolutionForResult(PermissionActivity.this, Util.LOCATION_SETTING_CODE);
                            } catch (IntentSender.SendIntentException | ClassCastException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_manual:
                openLocationSettingBottomSheet();
                break;

            case R.id.btn_current_location:
                checkPermission();
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult");
        if (requestCode == Util.REQUEST_LOCATION){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e(TAG, "You allow the permission");
                    useCurrentLocationButton.setText(null);
                    buttonProgressbar.setVisibility(View.VISIBLE);
                    if (PermissionUtils.isLocationEnable(this)){
                        setUpLocationListener();
                    }else {
                        enableLocationSetting();
                    }
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (isOpenBottomSheet) {
                            Log.e(TAG, "Deny & don't ask again");
                        }
                    }else {
                        if (isOpenBottomSheet) {
                            Log.e(TAG, "Deny");
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util.LOCATION_SETTING_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e(TAG, "you click on OK");
                    useCurrentLocationButton.setText(null);
                    buttonProgressbar.setVisibility(View.VISIBLE);
                    setUpLocationListener();
                    break;

                case Activity.RESULT_CANCELED:
                    Log.e(TAG, "you click on NO,Thanks");
                    useCurrentLocationButton.setText(R.string.use_current_location);
                    buttonProgressbar.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void bottomSheetClosed(boolean b) {
        isOpenBottomSheet = b;
        Log.e(TAG,"bottomSheetClosed: "+isOpenBottomSheet);
    }
}