package com.example.cse3311_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .enableDarkMode(true)
                .setDescription("An android application that makes day to day trips more efficient by prioritizing the places the user inputs to save travel time and distance.")
                .setImage(R.drawable.pathfinder)
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("cse3311.team3@gmail.com")
                .addGitHub("cse3311Team3")
                .addPlayStore("com.example.cse3311_project")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);


    }
    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Pathfinder team.", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);;
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
   }
}