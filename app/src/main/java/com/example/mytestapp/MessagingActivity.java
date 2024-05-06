package com.example.mytestapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editTextMessage;
    private Button buttonSend;
    private Button backButton; // Added button for back functionality
    private RecyclerView recyclerViewMessages;
    private MessagesAdapter messagesAdapter;
    private List<Messages> messagesList;
    private DatabaseReference messagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get recipient's information from intent
        String userName = getIntent().getStringExtra("userName");
        String recipientUID = getIntent().getStringExtra("recipientUID");

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(userName);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        toolbarTitle.setTextSize(30);
        toolbarTitle.setTypeface(null, Typeface.BOLD);
        toolbarTitle.setLayoutParams(layoutParams);

        // Initialize UI elements
        textView = findViewById(R.id.toolbar_title);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        backButton = findViewById(R.id.buttonBack); // Initialize back button
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        // Initialize Firebase database reference
        messagesRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) // Current user's UID
                .child("messages").child(recipientUID);

        // Initialize RecyclerView and adapter
        messagesList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(messagesList);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messagesAdapter);

        // Set onClickListener for send button
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Set onClickListener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back when back button is clicked
            }
        });

        // Listen for changes in the messages node
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Messages message = snapshot.getValue(Messages.class);
                    if (message != null) {
                        messagesList.add(message);
                    }
                }
                messagesAdapter.notifyDataSetChanged(); // Notify adapter that data set has changed
                recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1); // Scroll to the last message
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error
                Toast.makeText(MessagingActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to send a message
    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get sender's ID (current user's ID)
        String receiverId = getIntent().getStringExtra("recipientUID");

        if (!messageText.isEmpty()) {
            // Generate a unique message ID
            String messageId = messagesRef.push().getKey();

            // Create a new Messages object
            Messages message = new Messages(messageId, senderId, receiverId, messageText, System.currentTimeMillis());

            // Push the message object to the sender's node in the database
            if (messageId != null) {
                messagesRef.child(messageId).setValue(message);
            }

            // Clear the message EditText after sending
            editTextMessage.setText("");

            // Scroll RecyclerView to the last message
            recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }
}
