package com.example.cse3311_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class CurrentSchedule extends AppCompatActivity{


    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot, firebaseRoot2;
    private Spinner pickScheduleSpinner;
    String address, city, state, zip;
    TextView fullAddress;

    List<String> scheduleList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_schedule);


//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseRoot = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();
//        firebaseRoot.child(uid).child("Schedules").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                scheduleList.clear();
//                for (DataSnapshot locationSnapshot: snapshot.getChildren()) {
//                    String name = locationSnapshot.child("Name").getValue(String.class);
//
//                    if(name != null){
//                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
//                        scheduleList.add(name);
//                    }
//
//                }}
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }});

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        firebaseRoot = FirebaseDatabase.getInstance().getReference();
        firebaseRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                address = dataSnapShot.child(uid).child("Schedules").child("My Schedule").child("Address One").getValue().toString();
                city = dataSnapShot.child(uid).child("Schedules").child("My Schedule").child("City").getValue().toString();
                state = dataSnapShot.child(uid).child("Schedules").child("My Schedule").child("State").getValue().toString();
                zip = dataSnapShot.child(uid).child("Schedules").child("My Schedule").child("Postal Code").getValue().toString();

                //make the
                fullAddress.setText(address + ", " + city + ", " + state + " " + zip);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fullAddress = findViewById(R.id.textView6);

    }
//
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(parent.getId() == R.id.spinner4)
//        {
//            String text = parent.getItemAtPosition(position).toString();
//            scheduleName = text;
//        }
//    }
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }


}
