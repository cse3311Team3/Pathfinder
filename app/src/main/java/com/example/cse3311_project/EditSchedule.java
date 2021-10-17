package com.example.cse3311_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditSchedule extends AppCompatActivity {

    private Button edit_button;
    private Button edit_location;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        // adding toolbar to the create schedule page
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit_button = (Button)findViewById(R.id.edit_schedule);
        edit_location = (Button)findViewById(R.id.edit_locations);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit_schedule_dialog
                editscheduleDialog();

            }
        });

        edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit_location_dialog

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


    public void editscheduleDialog() {
        dialogbuilder = new AlertDialog.Builder(this);
        final View edit_schedule = getLayoutInflater().inflate(R.layout.schedule_list_popup, null);

        dialogbuilder.setView(edit_schedule);
        dialog = dialogbuilder.create();
        dialog.show();

        //use these later for implementation
        Button schedule1 = (Button) edit_schedule.findViewById(R.id.schdeule1);
        Button schedule2 = (Button)edit_schedule.findViewById(R.id.schedule2);
        Button schedule3 = (Button)edit_schedule.findViewById(R.id.schedule3);
        Button save = (Button)edit_schedule.findViewById(R.id.button_save);
        Button cancel = (Button)edit_schedule.findViewById(R.id.button_cancel);

        schedule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
                //dialog.dismiss();
            }
        });

        schedule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
                //dialog.dismiss();
            }
        });

        schedule3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You can change schedule here. Implementation left", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Change Schedule Successful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Change Schedule cancelled.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
}