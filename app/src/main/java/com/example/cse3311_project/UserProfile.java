package com.example.cse3311_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    // declaration for user avatar
    Uri imageUri;

    EditText firstNameInput;
    EditText lastNameInput;
    EditText userNameInput;

    ImageView avatarPhoto;
    Button updateAccount;
    Button cancel;
    Button avatar;

    String firstName;
    String lastName;
    String userName;

    private static final int IMAGE = 100;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRoot;

    String popFirst, popLast, popUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRoot = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();


        // get the input for the first name, last name and user name
        firebaseRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popFirst = snapshot.child(uid).child("Account Info").child("First Name").getValue().toString();
                popLast = snapshot.child(uid).child("Account Info").child("Last Name").getValue().toString();
                popUser = snapshot.child(uid).child("Account Info").child("Username").getValue().toString();
                firstNameInput.setText(popFirst);
                lastNameInput.setText(popLast);
                userNameInput.setText(popUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        avatarPhoto = findViewById(R.id.avatarProfile);
        firstNameInput = findViewById(R.id.firstNameUpdate);
        lastNameInput = findViewById(R.id.lastNameUpdate);
        userNameInput = findViewById(R.id.userNameUpdate);
        updateAccount = findViewById(R.id.updateAccountButton);
        avatar = findViewById(R.id.changeAvatar);
        cancel = findViewById(R.id.cancelAccountButton);


        // change the avatar 
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarFile();
            }
        });
        // update the account while getting all the inputs
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
    // go to user's phone camera and let user select the avatar
    private void avatarFile(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    //set the image for user avatar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE){
            imageUri = data.getData();
            avatarPhoto.setImageURI(imageUri);
        }
    }
}
