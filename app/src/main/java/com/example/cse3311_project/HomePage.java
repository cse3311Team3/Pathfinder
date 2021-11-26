package com.example.cse3311_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomePage extends AppCompatActivity implements View.OnClickListener  {

    public CardView current_schedule, create_schedule, edit_route, view_map, list_view;


    private DatabaseReference firebaseRoot1;
    public static String address, city, state, zip, fullAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        
        // adding toolbar to the home page
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        current_schedule = (CardView) findViewById(R.id.c1);
        create_schedule = (CardView) findViewById(R.id.c2);
        edit_route = (CardView) findViewById(R.id.c3);
        view_map = (CardView) findViewById(R.id.c4);
        list_view = (CardView) findViewById(R.id.c5);

        current_schedule.setOnClickListener(this);
        create_schedule.setOnClickListener(this);
        edit_route.setOnClickListener(this);
        view_map.setOnClickListener(this);
        list_view.setOnClickListener(this);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        firebaseRoot1 = FirebaseDatabase.getInstance().getReference();
        firebaseRoot1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot3) {

                if (dataSnapShot3.child(uid).child("Schedules").exists()) {
                    address = dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Address One").getValue().toString();
                    city = dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("City").getValue().toString();
                    state = dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("State").getValue().toString();
                    zip = dataSnapShot3.child(uid).child("Schedules").child("My Schedule").child("Postal Code").getValue().toString();


                    fullAddress = address + ", " + city + ", " + state + " " + zip;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.c1:
                i = new Intent(this, CurrentSchedule.class);
                startActivity(i); // if user clicks current schedule, go to current schedule page
                break;
            case R.id.c2:
                i = new Intent(this, CreateSchedule.class);
                startActivity(i); // if user clicks create schedule, go to create schedule page
                break;
            case R.id.c3:
                i = new Intent(this, EditSchedule.class);
                startActivity(i); // if user clicks edit schedule, go to edit schedule page
                break;
            case R.id.c4:
                i = new Intent(this, ViewMap.class);
                startActivity(i); // if user clicks edit schedule, go to view map page
                break;
            case R.id.c5:
                i = new Intent(this, ListView.class);
                startActivity(i); // if user clicks list view, go to list view page
                break;
        }
    }

}
