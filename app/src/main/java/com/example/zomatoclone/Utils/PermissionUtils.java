package com.example.zomatoclone.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zomatoclone.R;

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

    public static void showDialogWithoutTitle(final Activity activity, String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        final View customLayout = activity
                .getLayoutInflater()
                .inflate(R.layout.custom_dialog,null);
        dialog.setView(customLayout);
        dialog.setCancelable(false);
        final AlertDialog show = dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(show.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView messageTextView = customLayout.findViewById(R.id.textMsg);
        messageTextView.setText(message);
        RelativeLayout goToSetting = customLayout.findViewById(R.id.rl_button_setting);
        goToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAppSetting(activity);
                show.dismiss();
            }
        });
        show.getWindow().setAttributes(lp);
    }

    public static void showAlertWithTitle(final Activity activity, String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View customLayout = activity
                .getLayoutInflater()
                .inflate(R.layout.custom_dialog_with_title,null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView titleTextView = customLayout.findViewById(R.id.tv_title);
        titleTextView.setText(title);

        TextView messageTextView = customLayout.findViewById(R.id.tv_message);
        messageTextView.setText(message);

        RelativeLayout retryButton = customLayout.findViewById(R.id.rl_button_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission((AppCompatActivity) activity,Manifest.permission.ACCESS_FINE_LOCATION,Util.REQUEST_LOCATION);
                dialog.dismiss();
            }
        });

        RelativeLayout sureButton = customLayout.findViewById(R.id.rl_button_i_am_sure);
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private static void openAppSetting(Activity activity){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
    }
}
