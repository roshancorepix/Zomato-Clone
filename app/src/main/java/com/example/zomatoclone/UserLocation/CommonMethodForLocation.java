package com.example.zomatoclone.UserLocation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.zomatoclone.R;
import com.example.zomatoclone.Service.FetchAddressIntentService;
import com.example.zomatoclone.SharedPrefrence.PreferenceManager;
import com.example.zomatoclone.SharedPrefrence.UserLocationPreference;
import com.example.zomatoclone.Utils.Util;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class CommonMethodForLocation extends Activity {

    private final String TAG = getClass().getSimpleName();

    private ResultReceiver resultReceiver;
    private Context context;
    private ProgressBar progressBar;

    public CommonMethodForLocation() {
    }

    public CommonMethodForLocation(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");


    }

    // Check if user allow the location access permission or not
    // if user allow the permission then get the users location latitude and longitude
    public void accessLocation() {
        resultReceiver = new AddressResultReceiver(new Handler(), context, progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(Util.SET_INTERVAL);
            locationRequest.setFastestInterval(Util.FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission((Activity)context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity)context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // location permission has not been granted.
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Dialog
                }

                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Util.REQUEST_LOCATION);
            }else {
                progressBar.setVisibility(View.VISIBLE);
                LocationServices.getFusedLocationProviderClient(context)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(context)
                                        .removeLocationUpdates(this);

                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int latestLocationIndex = locationResult.getLocations().size() - 1;

                                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                                    Log.e(TAG, "Latitude: " + latitude);
                                    Log.e(TAG, "Longitude: " + longitude);

                                    // save users current latitude and longitude in sharedpreferences
                                    UserLocationPreference.init(context);
                                    UserLocationPreference.setUserLocationLatitude(latitude);
                                    UserLocationPreference.setUserLocationLongitude(longitude);

                                    // send users latlng data to the method
                                    fetchAddressFromLatLong(latitude, longitude);
                                } else {
                                    Log.e(TAG,"Location result null");
                                }
                            }
                        }, Looper.getMainLooper());
            }

        }

    }

    // This method send data to the FetchAddressIntentService class
    private void fetchAddressFromLatLong(double lat, double lng){
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(Util.RECEIVER, resultReceiver);
        intent.putExtra(Util.LOCATION_LATITUDE_DATA_EXTRA, lat);
        intent.putExtra(Util.LOCATION_LONGITUDE_DATA_EXTRA, lng);
        context.startService(intent);
    }

    // This method enable device location setting programmatically
    public void enableLocationSetting() {

        LocationRequest request = new LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder locationSettingBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);

        Task<LocationSettingsResponse> responseTask = LocationServices.getSettingsClient(context).checkLocationSettings(locationSettingBuilder.build());

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
                                exception.startResolutionForResult((Activity) context, Util.LOCATION_SETTING_CODE);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            } catch (ClassCastException ex) {
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

    public void showAlert(Context context, String message)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView messageTextView = (TextView) dialog.findViewById(R.id.textMsg);
        messageTextView.setText(message);
        RelativeLayout button = dialog.findViewById(R.id.rl_button_setting);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.e(TAG, "You Click on button");
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
