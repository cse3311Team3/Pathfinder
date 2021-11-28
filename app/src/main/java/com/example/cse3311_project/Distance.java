package com.example.cse3311_project;

//Some code was used from this source: https://github.com/vastavch/GoogleMapsDistanceMatrixAPI_Demo

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Distance extends AppCompatActivity implements GeoTask.Geo, AdapterView.OnItemSelectedListener {
    Button btn_get;
    String str_from,str_to;
    String fAddress;

    double lat, lng;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private DatabaseReference firebaseRoot1;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot;
    int counter = 0;
    int c = 0;

    ArrayList<String> keyArrayList;
    ArrayList<String> addressArrayList;
    ArrayList<String> cityArrayList;
    ArrayList<String> stateArrayList;
    ArrayList<String> zipArrayList;
    ArrayList<String> fullAddressArrayList;

    ArrayList<String> unorderedArrayList;
    ArrayList<Integer> orderedArrayList;
    ArrayList<String> finalArrayList;
    HashMap<Integer, String> hash_map;
    List<String> scheduleList = new ArrayList();
    ListView listViewOrdered;
    private Spinner spinner;
    String scheduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        listViewOrdered = (ListView)findViewById(R.id.listviewOrdered);

        addressArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        zipArrayList = new ArrayList<>();
        keyArrayList = new ArrayList<>();
        orderedArrayList = new ArrayList<>();
        unorderedArrayList = new ArrayList<>();
        finalArrayList = new ArrayList<>();
        fullAddressArrayList = new ArrayList<>();


        hash_map = new HashMap<Integer, String>();

        btn_get= (Button) findViewById(R.id.button_get);

        getDeviceLocation();

        spinner = findViewById(R.id.schedule_spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, scheduleList);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRoot = FirebaseDatabase.getInstance().getReference();
        firebaseRoot.child(uid).child("Schedules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //scheduleList.clear();
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
        int x = scheduleList.size();
        //Toast.makeText(Distance.this, x, Toast.LENGTH_SHORT).show();
        Log.v("ScheduleName", "size" + x);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseRoot1 = FirebaseDatabase.getInstance().getReference();
                firebaseRoot1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapShot3) {
                        // Getting values from Locations
                        if (dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").exists()) {
                            for (DataSnapshot ds : dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").getChildren()) {
                                keyArrayList.add(ds.getKey());
                            }
                            for (DataSnapshot ds : dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").getChildren()) {
                                String locationName = ds.getValue().toString();
                                if (keyArrayList.contains(locationName)) {
                                    addressArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("Address One").getValue().toString());
                                    cityArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("City").getValue().toString());
                                    stateArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("State").getValue().toString());
                                    zipArrayList.add(dataSnapShot3.child(uid).child("Schedules").child(scheduleName).child("Locations").child(locationName).child("Postal Code").getValue().toString());
                                }
                            }


                            // Getting locations and parsing them:
                            for (int i = 0; i < addressArrayList.size(); i++) {
                                fullAddressArrayList.add(addressArrayList.get(i) + ", " + cityArrayList.get(i) + ", " + stateArrayList.get(i) + " " + zipArrayList.get(i));

                                String addressParsed = addressArrayList.get(i).replace(" ", "+");
                                String cityParsed = cityArrayList.get(i).replace(" ", "+");

                                fAddress = addressParsed + "," + cityParsed;
                                unorderedArrayList.add(fAddress);
                            }




                            String Lat_Lng = lat + "," + lng;

                            for (int x = 0; x < unorderedArrayList.size(); x++) {
                                str_from = Lat_Lng;
                                str_to = unorderedArrayList.get(x);
                                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + str_from + "&destinations=" + str_to + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyDwqb3Mc7o9iyi7eyavU4AG53radkNaRaM";
                                new GeoTask(Distance.this).execute(url);

                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                String Lat_Lng = lat + "," + lng;
//
//                for (int x = 0; x < unorderedArrayList.size(); x++) {
//                    str_from = Lat_Lng;
//                    str_to = unorderedArrayList.get(x);
//                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + str_from + "&destinations=" + str_to + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyDwqb3Mc7o9iyi7eyavU4AG53radkNaRaM";
//                    new GeoTask(Distance.this).execute(url);
//
//                }
            }
        });

    }

    //The final returned function:
    @Override
    public void setDouble(String result) {
        String res[]=result.split(",");
        Double min=Double.parseDouble(res[0])/60;
        int dist=Integer.parseInt(res[1])/1609;     //Gets distance between in miles


        hash_map.put(dist, fullAddressArrayList.get(counter));


        if (unorderedArrayList.size() > counter) {
            orderedArrayList.add(dist);
            counter++;
        }

        if (unorderedArrayList.size() == counter) {
            Collections.sort(orderedArrayList);
            counter++;

            for (int z = 0; z < orderedArrayList.size(); z++) {
                finalArrayList.add(hash_map.get(orderedArrayList.get(z)));
             }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, finalArrayList);
            listViewOrdered.setAdapter(arrayAdapter);

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

                        lat = currentLocation.getLatitude();
                        lng = currentLocation.getLongitude();

                    }else{
                        Toast.makeText(Distance.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (SecurityException e){
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        scheduleName = text;
        Toast.makeText(Distance.this, scheduleName, Toast.LENGTH_SHORT).show();
        Log.v("ScheduleName", scheduleName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Toast.makeText(Distance.this, scheduleName, Toast.LENGTH_SHORT).show();
        Log.v("nothing", "nothing");
    }
}