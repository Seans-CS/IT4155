package com.example.mytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Profile extends Fragment {

    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    public void onStart(){
        super.onStart();
        Button btn = (Button) context.findViewById(R.id.buttonPreferences);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Preferences.class);
                startActivity(intent);
            }
        });
    }

}