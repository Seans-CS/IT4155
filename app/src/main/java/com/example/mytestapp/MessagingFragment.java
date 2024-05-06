package com.example.mytestapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagingFragment extends Fragment {

    private ArrayList<String> usersNames;
    private ArrayAdapter<String> adapter;
    private DatabaseReference usersRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersNames = new ArrayList<>();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        fetchUserNamesFromFirebase(); // Call the method to fetch user names
    }

    // Define the method to fetch user names from Firebase
    private void fetchUserNamesFromFirebase() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersNames.clear(); // Clear the existing list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the first name and last name of each user
                    String firstName = snapshot.child("name").getValue(String.class);
                    // Add the full name to the list
                    usersNames.add(firstName);
                }
                adapter.notifyDataSetChanged(); // Update the ListView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        ListView listView = view.findViewById(R.id.messagesListView);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, usersNames);
        listView.setAdapter(adapter);

        // Set item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUserName = usersNames.get(position);
                String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's UID

                // Open the messaging activity with the selected user's UID and name
                Intent intent = new Intent(requireContext(), MessagingActivity.class);
                intent.putExtra("userName", selectedUserName);

                // Get the UID of the selected user from Firebase
                usersRef.orderByChild("name").equalTo(selectedUserName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String recipientUID = snapshot.getKey();
                            // Add recipient UID to the intent
                            intent.putExtra("recipientUID", recipientUID);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });
            }
        });

        return view;
    }
}
