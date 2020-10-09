package com.example.zomatoclone.Service;


import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.zomatoclone.Utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentService extends IntentService {

    private final String TAG = getClass().getSimpleName();
    private ResultReceiver resultReceiver;
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String errorMessage = "";
            List<Address> addresses = null;
            double latitude = intent.getDoubleExtra(Util.LOCATION_LATITUDE_DATA_EXTRA,0);
            double longitude = intent.getDoubleExtra(Util.LOCATION_LONGITUDE_DATA_EXTRA,0);
            Log.e(TAG,"Lat: "+latitude + "\n" + "Lng: "+longitude);
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException ioException) {
                errorMessage = "Service Not Available";
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used";
            }

            resultReceiver = intent.getParcelableExtra(Util.RECEIVER);

            if (addresses == null || addresses.size()  == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "Not Found";
                    Log.e(TAG, errorMessage);
                }
                deliverResultToReceiver(Util.FAILURE_RESULT, errorMessage, null);
            } else {
                Log.e(TAG,"Add: "+addresses.get(0));
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();

                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                Log.i(TAG, "Address Found");
                deliverResultToReceiver(Util.SUCCESS_RESULT,
                        TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),
                                addressFragments), address);
            }
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Util.RESULT_ADDRESS, address);
        bundle.putString(Util.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }
}
