package com.example.cse3311_project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Button edit_button;
    private Button edit_location;
    private AlertDialog.Builder dialogbuilder, dialogbuilder2,dialogbuilder3 ;
    private AlertDialog dialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot, firebaseRoot2;
    private Spinner spinner, spinner2, spinner3, spinner4;
    private Button cancel, save, select;
    String stateAbb, scheduleName, locationName;

    List<String> scheduleList = new ArrayList();
    List<String> locationList = new ArrayList();


    EditText address_one, address_two, city, postal_code, country;

    String addressName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRoot = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        firebaseRoot.child(uid).child("Schedules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduleList.clear();
                for (DataSnapshot locationSnapshot: snapshot.getChildren()) {
                    String name = locationSnapshot.child("Name").getValue(String.class);
                    if(name != null){
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        scheduleList.add(name);
                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});


//        edit_button = (Button)findViewById(R.id.edit_schedule);
        edit_location = (Button)findViewById(R.id.edit_locations);


//        edit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //edit_schedule_dialog
//                editscheduleDialog();
//
//            }
//        });

        edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit_location_dialog
                edit_location_dialog();

            }
        });
    }


//     dialog pop up for editing schedule
//    public void editscheduleDialog() {
//        dialogbuilder = new AlertDialog.Builder(this);
//        final View edit_schedule = getLayoutInflater().inflate(R.layout.schedule_list_popup, null);
//
//        dialogbuilder.setView(edit_schedule);
//        dialog = dialogbuilder.create();
//        dialog.show();
//
//        //use these later for implementation
//        Button schedule1 = (Button) edit_schedule.findViewById(R.id.schdeule1);
////        Button schedule2 = (Button)edit_schedule.findViewById(R.id.schedule2);
////        Button schedule3 = (Button)edit_schedule.findViewById(R.id.schedule3);
////        Button save = (Button)edit_schedule.findViewById(R.id.button_save);
//        Button cancel = (Button)edit_schedule.findViewById(R.id.button_cancel);
//
//        // Delete Schedule
//        schedule1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String uid = user.getUid();
//                firebaseRoot = FirebaseDatabase.getInstance().getReference();
//                firebaseRoot.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
//
////                        if (dataSnapShot.child(uid).child("Schedules").exists()) {
////                            dataSnapShot.getRef().removeValue();
////                            Toast.makeText(getApplicationContext(), "Schedule Deleted", Toast.LENGTH_SHORT).show();
////
////                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
////
////        schedule2.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
////                //dialog.dismiss();
////            }
////        });
////
////        schedule3.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
////            }
////        });
////
////        save.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(), "Change Schedule Successful", Toast.LENGTH_SHORT).show();
////                dialog.dismiss();
////            }
////        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Change Schedule cancelled.", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//    }
    
    //dialog pop up for editing location to the existing schedule
    public void edit_location_dialog()
    {
        dialogbuilder = new AlertDialog.Builder(this);
        final View edit_location = getLayoutInflater().inflate(R.layout.schedule_list_popup, null);

        dialogbuilder.setView(edit_location);
        dialog = dialogbuilder.create();
        dialog.show();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        firebaseRoot.child(uid).child("Schedules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduleList.clear();
                for (DataSnapshot locationSnapshot: snapshot.getChildren()) {
                    String name = locationSnapshot.child("Name").getValue(String.class);
                    if(name != null){
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        scheduleList.add(name);
                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});

        spinner4 = edit_location.findViewById(R.id.schedule_spinner);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, scheduleList);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(this);

        //use these later for implementation
        Button select = (Button) edit_location.findViewById(R.id.button_select);
        Button schedule1 = (Button) edit_location.findViewById(R.id.schdeule1);
//        Button schedule2 = (Button)edit_location.findViewById(R.id.schedule2);
//        Button schedule3 = (Button)edit_location.findViewById(R.id.schedule3);
//        Button save = (Button)edit_location.findViewById(R.id.button_save);
        Button cancel = (Button)edit_location.findViewById(R.id.button_cancel);



        // implementation left for schedule 
        schedule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
                //dialog.dismiss();
                location_edit_popup(scheduleName);
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
                //dialog.dismiss();
                location_edit_popup(scheduleName);
            }
        });

//        schedule2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
//                //dialog.dismiss();
//                location_edit_popup();
//            }
//        });
//
//        schedule3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
//                location_edit_popup();
//            }
//        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Change location Successful", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Change location cancelled.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    // dialog pop up for editing location
    public void location_edit_popup(String scheduleName)
    {
        dialogbuilder = new AlertDialog.Builder(this);
        final View edit_location = getLayoutInflater().inflate(R.layout.edit_location_popup, null);
        dialogbuilder.setView(edit_location);
        dialog = dialogbuilder.create();
        dialog.show();



        Button location1 = (Button) edit_location.findViewById(R.id.location1);
        Button location2 = (Button)edit_location.findViewById(R.id.location2);
        Button location3 = (Button)edit_location.findViewById(R.id.location3);
        Button save = (Button)edit_location.findViewById(R.id.but_save);
        Button cancel = (Button)edit_location.findViewById(R.id.but_cancel);
        Toast.makeText(getApplicationContext(), scheduleName, Toast.LENGTH_SHORT).show();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        firebaseRoot.child(uid).child("Schedules").child(scheduleName).child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationList.clear();
                for (DataSnapshot locationSnapshot: snapshot.getChildren()) {
                    String name = locationSnapshot.child("Location Name").getValue(String.class);
                    if(name != null){
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        locationList.add(name);
                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});
        spinner4 = edit_location.findViewById(R.id.location_spinner);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locationList);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(this);
        // implementation left - locations listed currently
        location1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You can change location here. Implementation left", Toast.LENGTH_SHORT).show();
            }
        });

        location3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You can change location here. Implementation left", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLocationDialog(locationName, scheduleName);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Change location cancelled.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void editLocationDialog(String locationName, String scheduleName)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        dialogbuilder2 = new AlertDialog.Builder(this);
        final View location_popup = getLayoutInflater().inflate(R.layout.edit_popup, null);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.schedule_spinner)
        {
            String text = parent.getItemAtPosition(position).toString();
            scheduleName = text;
        }
        else if(parent.getId() == R.id.location_spinner)
        {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
            String text = parent.getItemAtPosition(position).toString();
            locationName = text;
        }
        else if(parent.getId() == R.id.states)
        {
            String text = parent.getItemAtPosition(position).toString();
            stateAbb = text;
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        Toast.makeText(getApplicationContext(), "Date selected - "+currentDateString+".", Toast.LENGTH_SHORT).show();
        //we can use the currentdatstring
    }
}



