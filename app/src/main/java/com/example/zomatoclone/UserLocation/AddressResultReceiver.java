package com.example.zomatoclone.UserLocation;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.zomatoclone.SharedPrefrence.PreferenceManager;
import com.example.zomatoclone.SharedPrefrence.UserLocationPreference;
import com.example.zomatoclone.UI.PermissionActivity;
import com.example.zomatoclone.UI.UserLocationMapActivity;
import com.example.zomatoclone.Utils.Util;

public class AddressResultReceiver extends ResultReceiver {

    private Context context;
    private static final String TAG = AddressResultReceiver.class.getSimpleName();
    public AddressResultReceiver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        final Address address = resultData.getParcelable(Util.RESULT_ADDRESS);
        if (resultCode == Util.SUCCESS_RESULT){
            assert address != null;
            UserLocationPreference.init(context);
            UserLocationPreference.setUserLocationAddress(address.getAddressLine(0));
            classifyAddress(address.getAddressLine(0));
            context.startActivity(new Intent(context, UserLocationMapActivity.class));
            Log.e(TAG,"Address: "+address.getAddressLine(0));
        }else {
            Log.e(TAG,"Error: "+resultCode);
        }
    }

    private void classifyAddress(String address){
        UserLocationPreference.init(context);
        UserLocationPreference.setUserLocationAddress(address);
        String[] addressLine = address.split(",");
        String streetNumber = addressLine[0];
        String road = addressLine[1];
        String area = addressLine[2];
        String subLocality = addressLine[3];
        String locality = addressLine[4];

        UserLocationPreference.setUserArea(area);
        UserLocationPreference.setUserSubLocality(subLocality);

        Log.e(TAG,"streetNumber: "+streetNumber);
        Log.e(TAG,"road: "+road);
        Log.e(TAG,"area: "+area);
        Log.e(TAG,"subLocality: "+subLocality);
        Log.e(TAG,"locality: "+locality);
    }
}
