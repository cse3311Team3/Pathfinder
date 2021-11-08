package com.example.cse3311_project;

import android.app.AlertDialog;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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

public class CreateSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Button create_button;
    private Button add_location;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog.Builder dialogbuilder_next;
    private AlertDialog dialog;
    private AlertDialog new_dialog;
    private EditText name_schedule, address_one, address_two, city, postal_code, country;
    private Button cancel, save;
    private Spinner spinner, spinner2;
    private Button date_bt, cancel_bt, save_bt;
    private EditText new_schedule, ocas_schedule;
    String[] temp = {"Test1", "Test2", "Test3"};
    String stateAbb, scheduleName;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot, firebaseRoot2;
    List<String> scheduleList = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRoot = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        firebaseRoot2 = FirebaseDatabase.getInstance().getReference(uid).child("Schedules");




            // adding toolbar to the create schedule page
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //create_schedule window pop-up
        create_button = (Button)findViewById(R.id.create_schedule);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createScheduleDialog();
            }
        });
        add_location = (Button)findViewById(R.id.add_locations);
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewLocationDialog();
            }
        });
    }
    // menu inflater
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
//        return true;
//    }
//
//    // menu options
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.user_profile)
//        {
//            Intent i = new Intent(this, UserProfile.class);
//            startActivity(i); // if user clicks user profile, go to user profile page
//        }
//        else if (id == R.id.settings)
//        {
//            Intent i = new Intent(this, SettingsActivity.class);
//            startActivity(i); // if user clicks settings, go to settings page
//        }
//        else if (id == R.id.about)
//        {
//            Intent i = new Intent(this, AboutActivity.class);
//            startActivity(i); // if user clicks about, go to about page
//        }
//        else if (id == R.id.log_out)
//        {
//            Context context = getApplicationContext();
//            CharSequence text = "Successfully logged out - left authentication!"; //firebase authentication left
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i); //go to login page
//        }
//
//        return true;
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner3)
        {
            String text = parent.getItemAtPosition(position).toString();
            scheduleName = text;
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
    // Dialog for creating new schedule

    public void createNewLocationDialog()
    {
        dialogbuilder = new AlertDialog.Builder(this);
        final View location_popup = getLayoutInflater().inflate(R.layout.popup, null);

        // use these later for implementation
        name_schedule = (EditText) location_popup.findViewById(R.id.name_schedule);
        address_one = (EditText) location_popup.findViewById(R.id.address_one);
        address_two = (EditText) location_popup.findViewById(R.id.address_two);
        city= (EditText) location_popup.findViewById(R.id.city);
        postal_code = (EditText) location_popup.findViewById(R.id.postal);
        country = (EditText) location_popup.findViewById(R.id.country);

        spinner2 = location_popup.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, temp);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        //Spinner for states
        spinner = location_popup.findViewById(R.id.states);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        cancel = (Button) location_popup.findViewById(R.id.cancel_button);
        save = (Button) location_popup.findViewById(R.id.save_button);

        dialogbuilder.setView(location_popup);
        dialog = dialogbuilder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // define cancel button
                Toast.makeText(getApplicationContext(), "Location Not saved.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Location saved to the schedule.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    
    // pop up the create new schedule dialogue
    public void createScheduleDialog()
    {
        dialogbuilder_next = new AlertDialog.Builder(this);
        final View schedule_popup = getLayoutInflater().inflate(R.layout.schedule_popup, null);
        new_schedule = (EditText) schedule_popup.findViewById(R.id.schedule_name);
        ocas_schedule = (EditText) schedule_popup.findViewById(R.id.schedule_ocassion);
        cancel_bt = (Button) schedule_popup.findViewById(R.id.cancel_but);
        save_bt = (Button) schedule_popup.findViewById(R.id.save_but);
        date_bt = (Button) schedule_popup.findViewById(R.id.select_date);

        dialogbuilder_next.setView(schedule_popup);
        new_dialog = dialogbuilder_next.create();
        new_dialog.show();

        date_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new datepickerdialog();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "New schedule created.", Toast.LENGTH_SHORT).show();
                new_dialog.dismiss();
            }
        });

        cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "No new schedule created.", Toast.LENGTH_SHORT).show();
                new_dialog.dismiss();
            }
        });

    }
    // display the calendar clicking the date
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
