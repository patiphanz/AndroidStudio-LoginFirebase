package com.example.loginfirebase;

import android.Manifest;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ViewLocation extends AppCompatActivity {

    private Button getLocation, stopGPS;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker gps;
    private int counter = 1;

    // Thread
    Handler h = new Handler();
    Thread task;
    private long startTime;
    private String timeString;
    private TextView viewTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);

        try {
            if(ActivityCompat.checkSelfPermission(this, mPermission) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLocation = (Button) findViewById(R.id.getLocation);
        viewTimer = (TextView) findViewById(R.id.viewTimer);
        stopGPS = (Button) findViewById(R.id.stopGPS);

        stopGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopHandleTask();
                viewTimer.setText("Location service is stopped.");
            }
        });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                counter = 1;
//                gps = new GPSTracker(ViewLocation.this);
//                if(gps.isCanGetLocation()) {
//                    double latitude = gps.getLatitude();
//                    double longitude = gps.getLongitude();
//
//                    Toast.makeText(getApplicationContext(), counter + "\nCurrent location is \n Lat: " + latitude +
//                            "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                } else {
//                    gps.showSettingsAlert();
//                }
//                counter++;
            }
        });
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        task = new Thread() {
            @Override
            public void run() {
                long mills = System.currentTimeMillis() - startTime;
                final long secs = mills/ 1000 % 60;

                timeString = String.format("%02d",secs);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewTimer.setText(timeString);
                        if(secs % 5 == 0) {
                            gps = new GPSTracker(ViewLocation.this);
                            if(gps.isCanGetLocation()) {
                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();

                                Toast.makeText(getApplicationContext(), counter + "\nCurrent location is \n Lat: " + latitude +
                                        "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                            } else {
                                gps.showSettingsAlert();
                            }
                            counter++;
                        }
                    }
                });
                h.postDelayed(task, 1000);
            }
        };
        h.postDelayed(task, 1000);
    }

    public void stopHandleTask() {
        h.removeCallbacks(task);
    }
}
