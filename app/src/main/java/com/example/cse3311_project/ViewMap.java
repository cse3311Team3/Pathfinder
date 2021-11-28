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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;
//import com.example.cse3311_project.databinding.ActivityViewMap2Binding;


public class ViewMap extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private static final String TAG = "MapsActivity";

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private DatabaseReference firebaseRoot1;
    private Geocoder geocoder;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot;
    private Spinner spinner;

    ArrayList<String> keyArrayList;
    ArrayList<String> addressArrayList;
    ArrayList<String> cityArrayList;
    ArrayList<String> stateArrayList;
    ArrayList<String> zipArrayList;

    String scheduleName;
    List<String> scheduleList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRoot = FirebaseDatabase.getInstance().getReference();

        addressArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        zipArrayList = new ArrayList<>();
        keyArrayList = new ArrayList<>();

        spinner = findViewById(R.id.spinnerMap);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, scheduleList);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        firebaseRoot.child(uid).child("Schedules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduleList.clear();
                scheduleList.add("Select a schedule....");
                for (DataSnapshot locationSnapshot: snapshot.getChildren()) {
                    String name = locationSnapshot.child("Name").getValue(String.class);
                    if(name != null){
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        scheduleList.add(name);
                        adapter.notifyDataSetChanged();
                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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


      /*  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        firebaseRoot1 = FirebaseDatabase.getInstance().getReference();
        firebaseRoot1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot3) {
                // Getting values from Locations
                if (dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").exists()) {
                    for (DataSnapshot ds : dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").getChildren()) {
                        keyArrayList.add(ds.getKey());
                    }
//                    Log.d("Locations: ", keyArrayList.toString());
                    for (DataSnapshot ds : dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").getChildren()) {
                        String locationName = ds.getValue().toString();
                        if (keyArrayList.contains(locationName)) {
                            addressArrayList.add(dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").child(locationName).child("Address One").getValue().toString());
                            cityArrayList.add(dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").child(locationName).child("City").getValue().toString());
                            stateArrayList.add(dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").child(locationName).child("State").getValue().toString());
                            zipArrayList.add(dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").child(locationName).child("Postal Code").getValue().toString());
                        }
                    }
//                    Log.d("SNAP: ", addressArrayList.toString());
//                    Log.d("SNAP: ", cityArrayList.toString());
//                    Log.d("SNAP: ", stateArrayList.toString());
//                    Log.d("SNAP: ", zipArrayList.toString());


                    // Marking locations on map:
                    for (int i = 0; i < addressArrayList.size(); i++) {
                        String fAddress = addressArrayList.get(i) + ", " + cityArrayList.get(i) + ", " + stateArrayList.get(i) + " " + zipArrayList.get(i);
//                        Log.d("Addy: ", fAddress);

                            try {
                                List<Address> addresses = geocoder.getFromLocationName(fAddress, 1);

                                if (addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    Log.d(TAG, "OnMapReady: " + address.toString());
                                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(location)
                                            .title(address.getLocality());


                                    mMap.addMarker(markerOptions);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

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
        }catch (SecurityException e){
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            String text = adapterView.getItemAtPosition(i).toString();
            scheduleName = text;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            firebaseRoot1 = FirebaseDatabase.getInstance().getReference();
            firebaseRoot1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapShot3) {
                    // Getting values from Locations
                    if (dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").exists()) {
                        for (DataSnapshot ds : dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Locations").getChildren()) {
                            keyArrayList.add(ds.getKey());
                        }
//                    Log.d("Locations: ", keyArrayList.toString());
                        for (DataSnapshot ds : dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").getChildren()) {
                            String locationName = ds.getValue().toString();
                            if (keyArrayList.contains(locationName)) {
                                addressArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("Address One").getValue().toString());
                                cityArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("City").getValue().toString());
                                stateArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("State").getValue().toString());
                                zipArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("Postal Code").getValue().toString());
                            }
                        }
//                    Log.d("SNAP: ", addressArrayList.toString());
//                    Log.d("SNAP: ", cityArrayList.toString());
//                    Log.d("SNAP: ", stateArrayList.toString());
//                    Log.d("SNAP: ", zipArrayList.toString());


                        // Marking locations on map:
                        for (int i = 0; i < addressArrayList.size(); i++) {
                            String fAddress = addressArrayList.get(i) + ", " + cityArrayList.get(i) + ", " + stateArrayList.get(i) + " " + zipArrayList.get(i);
//                        Log.d("Addy: ", fAddress);

                            try {
                                List<Address> addresses = geocoder.getFromLocationName(fAddress, 1);

                                if (addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    Log.d(TAG, "OnMapReady: " + address.toString());
                                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(location)
                                            .title(address.getLocality());


                                    mMap.addMarker(markerOptions);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(this, "Please select a schedule", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

