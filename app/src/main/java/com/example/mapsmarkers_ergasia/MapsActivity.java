package com.example.mapsmarkers_ergasia;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static FirebaseFirestore db;
    Marker currentM = null;
    Location mLastLocation;
    LocationManager locationManager;// Define object to manage location
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // FireBase
        db = FirebaseFirestore.getInstance();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //

        //Location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Permission Check (if the app has access to the location )
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {//Update and show with Marker the live location from Network_Provider
                    mLastLocation = location;
                    if (currentM != null) {
                        currentM.remove();
                    }
                    //New current location
                    double latitude = location.getLatitude();
                    double longitube = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitube);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    currentM = mMap.addMarker(markerOptions);

                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


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
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//Update and show with Marker the live location from GPS_Provider
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLastLocation = location;
                    if (currentM != null) {
                        currentM.remove();
                    }
                    //New current location
                    double latitude = location.getLatitude();
                    double longitube = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitube);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    currentM = mMap.addMarker(markerOptions);

                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


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
            });
        }


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
    public void onMapReady(GoogleMap googleMap) {//Show the map
        mMap = googleMap;


    }


    public void ShowSaveLocation(View v) {//When we click the button btnsavelocation from activity_xml send data and create the popupactivity
        double currentLat = currentM.getPosition().latitude;
        double currentLon = currentM.getPosition().longitude;

        Intent intent = new Intent(getApplicationContext(), popupactivity.class);

        //Send the location info to the popupactivity
        Bundle args = new Bundle();
        args.putDouble("currentLat", currentLat);
        args.putDouble("currentLon", currentLon);
        intent.putExtras(args);


        startActivity(intent);
    }


}
