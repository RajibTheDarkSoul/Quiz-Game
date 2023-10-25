package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Score_Page extends AppCompatActivity {

    TextView correct,wrong;
    Button playAgain,exit;

    ProgressBar progressBar;


    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user= auth.getCurrentUser();

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference().child("scores").child(user.getUid());

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        correct=findViewById(R.id.textViewCorrected);
        wrong=findViewById(R.id.textViewWrongAns);

        playAgain=findViewById(R.id.buttonPlayAgain);
        exit=findViewById(R.id.buttonExit);
        progressBar=findViewById(R.id.progressBar4);


        getData();


        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Score_Page.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Exit the app
                finishAffinity(); // Finish all activities in the task stack
                System.exit(0); // Terminate the app process

            }
        });


    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                correct.setVisibility(View.VISIBLE);
                wrong.setVisibility(View.VISIBLE);
                playAgain.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);

                String c=snapshot.child("correct").getValue().toString();
                String w=snapshot.child("wrong").getValue().toString();

                setValues(c,w);
            }

            private void setValues(String c, String w) {
                correct.setText("Correct Answer:"+c);
                wrong.setText("Correct Answer:"+w);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Score_Page.this, "Error:"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}