package com.example.familylocation;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;



    private Button showCurrent;
    double lat = 0;
    double longt = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showCurrent = (Button)findViewById(R.id.showCurrent);


        showCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lat = mMap.getMyLocation().getLatitude();
                longt = mMap.getMyLocation().getLongitude();


                SharedPreferences pref = getSharedPreferences("test", MODE_PRIVATE);
                pref.edit()
                        .putString("lat", String.valueOf(lat))
                        .putString("longt", String.valueOf(longt))
                        .commit();

                Toast.makeText(MapsActivity.this,"lat:"+lat+"  longt:"+longt,Toast.LENGTH_SHORT).show();
                finish();

            }
        });
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(22.303141, 114.185876);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        moveMap(sydney);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            Toast.makeText(MapsActivity.this,"permission error!",Toast.LENGTH_SHORT).show();
        }




        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("latitude:"+point.latitude+"  longitude:"+point.longitude);

                mMap.addMarker(marker);

                System.out.println(point.latitude+"---"+ point.longitude);
            }
        });
    }
    private void moveMap(LatLng place) {

        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();


        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }








}
