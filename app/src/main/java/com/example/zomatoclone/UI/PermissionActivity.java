package com.example.zomatoclone.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zomatoclone.CallBacks.BottomSheetCallback;
import com.example.zomatoclone.R;
import com.example.zomatoclone.UserLocation.CommonMethodForLocation;
import com.example.zomatoclone.Utils.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private boolean isOpen = false;
    private ProgressBar locationProgressbar;
    private Button useCurrentLocationButton;
    private TextView textManually;
    private RelativeLayout bottomSheetUseLocation;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View backgroundView;
    private ImageView closeBottomSheetButton;
    private CommonMethodForLocation commonMethodForLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));

        setContentView(R.layout.activity_permission);

        init();

        CommonMethodForLocation c = new CommonMethodForLocation();
        c.showAlert(this, String.valueOf(Html.fromHtml(getString(R.string.location_permission_message))));

        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback(mBottomSheetBehavior));

        useCurrentLocationButton.setOnClickListener(this);

        textManually.setOnClickListener(this);

        bottomSheetUseLocation.setOnClickListener(this);

        backgroundView.setOnClickListener(this);

        closeBottomSheetButton.setOnClickListener(this);

    }

    private void closeBottomSheet() {
        isOpen = false;
        closeKeyBoard();
        backgroundView.setVisibility(View.GONE);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void openBottomSheet() {
        isOpen = true;
        backgroundView.setVisibility(View.VISIBLE);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_current_location:

            case R.id.rl_use_current_location:
                // Access User's Location
                commonMethodForLocation = new CommonMethodForLocation(PermissionActivity.this, locationProgressbar);
                commonMethodForLocation.accessLocation();
                break;

            case R.id.iv_close_bottom_sheet:

            case R.id.bg:
                closeBottomSheet();
                break;

            case R.id.tv_manual:
                // manually set location
                openBottomSheet();
                break;
        }

    }

    private void init() {
        locationProgressbar = findViewById(R.id.progressBar);
        useCurrentLocationButton = findViewById(R.id.btn_current_location);
        textManually = findViewById(R.id.tv_manual);
        bottomSheetUseLocation = findViewById(R.id.rl_use_current_location);
        backgroundView = findViewById(R.id.bg);
        closeBottomSheetButton = findViewById(R.id.iv_close_bottom_sheet);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG,"onRequestPermissionsResult");
        if (requestCode == Util.REQUEST_LOCATION) {
            // check if the only required permission has been granted
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // code for getting user location.
                Log.e(TAG, "You allow the permission");
                locationProgressbar.setVisibility(View.VISIBLE);
                commonMethodForLocation.enableLocationSetting();
                commonMethodForLocation.accessLocation();

            } else {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    public void onBackPressed() {
        if (isOpen){
            closeBottomSheet();
        }else {
            super.onBackPressed();
        }
    }

}