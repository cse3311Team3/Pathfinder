package com.example.cse3311_project;

//Some code was used from this source: https://github.com/vastavch/GoogleMapsDistanceMatrixAPI_Demo

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class Distance extends AppCompatActivity implements GeoTask.Geo {
    EditText edttxt_from,edttxt_to;
    Button btn_get;
    String str_from,str_to;
    TextView tv_result1,tv_result2;
    String fAddress;

    double lat, lng;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private DatabaseReference firebaseRoot1;

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
    HashMap<Integer, String> hash_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance);

        addressArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        zipArrayList = new ArrayList<>();
        keyArrayList = new ArrayList<>();
        orderedArrayList = new ArrayList<>();
        unorderedArrayList = new ArrayList<>();

        fullAddressArrayList = new ArrayList<>();

        hash_map = new HashMap<Integer, String>();

        initialize();
        getDeviceLocation();

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
                        fullAddressArrayList.add(addressArrayList.get(i) + ", " + cityArrayList.get(i) + ", " + stateArrayList.get(i) + " " + zipArrayList.get(i));

                        String addressParsed = addressArrayList.get(i).replace(" ", "+");
                        String cityParsed = cityArrayList.get(i).replace(" ", "+");

                        fAddress = addressParsed + "," + cityParsed;
                        unorderedArrayList.add(fAddress);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Lat_Lng = lat + "," + lng;

//                str_from=edttxt_from.getText().toString();
//                str_to=edttxt_to.getText().toString();
                Log.d("SNAP: ", unorderedArrayList.toString());


                for (int x = 0; x < unorderedArrayList.size(); x++) {
                    str_from = Lat_Lng;
//                    str_to = fAddress;
                    str_to = unorderedArrayList.get(x);
                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + str_from + "&destinations=" + str_to + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyDwqb3Mc7o9iyi7eyavU4AG53radkNaRaM";
                    new GeoTask(Distance.this).execute(url);

                }
            }
        });

    }

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
//            Log.d("SNAP: ", hash_map.get(5));

            for (int z = 0; z < orderedArrayList.size(); z++) {
                hash_map.get(orderedArrayList.get(z));
                    //print to page instead
                    Log.d("ORDERED: ", hash_map.get(orderedArrayList.get(z)));

             }

        }

        tv_result1.setText("Duration= " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
        tv_result2.setText("Distance= " + dist + " mi");

//
//        for (int z = 0; z < orderedArrayList.size(); z++) {
//            if (hash_map.containsKey(orderedArrayList.get(z))){
//                //print to page instead
//                Log.d("ORDERED: ", hash_map.get(z));
//            }
//
//        }


        Log.d("SNAP: ", hash_map.toString());

    }



    public void initialize()
    {
        edttxt_from= (EditText) findViewById(R.id.editText_from);
        edttxt_to= (EditText) findViewById(R.id.editText_to);
        btn_get= (Button) findViewById(R.id.button_get);
        tv_result1= (TextView) findViewById(R.id.textView_result1);
        tv_result2=(TextView) findViewById(R.id.textView_result2);

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


}