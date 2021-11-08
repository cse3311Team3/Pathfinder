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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CurrentSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_schedule);

        // adding toolbar to the current schedule page
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        
        // schedule buttons - implementation left
        Button current_schedule1 = (Button)findViewById(R.id.schdeule1);
        current_schedule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You can see your current schedule here. Implementation Left", Toast.LENGTH_SHORT).show();
            }
        });

        Button current_schedule2 = (Button)findViewById(R.id.schedule2);
        current_schedule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You can see your current schedule here. Implementation Left", Toast.LENGTH_SHORT).show();
            }
        });

        Button current_schedule3 = (Button)findViewById(R.id.schedule3);
        current_schedule3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You can see your current schedule here. Implementation Left", Toast.LENGTH_SHORT).show();
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
}
