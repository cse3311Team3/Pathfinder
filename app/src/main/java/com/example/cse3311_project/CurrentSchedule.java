package com.example.cse3311_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot, firebaseRoot2;
    ListView listView;
    private Spinner spinner;
    String scheduleName;
    Button buttonSelect;
    List<String> scheduleList = new ArrayList();
    List<String> locationList = new ArrayList<>();
    List<String> address1ArrayList = new ArrayList<>();
    List<String> address2ArrayList = new ArrayList<>();
    List<String> cityArrayList = new ArrayList<>();
    List<String> stateArrayList = new ArrayList<>();
    List<String> zipArrayList = new ArrayList<>();
    List<String> fullAddressArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_schedule);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        listView = (ListView)findViewById(R.id.schedule_list);
        buttonSelect = (Button) findViewById(R.id.select_button);
        spinner = findViewById(R.id.schedule_spinner3);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, scheduleList);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fullAddressArrayList);
        listView.setAdapter(adapter2);


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

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                firebaseRoot.child(uid).child("Schedules").child(scheduleName).child("Locations").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        locationList.clear();
                        for (DataSnapshot locationSnapshot: snapshot.getChildren()) {
                            String name = locationSnapshot.child("Location Name").getValue(String.class);
                            String addressOne = locationSnapshot.child("Address One").getValue(String.class);
                            String addressTwo = locationSnapshot.child("Address Two").getValue(String.class);
                            String city = locationSnapshot.child("City").getValue(String.class);
                            String country = locationSnapshot.child("Country").getValue(String.class);
                            String postal = locationSnapshot.child("Postal Code").getValue(String.class);
                            String state = locationSnapshot.child("State").getValue(String.class);
                            if(name != null){
                                //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                                locationList.add(name);
                                address1ArrayList.add(addressOne);
                                address2ArrayList.add(addressTwo);
                                cityArrayList.add(city);
                                stateArrayList.add(country);
                                zipArrayList.add(postal);
                                //Log.v("locations: ", name);
                                adapter2.notifyDataSetChanged();
                            }

                        }


                        // Getting locations and parsing them:
                        for (int i = 0; i < address1ArrayList.size(); i++) {
                            if(address2ArrayList.get(i) != null){
                            fullAddressArrayList.add(locationList.get(i) + ": " + address1ArrayList.get(i) + " " + address2ArrayList.get(i) + ", " + cityArrayList.get(i) + ", " + stateArrayList.get(i) + " " + zipArrayList.get(i));
                            }
                            else {
                                fullAddressArrayList.add(locationList.get(i) + ": " + address1ArrayList.get(i) +  ", " + cityArrayList.get(i) + ", " + stateArrayList.get(i) + " " + zipArrayList.get(i));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }});
            }
        });
        listView.setAdapter(adapter2);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        scheduleName = text;
        Toast.makeText(this, scheduleName, Toast.LENGTH_SHORT).show();
        Log.v("ScheduleName", scheduleName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
