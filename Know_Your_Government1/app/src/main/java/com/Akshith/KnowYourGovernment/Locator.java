package com.Akshith.KnowYourGovernment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;


public class Locator {

    private static final String TAG = "Locator";
    private MainActivity owner;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private int MY_PERM_REQUEST_CODE = 12345;
    private ConnectivityManager connectivityManager;
    private boolean connected = false;

    public Locator(MainActivity mainActivity)
    {
        this.owner = mainActivity;
        if(isOnline())
        {
            boolean havePermission = checkPermission();
            if (havePermission) {
                setUpLocationManager();
                determineLocation();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(owner);
            builder.setMessage("Data cannot be added/accessed without a network connection");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(owner, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(owner,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }

    public void setUpLocationManager() {

            if (locationManager != null)
                return;

            if (!checkPermission())
                return;

            // Get the system's Location Manager
            locationManager = (LocationManager) owner.getSystemService(LOCATION_SERVICE);

            // Define a listener that responds to location updates
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    //Toast.makeText(owner, "Update from " + location.getProvider(), Toast.LENGTH_SHORT).show();
                    //owner.doLocationWork(location.getLatitude(), location.getLongitude());
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // Nothing to do here
                }

                public void onProviderEnabled(String provider) {
                    // Nothing to do here
                }

                public void onProviderDisabled(String provider) {
                    // Nothing to do here
                }
            };

            // Register the listener with the Location Manager to receive GPS location updates
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

    }

    public void shutdown() {
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

    public void determineLocation(){

        if (!checkPermission())
            return;

        if (locationManager == null)
            setUpLocationManager();

        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(owner, "Using " + LocationManager.NETWORK_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null) {
                owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(owner, "Using " + LocationManager.PASSIVE_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(owner, "Using " + LocationManager.GPS_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // If you get here, you got no location at all
        owner.noLocationAvailable();
        return;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) owner
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

}
