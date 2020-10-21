package com.example.zomatoclone.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    private static PermissionUtils permissionUtils;

    public static PermissionUtils init(){
        if (permissionUtils == null){
            permissionUtils = new PermissionUtils();
        }
        return permissionUtils;
    }

    public static void requestPermission(AppCompatActivity activity,String permissionName,int requestId){
        ActivityCompat.requestPermissions(activity,
                new String[] {permissionName},
                requestId
        );
    }

    public static boolean isPermissionGranted(Context context, String permissionName){
        return ContextCompat
                .checkSelfPermission(
                        context,
                        permissionName
                ) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isLocationEnable(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }else {
            return false;
        }
    }

}
