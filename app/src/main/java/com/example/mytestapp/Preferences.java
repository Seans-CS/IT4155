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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Preferences extends AppCompatActivity {

    TextInputEditText uploadFirstName, uploadLastName, uploadAge, uploadMajor;
    Button saveButton;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        uploadFirstName = findViewById(R.id.firstName);
        uploadLastName = findViewById(R.id.lastName);
        uploadAge = findViewById(R.id.userAge);
        uploadMajor = findViewById(R.id.userMajor);
        saveButton = findViewById(R.id.savePreferences);

        reference = FirebaseDatabase.getInstance().getReference();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName, lastName, age, major;

                firstName = String.valueOf(uploadFirstName.getText());
                lastName = String.valueOf(uploadLastName.getText());
                age = String.valueOf(uploadAge.getText());
                major = String.valueOf(uploadMajor.getText());

                if (TextUtils.isEmpty(firstName)){
                    Toast.makeText(Preferences.this, "Enter first name", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(lastName)){
                    Toast.makeText(Preferences.this, "Enter last name", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(age)){
                    Toast.makeText(Preferences.this, "Enter age", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(major)){
                    Toast.makeText(Preferences.this, "Enter major", Toast.LENGTH_SHORT).show();
                }

                Users user = new Users(firstName, lastName, age, major);

                reference.child("users").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Preferences Successfully Updated.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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