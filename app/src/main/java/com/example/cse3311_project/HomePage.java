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


public class HomePage extends AppCompatActivity implements View.OnClickListener  {

    public CardView current_schedule, create_schedule, edit_route, view_map, list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.user_profile)
        {
            Intent i = new Intent(this, UserProfile.class);
            startActivity(i);
        }
        else if (id == R.id.settings)
        {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.about)
        {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        }
        else if (id == R.id.log_out)
        {
            Context context = getApplicationContext();
            CharSequence text = "Successfully logged out - left authentication!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.c1:
                i = new Intent(this, CurrentSchedule.class);
                startActivity(i);
                break;
            case R.id.c2:
                i = new Intent(this, CreateSchedule.class);
                startActivity(i);
                break;
            case R.id.c3:
                i = new Intent(this, EditSchedule.class);
                startActivity(i);
                break;
            case R.id.c4:
                i = new Intent(this, ViewMap.class);
                startActivity(i);
                break;
            case R.id.c5:
                i = new Intent(this, ListView.class);
                startActivity(i);
                break;
        }
    }

}