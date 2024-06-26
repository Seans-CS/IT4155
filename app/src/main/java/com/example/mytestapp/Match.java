package com.example.mytestapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Match extends Fragment {

    private TextView textViewName;
    private TextView textViewMajor;
    private ImageView imageViewProfile;

    private static final String TAG = "MatchFragment";

    private int currentUserIdIndex = 0;
    private List<UserMatch> userList = new ArrayList<>();

    // Manually created values for q1 to q16 for testing
    private int[] manualValues = {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        // Initialize TextViews and ImageView
        textViewName = rootView.findViewById(R.id.textViewName);
        textViewMajor = rootView.findViewById(R.id.textviewMajor);
        imageViewProfile = rootView.findViewById(R.id.image_profile);

        // Initialize Yes and No buttons
        Button btnYes = rootView.findViewById(R.id.btn_yes);
        Button btnNo = rootView.findViewById(R.id.btn_no);

        // Calculate similarity scores and populate userList
        calculateSimilarityScores();

        // Set onClickListener for Yes button
        btnYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment currentUserIdIndex
                currentUserIdIndex++;
                // Display next user
                displayNextUser();
            }
        });

        // Set onClickListener for No button
        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment currentUserIdIndex
                currentUserIdIndex++;
                // Display next user
                displayNextUser();
            }
        });

        // Display user information for the first user
        displayNextUser();

        return rootView;
    }

    private void calculateSimilarityScores()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String imageUrl = userSnapshot.child("image").getValue(String.class);
                    String major = userSnapshot.child("major").getValue(String.class);
                    // Retrieve user's questionnaire responses
                    int[] userValues = new int[16];
                    for (int i = 1; i <= 16; i++) {
                        userValues[i - 1] = userSnapshot.child("q" + i).getValue(Integer.class);
                    }

                    // Calculate similarity score
                    int similarityScore = calculateSimilarityScore(userValues);

                    // Store user data and similarity score in userList
                    UserMatch userMatch = new UserMatch(name, similarityScore, imageUrl, major);
                    userList.add(userMatch);
                }

                // Sort userList based on similarity score
                Collections.sort(userList, new Comparator<UserMatch>()
                {
                    @Override
                    public int compare(UserMatch o1, UserMatch o2)
                    {
                        return Integer.compare(o2.getSimilarityScore(), o1.getSimilarityScore());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void displayNextUser()
    {
        // Check if userList is not empty
        if (!userList.isEmpty())
        {
            // Check if currentUserIdIndex is within bounds of userList
            if (currentUserIdIndex >= userList.size())
            {
                // Reset currentUserIdIndex to 0 to start from the beginning
                currentUserIdIndex = 0;
            }
            // Display user information for the next user in the sorted list
            UserMatch user = userList.get(currentUserIdIndex);
            textViewName.setText(user.getName() + " - Similarity: " + user.getSimilarityScore());
            textViewMajor.setText("Major: " + user.getMajor());
            // Load and display image
            Picasso.get().load(user.getImageUrl()).into(imageViewProfile);
        } else
        {
            // Display a message indicating that no users are available
            textViewName.setText("No users available");
        }
    }

    private int calculateSimilarityScore(int[] userValues)
    {
        // Calculate similarity score between user's values and manual values
        int score = 0;
        for (int i = 0; i < 16; i++)
        {
            if (userValues[i] == manualValues[i])
            {
                score++;
            }
        }
        return score;
    }

    class UserMatch {
        private String name;
        private int similarityScore;
        private String imageUrl;
        private String major;

        public UserMatch(String name, int similarityScore, String imageUrl, String major)
        {
            this.name = name;
            this.similarityScore = similarityScore;
            this.imageUrl = imageUrl;
            this.major = major;
        }

        public String getName()
        {
            return name;
        }

        public int getSimilarityScore()
        {
            return similarityScore;
        }

        public String getImageUrl()
        {
            return imageUrl;
        }

        public String getMajor()
        {
            return major;
        }
    }
}
