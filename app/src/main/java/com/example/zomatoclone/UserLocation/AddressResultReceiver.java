package com.example.zomatoclone.UserLocation;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.example.zomatoclone.SharedPrefrence.UserLocationPreference;
import com.example.zomatoclone.Utils.Util;

public class AddressResultReceiver extends ResultReceiver {

    private Context context;
    private ProgressBar progressBar;
    private static final String TAG = AddressResultReceiver.class.getSimpleName();
    public AddressResultReceiver(Handler handler, Context context,ProgressBar progressBar) {
        super(handler);
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        final Address address = resultData.getParcelable(Util.RESULT_ADDRESS);
        if (resultCode == Util.SUCCESS_RESULT){
            assert address != null;
            progressBar.setVisibility(View.INVISIBLE);
            UserLocationPreference.init(context);
            UserLocationPreference.setUserLocationAddress(address.getAddressLine(0));
            Log.e(TAG,"Address: "+address.getAddressLine(0));
        }else {
            Log.e(TAG,"Error: "+resultCode);
        }
    }
}
