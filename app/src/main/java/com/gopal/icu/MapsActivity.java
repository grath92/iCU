package com.gopal.icu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by gopal on 30/11/16.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double mLat, mLng;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mTitle = getIntent().getStringExtra(Utility.NAME);
        mLat = getIntent().getDoubleExtra(Utility.LAT,0);
        mLng = getIntent().getDoubleExtra(Utility.LNG,0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker and move the camera
        LatLng mLatLng = new LatLng(mLat, mLng);
        mMap.addMarker(new MarkerOptions().position(mLatLng).title(mTitle)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,13.0f));
    }
}
