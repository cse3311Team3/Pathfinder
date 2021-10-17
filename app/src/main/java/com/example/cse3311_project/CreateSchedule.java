package com.example.cse3311_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button create_button;
    private Button add_location;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private EditText name_schedule, address_one, address_two, city, postal_code, country;
    private Button cancel, save;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        // adding toolbar to the create schedule page
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create_schedule window pop-up
        create_button = (Button)findViewById(R.id.create_schedule);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something else
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    // menu options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.user_profile)
        {
            Intent i = new Intent(this, UserProfile.class);
            startActivity(i); // if user clicks user profile, go to user profile page
        }
        else if (id == R.id.settings)
        {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i); // if user clicks settings, go to settings page
        }
        else if (id == R.id.about)
        {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i); // if user clicks about, go to about page
        }
        else if (id == R.id.log_out)
        {
            Context context = getApplicationContext();
            CharSequence text = "Successfully logged out - left authentication!"; //firebase authentication left
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i); //go to login page
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    // Dialog for creating new schedule
    public void createNewLocationDialog()
    {
        dialogbuilder = new AlertDialog.Builder(this);
        final View location_popup = getLayoutInflater().inflate(R.layout.popup, null);
        name_schedule = (EditText) location_popup.findViewById(R.id.name_schedule);
        address_one = (EditText) location_popup.findViewById(R.id.address_one);
        address_two = (EditText) location_popup.findViewById(R.id.address_two);
        city= (EditText) location_popup.findViewById(R.id.city);
        postal_code = (EditText) location_popup.findViewById(R.id.postal);
        country = (EditText) location_popup.findViewById(R.id.country);

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}