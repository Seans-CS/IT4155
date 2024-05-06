package com.example.mytestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytestapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Preferences extends AppCompatActivity {

    TextInputEditText uploadFirstName, uploadLastName, uploadAge, uploadMajor, uploadGender, uploadBio;
    Button saveButton;
    FirebaseDatabase db;
    DatabaseReference reference;
    FirebaseAuth auth;

    FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        auth = FirebaseAuth.getInstance();

        uploadFirstName = findViewById(R.id.firstName);
        uploadLastName = findViewById(R.id.lastName);
        uploadAge = findViewById(R.id.userAge);
        uploadMajor = findViewById(R.id.userMajor);
        uploadBio = findViewById(R.id.userBio);
        uploadGender = findViewById(R.id.userGender);
        saveButton = findViewById(R.id.savePreferences);

        reference = FirebaseDatabase.getInstance().getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName, lastName, age, major, id, gender, bio;

                firstName = String.valueOf(uploadFirstName.getText());
                lastName = String.valueOf(uploadLastName.getText());
                age = String.valueOf(uploadAge.getText());
                major = String.valueOf(uploadMajor.getText());
                gender = String.valueOf(uploadGender.getText());
                bio = String.valueOf(uploadBio.getText());


                Users user = new Users(firstName, lastName, age, major, gender, bio);
                id = currUser.getUid();

                reference.child("users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Preferences Successfully Updated.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Preferences.this, "Update failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
    }

}