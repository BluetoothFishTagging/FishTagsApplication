package bft.fishtagsapp.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import bft.fishtagsapp.Constants;
import bft.fishtagsapp.R;

/**
 * Created by jamiecho on 3/9/16.
 */
public class GPS implements LocationListener {

    private LocationManager locationManager;
    private Location location;
    private String provider;
    private Context context;
    private Boolean permitted;

    public GPS(Context context) {
        permitted = false;
        Log.i("GPS", "CREATING");
        this.context = context;

        /*PermissionEverywhere.getPermission(context,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                },
                Constants.REQUEST_LOCATION,
                "Requesting Permission",
                "Obtain GPS Data for Fish Capture",
                R.mipmap.ic_launcher).enqueue(new PermissionResultCallback() {
            @Override
            public void onComplete(PermissionResponse permissionResponse) {
                permitted = permissionResponse.isGranted();
            }
        });*/


    }

    public void enableGPS() {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {
            Log.i("ENABLED", "FALSE");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }
        Log.i("ENABLED", "TRUE");
        //hopefully user will enable GPS here
    }

    public Location getGPS() {
        if(!permitted){
            return null;
        }
        enableGPS(); //in case not enabled
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        Log.i("PROVIDER : ", provider);
        try {
            //to be safe
            locationManager.requestLocationUpdates(provider, 0, 0, this);
            location = locationManager.getLastKnownLocation(provider);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
        // Initialize the location fields
        Log.i("LOCATION : ", String.valueOf(location));

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);

        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //nothing yet
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}