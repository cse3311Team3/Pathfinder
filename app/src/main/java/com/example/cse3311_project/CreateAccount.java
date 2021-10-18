package com.example.cse3311_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText userNameInput;

    Button createAccount;

    String email;
    String password;
    String firstName;
    String lastName;
    String userName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRoot = FirebaseDatabase.getInstance().getReference();
        
        // get all the edittext
        emailInput = findViewById(R.id.createEmailInput);
        passwordInput = findViewById(R.id.createPasswordInput);
        firstNameInput = findViewById(R.id.createFirstName);
        lastNameInput = findViewById(R.id.createLastName);
        userNameInput = findViewById(R.id.createUserName);
        createAccount = findViewById(R.id.createPageAccountButton);
        
        //On cicking user profile check if the fields are empty
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();
                firstName = firstNameInput.getText().toString();
                lastName = lastNameInput.getText().toString();
                userName = userNameInput.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(userName)) {
                    Toast.makeText(CreateAccount.this, "Please enter valid information", Toast.LENGTH_SHORT).show(); // firebase authentication
                } else {
                    if(password.length() < 6)
                    {
                        Toast.makeText(CreateAccount.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        createUser(email, password, firstName, lastName, userName);
                    }
                }
            }
        });
    }
    
    // create a user using firebase authentication
    // takes first name, last name, username, and email to check
    private void createUser(String email, String password, String firstName, String lastName, String userName) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccount.this, "Account created", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String uid = user.getUid();
                    firebaseRoot.child(uid).child("Account Info").child("First Name").setValue(firstName);
                    firebaseRoot.child(uid).child("Account Info").child("Last Name").setValue(lastName);
                    firebaseRoot.child(uid).child("Account Info").child("Username").setValue(userName);
                    firebaseRoot.child(uid).child("Account Info").child("Email").setValue(email);
                    startActivity(new Intent(CreateAccount.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else {
                    Toast.makeText(CreateAccount.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
