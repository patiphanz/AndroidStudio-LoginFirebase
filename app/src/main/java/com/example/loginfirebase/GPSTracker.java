package com.example.loginfirebase;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // is GPS enable status
    boolean isGPSEnabled = false;
    // check network enable status
    boolean isNetworkEnabled = false;
    // is able to get location
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BTW_UPDATES = 1000 * 60 * 1; // 1 min

    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // gps status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // network status
            isNetworkEnabled = locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER));
            if(!isNetworkEnabled && !isGPSEnabled) {
                // no network
            } else {
                this.canGetLocation = true;
                if(isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BTW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this);
                    if(locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled) {
                    if(location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BTW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this);
                        if(locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() {
        if(locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if(location != null) latitude = location.getLatitude();
        return latitude;
    }

    public double getLongitude() {
        if(location != null) longitude = location.getLongitude();
        return longitude;
    }

    public boolean isCanGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder((mContext));

        alertDialog.setTitle("GPS has been set?");
        alertDialog.setMessage("GPS is not enable, Enable?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
