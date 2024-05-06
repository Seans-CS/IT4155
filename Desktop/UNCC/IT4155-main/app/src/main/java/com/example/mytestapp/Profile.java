package com.example.mytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    Activity context;
    TextView profileName, profileBio;
    DatabaseReference db;

    FirebaseUser currUser;

    String name, bio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = rootView.findViewById(R.id.profileName);
        profileBio = rootView.findViewById(R.id.profileBio);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        String id;

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currUser != null;
        id = currUser.getUid();

        db = FirebaseDatabase.getInstance().getReference("users");

        db.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (user != null) {
                    profileName.setText(String.format("%s, %s", user.getFirstName(), user.getAge()));
                    profileBio.setText(user.getBio());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btn = getView().findViewById(R.id.buttonPreferences);
        Button logoutBtn = getView().findViewById(R.id.logoutButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Preferences.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
    }
}
