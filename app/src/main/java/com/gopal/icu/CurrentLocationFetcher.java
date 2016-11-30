package com.gopal.icu;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocationFetcher implements GoogleApiClient.ConnectionCallbacks {

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private String finalLocation;
    private double mLat, mLong;

    public CurrentLocationFetcher(Context context) {
        this.mContext = context;
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    public double getLat(){
        return mLat;
    }

    public double getLong(){
        return mLong;
    }

    public String getFinalLocation(){
        return finalLocation;
    }


    @Override
    public void onConnected(Bundle bundle) {
        if(mGoogleApiClient != null) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                //If everything went fine lets get latitude and longitude
                mLat = location.getLatitude();
                mLong = location.getLongitude();
                myCurrentLocation(location.getLatitude(), location.getLongitude());
            }/*else {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mLat = location.getLatitude();
                        mLong = location.getLongitude();
                        myCurrentLocation(location.getLatitude(), location.getLongitude());
                    }
                });
            }*/
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


     /*
   * Show your location in txt format...
   * Requires latitude & longitude
   * */
    private void myCurrentLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                String strAddress = returnedAddress.getSubLocality();
                String strCity = returnedAddress.getLocality();
                String strState = returnedAddress.getAdminArea();
                finalLocation = strAddress + ", " + strCity + ", " + strState;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

