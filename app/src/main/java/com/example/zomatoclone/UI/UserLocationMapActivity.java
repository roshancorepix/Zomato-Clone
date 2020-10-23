package com.example.zomatoclone.UI;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.zomatoclone.R;
import com.example.zomatoclone.SharedPrefrence.UserLocationPreference;
import com.example.zomatoclone.Utils.ViewUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

public class UserLocationMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "UserLocationMapActivity";
    private GoogleMap mMap;
    private View mapView;
    private ImageView mapPin;
    private double currentLatitude, currentLongitude;
    private GoogleMapOptions googleMapOptions = new GoogleMapOptions();
    private LatLng currentLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        setContentView(R.layout.activity_user_location_map);
        bindId();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapView = mapFragment.getView();
            mapFragment.getMapAsync(this);
        }


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pin_bounce_anim);
        mapPin.startAnimation(animation);
    }

    private void bindId() {
        mapPin = findViewById(R.id.map_pin);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapOptions.zOrderOnTop(true);
        mMap = googleMap;
        changeMapStyle();
        changeLocationButtonPosition();
        enableMyLocationOnMap();
    }

    private void changeMapStyle(){
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    private void changeLocationButtonPosition(){
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 10);
    }

    private void enableMyLocationOnMap() {
        mMap.setPadding(0, ViewUtils.dpToPx(28f), 0, 0);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        if (currentLatLng == null) {
            currentLatLng = new LatLng(currentLatitude, currentLongitude);

            moveCamera(currentLatLng);
        }


    }

    private void moveCamera(LatLng currentLatLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 3000, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
        UserLocationPreference.init(this);
        currentLatitude = UserLocationPreference.getUserLocationLatitude();
        currentLongitude = UserLocationPreference.getUserLocationLongitude();

        Log.e(TAG,"currentLatitude: "+currentLongitude);
        Log.e(TAG,"currentLongitude: "+currentLongitude);
    }
}