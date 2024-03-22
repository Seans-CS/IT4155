package com.example.mytestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity  extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });



      bottomNavigationView = findViewById(R.id.bottomNav);
      viewPager2 = findViewById(R.id.viewPager);
      viewPagerAdapter = new ViewPagerAdapter(this);
      viewPager2.setAdapter(viewPagerAdapter);
      bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
          @SuppressLint("NonConstantResourceId")
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              int id = item.getItemId();
              if (id == R.id.bottom_nav_profile){
                  viewPager2.setCurrentItem(0);
              }
              else if (id == R.id.bottom_nav_match){
                  viewPager2.setCurrentItem(1);
              }
              else if (id == R.id.bottom_nav_messages){
                  viewPager2.setCurrentItem(2);
              }
              return false;
          }
      });

      viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageSelected(int position) {
              switch (position){
                  case 0:
                      bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);
                      break;
                  case 1:
                      bottomNavigationView.getMenu().findItem(R.id.bottom_nav_match).setChecked(true);
                      break;
                  case 2:
                      bottomNavigationView.getMenu().findItem(R.id.bottom_nav_messages).setChecked(true);
                      break;
              }
              super.onPageSelected(position);
          }
      });
    }
}
