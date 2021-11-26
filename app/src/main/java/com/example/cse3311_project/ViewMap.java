package com.example.cse3311_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
//import com.example.cse3311_project.databinding.ActivityViewMap2Binding;


public class ViewMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private DatabaseReference firebaseRoot1;
//    String address, city, state, zip, fullAddress;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        getAddress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
//        getDeviceLocation();      Zooms in on Device location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Retrieves the given address
        String fAddress = HomePage.fullAddress;
        try {
            List <Address> addresses = geocoder.getFromLocationName(fAddress, 1);

            if (addresses.size() > 0){
                Address address = addresses.get(0);
                Log.d(TAG, "OnMapReady: " + address.toString());
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title(address.getLocality());


                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));


                //////////Adding another Location:
//                LatLng location2 = new LatLng(37.3783, -122.0777);
//                MarkerOptions markerOptions2 = new MarkerOptions()
//                        .position(location2)
//                        .title("Market");
//
//                mMap.addMarker(markerOptions2);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 16));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    private void getDeviceLocation(){

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17f));

                        }else{
                            Toast.makeText(ViewMap.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            //}
        }catch (SecurityException e){
        }
    }


    }

