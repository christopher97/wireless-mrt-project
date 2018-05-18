package com.example.dhyatmika.fp_layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyMap extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Location locGPS;
    private Marker marker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private int PERMISSION_ACCESS_LOC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        boolean fineLocPermision = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocPermision = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(!fineLocPermision){
            Log.v("TEST", "NO PERMISSION FOR LOCATION, ATTEMPT REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
            return;
        }

        if(!coarseLocPermision){
            Log.v("TEST", "NO PERMISSION FOR LOCATION, ATTEMPT REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1
            );
            return;
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(mMap != null){
                    Log.i("TEST", "GET LOCATION");
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(17).build();
                    if(marker != null){
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions().title("You Are Here").position(loc));
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng userLoc = new LatLng(locGPS.getLatitude(), locGPS.getLongitude());
        mMap.addMarker(new MarkerOptions().position(userLoc).title("You Are Here"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(userLoc).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void getLocation(View view) {


    }
}
