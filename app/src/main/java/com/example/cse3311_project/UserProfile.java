package com.example.cse3311_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {

    EditText firstNameInput;
    EditText lastNameInput;
    EditText userNameInput;

    Button updateAccount;
    Button cancel;

    String firstName;
    String lastName;
    String userName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRoot = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        firstNameInput = findViewById(R.id.firstNameUpdate);
        lastNameInput = findViewById(R.id.lastNameUpdate);
        userNameInput = findViewById(R.id.userNameUpdate);
        updateAccount = findViewById(R.id.updateAccountButton);
        cancel = findViewById(R.id.cancelAccountButton);

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = firstNameInput.getText().toString();
                lastName = lastNameInput.getText().toString();
                userName = userNameInput.getText().toString();

                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(userName)) {
                    Toast.makeText(UserProfile.this, "Please enter valid information", Toast.LENGTH_SHORT).show(); // firebase authentication
                } else {
                    firebaseRoot.child(uid).child("Account Info").child("First Name").setValue(firstName);
                    firebaseRoot.child(uid).child("Account Info").child("Last Name").setValue(lastName);
                    firebaseRoot.child(uid).child("Account Info").child("Username").setValue(userName);
                    startActivity(new Intent(UserProfile.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
    }

}
