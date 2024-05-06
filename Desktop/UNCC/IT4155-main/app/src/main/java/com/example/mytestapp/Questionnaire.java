package com.example.mytestapp;

import static android.graphics.Color.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Questionnaire extends AppCompatActivity implements View.OnClickListener {

    TextView totalQuestionsTV;
    TextView questionTV;
    Button ansA, ansB, ansC, ansD;
    Button submitButton;
    DatabaseReference reference;
    FirebaseUser currUser;

    int totalQuestions = questionnaireQsAs.question.length;
    int currentQuestionIndex = 0;
    int selectedAns;

    userResponses responses = new userResponses();



    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        reference = FirebaseDatabase.getInstance().getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        totalQuestionsTV = findViewById(R.id.quest_remaining);
        questionTV = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_a);
        ansB = findViewById(R.id.ans_b);
        ansC = findViewById(R.id.ans_c);
        ansD = findViewById(R.id.ans_d);
        submitButton = findViewById(R.id.submit_button);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        totalQuestionsTV.setText(String.format("Total Questions: %d", totalQuestions));

        loadNewQuestion();




    }


    @Override
    public void onClick(View v) {

        Button clickedButton = (Button) v;

        ansA.setBackgroundColor(WHITE);
        ansB.setBackgroundColor(WHITE);
        ansC.setBackgroundColor(WHITE);
        ansD.setBackgroundColor(WHITE);


        if(clickedButton.getId() == R.id.ans_a){
            selectedAns = 1;
        }
        if(clickedButton.getId() == R.id.ans_b){
            selectedAns = 2;
        }
        if(clickedButton.getId() == R.id.ans_c){
            selectedAns = 3;
        }
        if(clickedButton.getId() == R.id.ans_d){
            selectedAns = 4;
        }

        if(clickedButton.getId() == R.id.submit_button){
            // need to store answer into database somehow
            if(currentQuestionIndex == 0){
                responses.setQ1(selectedAns);
            }
            if(currentQuestionIndex == 1){
                responses.setQ2(selectedAns);
            }
            if(currentQuestionIndex == 2){
                responses.setQ3(selectedAns);
            }
            if(currentQuestionIndex == 3){
                responses.setQ4(selectedAns);
            }
            if(currentQuestionIndex == 4){
                responses.setQ5(selectedAns);
            }
            if(currentQuestionIndex == 5){
                responses.setQ6(selectedAns);
            }
            if(currentQuestionIndex == 6){
                responses.setQ7(selectedAns);
            }
            if(currentQuestionIndex == 7){
                responses.setQ8(selectedAns);
            }
            if(currentQuestionIndex == 8){
                responses.setQ9(selectedAns);
            }
            if(currentQuestionIndex == 9){
                responses.setQ10(selectedAns);
            }
            if(currentQuestionIndex == 10){
                responses.setQ11(selectedAns);
            }
            if(currentQuestionIndex == 11){
                responses.setQ12(selectedAns);
            }
            if(currentQuestionIndex == 12){
                responses.setQ13(selectedAns);
            }
            if(currentQuestionIndex == 13){
                responses.setQ14(selectedAns);
            }
            if(currentQuestionIndex == 14){
                responses.setQ15(selectedAns);
            }

            currentQuestionIndex++;
            loadNewQuestion();
        }
        else {
            clickedButton.setBackgroundColor(GRAY);
        }

    }

    void loadNewQuestion(){
        String id;
        id = currUser.getUid();

        if(currentQuestionIndex == totalQuestions){
            reference.child("users").child(id).child("responses").setValue(responses);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        questionTV.setText(questionnaireQsAs.question[currentQuestionIndex]);
        ansA.setText(questionnaireQsAs.choices[currentQuestionIndex][0]);
        ansB.setText(questionnaireQsAs.choices[currentQuestionIndex][1]);
        ansC.setText(questionnaireQsAs.choices[currentQuestionIndex][2]);
        ansD.setText(questionnaireQsAs.choices[currentQuestionIndex][3]);
    }
}