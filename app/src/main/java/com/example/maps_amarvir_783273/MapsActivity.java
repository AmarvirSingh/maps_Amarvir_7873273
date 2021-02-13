package com.example.maps_amarvir_783273;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener, GoogleMap.OnPolylineClickListener {

    // Constants
    private static final int REQUEST_CODE = 1;
    private static final int SIDES = 4;

    Polygon polygon ;

    Polyline polyline;

    private GoogleMap mMap;

     LatLng userLoc;

     float totalDistance = 0;
     LatLng firstLoc, secondLoc, thirdLoc, fourthLoc;

    int count = 1;

    Marker firstMarker;
    Marker secondMarker;
    Marker thirdMarker;
    Marker fourthMarker;

    double distance [];

    ArrayList<Marker> markers = new ArrayList<>();

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                setUserLocation(location);
            }
        };


// checking the permissions
        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();


        // adding ;long press
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (count == 1 )
                setMarkerA(latLng);
                else if (count == 2)
                    setMarkerB(latLng);
                else if (count == 3)
                    setMarkerC(latLng);
                else if (count == 4) {

                    setMarkerD(latLng);
                    drawShape();
                }
                else if (count == 5)
                    clearMarkers();
            }
        });

        mMap.setOnPolygonClickListener(this);
        mMap.setOnPolylineClickListener(this);


    }

    private void drawShape() {

        PolygonOptions options = new PolygonOptions()
                .fillColor(0x3500FF00)
                .strokeColor(Color.RED)
                .strokeWidth(10)
                .clickable(true);


        PolylineOptions options1 = new PolylineOptions().clickable(true);



        for (int i = 0; i<SIDES ; i++){
            options.add(markers.get(i).getPosition());
        }

        polygon = mMap.addPolygon(options);

        float results[] = new float[10];
        float results1[] = new float[10];
        float results2[] = new float[10];
        float results3[] = new float[10];


        Location.distanceBetween(firstLoc.latitude, firstLoc.longitude, secondLoc.latitude, secondLoc.longitude, results);
        Location.distanceBetween(secondLoc.latitude, secondLoc.longitude, thirdLoc.latitude, thirdLoc.longitude, results1);
        Location.distanceBetween(thirdLoc.latitude, thirdLoc.longitude, fourthLoc.latitude, fourthLoc.longitude, results2);
        Location.distanceBetween(fourthLoc.latitude, fourthLoc.longitude, firstLoc.latitude, firstLoc.longitude, results3);

        totalDistance = results[0] + results1[0] + results2[0] + results3[0];




    }

    // Clear maps
    private void clearMarkers() {

        for (int i = 0; i < markers.size(); i++){
            markers.get(i).remove();
        }

        totalDistance = 0;
        count = 1;
        markers.clear();
       // removing thee shape
        polygon.remove();
        polygon = null;


    }

    private void setMarkerD(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().title("D").position(latLng);
        fourthMarker = mMap.addMarker(options);
    float results[] = new float[10];

    Location.distanceBetween(userLoc.latitude, userLoc.longitude, latLng.latitude, latLng.longitude, results);
        options.snippet(String.format("Distance = %.2f KM",results[0]/1000));


        fourthLoc = latLng;

        fourthMarker = mMap.addMarker(options);
        markers.add(fourthMarker);
        // setting count to 1
        count += 1;
    }

    private void setMarkerC(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().title("C").position(latLng);
        float results[] = new float[10];

        Location.distanceBetween(userLoc.latitude, userLoc.longitude, latLng.latitude, latLng.longitude, results);
        options.snippet(String.format("Distance = %.2f KM",results[0]/1000));


        thirdLoc = latLng;
       thirdMarker = mMap.addMarker(options);
        markers.add(thirdMarker);
        // increasing count
        count += 1;
    }

    private void setMarkerB(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().title("B").position(latLng);
        float results[] = new float[10];

        Location.distanceBetween(userLoc.latitude, userLoc.longitude, latLng.latitude, latLng.longitude, results);
        options.snippet(String.format("Distance = %.2f KM",results[0]/1000));




        secondLoc = latLng;
       secondMarker = mMap.addMarker(options);

        markers.add(secondMarker);
        // increasing count
        count += 1;
    }

    private void setMarkerA(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().title("A").position(latLng);
        float results[] = new float[10];

        Location.distanceBetween(userLoc.latitude, userLoc.longitude, latLng.latitude, latLng.longitude, results);
        options.snippet(String.format("Distance = %.2f KM",results[0]/1000));



        firstLoc = latLng;
       firstMarker = mMap.addMarker(options);

        markers.add(firstMarker);
        // increasing count
        count += 1;

    }

    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
    }

    private void setUserLocation(Location location) {
        userLoc = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions().title("You are Here");
        options.position(userLoc);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.custome));
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc,15));
    }


    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (REQUEST_CODE == requestCode){

            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

            }

        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

        Toast.makeText(this, "Total Distance = "+ totalDistance, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

        Toast.makeText(this, "hello ", Toast.LENGTH_SHORT).show();
    }
}