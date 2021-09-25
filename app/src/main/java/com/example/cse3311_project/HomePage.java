package com.example.cse3311_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class HomePage extends AppCompatActivity implements View.OnClickListener  {

    public CardView current_schedule, create_schedule, edit_route, view_map, list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        
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