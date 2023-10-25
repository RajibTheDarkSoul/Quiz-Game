package com.example.quizgame;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz_Page extends AppCompatActivity implements View.OnClickListener{
    TextView time,correct,wrong;
    TextView question,a, b, c, d;

    Button next,finish;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=database.getReference().child("Questions");
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user= auth.getCurrentUser();
    DatabaseReference databaseReferenceSecond=database.getReference();

    String userAnswer,quizQuestion, quizAnswerA,quizAnswerB,quizAnswerC,quizAnswerD,correctAnswer;
    int questionCount,questionNumber=1,userCorrect=0,userWrong=0;

    ProgressBar progressBar;



    CountDownTimer countDownTimer;
    private  static long TOTAL_TIME=25000;
    Boolean timerContinue;
    long timeLeft=TOTAL_TIME;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        time=findViewById(R.id.textViewTime);
        correct=findViewById(R.id.textViewCorrect);
        wrong=findViewById(R.id.textViewWrong);

        question=findViewById(R.id.textViewQuestion);
        a=findViewById(R.id.textViewA);
        b=findViewById(R.id.textViewB);
        c=findViewById(R.id.textViewC);
        d=findViewById(R.id.textViewD);

        next=findViewById(R.id.buttonNext);
        finish=findViewById(R.id.buttonFinish);
        progressBar=findViewById(R.id.progressBar3);


        game();


        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);
        d.setOnClickListener(this);

        next.setOnClickListener(this);
        finish.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.textViewA)
        {
            pauseTimer();
            userAnswer="A";
            setClickabilityOff();
            a.setAlpha(1.0f);

            if (correctAnswer.equals(userAnswer))
            {

                a.setBackgroundColor(Color.parseColor("#28a326"));
                userCorrect++;
                correct.setText("Correct Answer: "+String.valueOf(userCorrect));
            }
            else {
                a.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("Wrong Answer: "+String.valueOf(userWrong));

                setRightText();

            }

        }

        if (view.getId()==R.id.textViewB)
        {
            pauseTimer();
            userAnswer="B";
            setClickabilityOff();
            b.setAlpha(1.0f);


            if (correctAnswer.equals(userAnswer))
            {
                b.setBackgroundColor(Color.parseColor("#28a326"));
                userCorrect++;
                correct.setText("Correct Answer: "+String.valueOf(userCorrect));
            }
            else {
                b.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("Wrong Answer: "+String.valueOf(userWrong));

                setRightText();

            }

        }

        if (view.getId()==R.id.textViewC)
        {
            pauseTimer();
            userAnswer="C";
            setClickabilityOff();
            c.setAlpha(1.0f);

            if (correctAnswer.equals(userAnswer))
            {
                c.setBackgroundColor(Color.parseColor("#28a326"));
                userCorrect++;
                correct.setText("Correct Answer: "+String.valueOf(userCorrect));
            }
            else {
                c.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("Wrong Answer: "+String.valueOf(userWrong));

                setRightText();

            }


        }

        if (view.getId()==R.id.textViewD)
        {
            pauseTimer();
            userAnswer="D";
            setClickabilityOff();
            d.setAlpha(1.0f);

            if (correctAnswer.equals(userAnswer))
            {
                d.setBackgroundColor(Color.parseColor("#28a326"));
                userCorrect++;
                correct.setText("Correct Answer: "+String.valueOf(userCorrect));
            }
            else {
                d.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("Wrong Answer: "+String.valueOf(userWrong));

                setRightText();

            }

        }

        if (view.getId()==R.id.buttonNext)
        {
            setDefaultColor();
            resetTimer();
            game();
            setClickabilityOn();
            

        }

        if (view.getId()==R.id.buttonFinish)
        {
            sendScore();
            Intent i=new Intent(Quiz_Page.this,Score_Page.class);
            startActivity(i);
            finish();

        }

    }

    private void setClickabilityOff() {
        a.setClickable(false);
        b.setClickable(false);
        c.setClickable(false);
        d.setClickable(false);

        a.setAlpha(0.5f);
        b.setAlpha(0.5f);
        c.setAlpha(0.5f);
        d.setAlpha(0.5f);
    }

    private void setClickabilityOn() {
        a.setClickable(true);
        b.setClickable(true);
        c.setClickable(true);
        d.setClickable(true);

        a.setAlpha(1.0f);
        b.setAlpha(1.0f);
        c.setAlpha(1.0f);
        d.setAlpha(1.0f);
    }



    private void setDefaultColor() {
        a.setBackgroundColor(Color.parseColor("#9807FF"));
        b.setBackgroundColor(Color.parseColor("#9807FF"));
        c.setBackgroundColor(Color.parseColor("#9807FF"));
        d.setBackgroundColor(Color.parseColor("#9807FF"));
    }

    private void setRightText() {
        if (correctAnswer.equals("A")){
            a.setBackgroundColor(Color.parseColor("#1ec21b"));
            a.setAlpha(1.0f);
        }
        else if (correctAnswer.equals("B")){
            b.setBackgroundColor(Color.parseColor("#1ec21b"));
            b.setAlpha(1.0f);
        }
        else if (correctAnswer.equals("C")){
            c.setBackgroundColor(Color.parseColor("#1ec21b"));
            c.setAlpha(1.0f);
        }
        else if (correctAnswer.equals("D")){
            d.setBackgroundColor(Color.parseColor("#1ec21b"));
            d.setAlpha(1.0f);
            
        }

    }

    public void game()
    {
        if(questionNumber==1){
        progressBar.setVisibility(View.VISIBLE);}

        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                startTimer();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);

                //getValue provides JSON data

                progressBar.setVisibility(View.GONE);
                questionCount= (int) dataSnapshot.getChildrenCount();
                
                
                quizQuestion=dataSnapshot.child(Integer.toString(questionNumber))
                        .child("Q").getValue().toString();
                quizAnswerA=dataSnapshot.child(Integer.toString(questionNumber))
                        .child("A").getValue().toString();
                quizAnswerB=dataSnapshot.child(Integer.toString(questionNumber))
                        .child("B").getValue().toString();
                quizAnswerC=dataSnapshot.child(Integer.toString(questionNumber))
                        .child("C").getValue().toString();
                quizAnswerD=dataSnapshot.child(Integer.toString(questionNumber))
                        .child("D").getValue().toString();
                correctAnswer=dataSnapshot.child(Integer.toString(questionNumber))
                        .child("answer").getValue().toString();


                question.setText(quizQuestion);
                a.setText(quizAnswerA);
                b.setText(quizAnswerB);
                c.setText(quizAnswerC);
                d.setText(quizAnswerD);

                if (questionNumber<questionCount)
                {
                    questionNumber++;
                }
                else
                {
                    Toast.makeText(Quiz_Page.this, "You're at the last question.", Toast.LENGTH_SHORT).show();
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(Quiz_Page.this, "Error:"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startTimer()
     {
        countDownTimer=new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {
                timeLeft=l;
                updateCountText();
            }

            @Override
            public void onFinish() {
                timerContinue=false;
                pauseTimer();
                setClickabilityOff();
                setRightText();
                userWrong++;
                wrong.setText("Wrong Answer: "+String.valueOf(userWrong));
                Toast.makeText(Quiz_Page.this, "Time is up!", Toast.LENGTH_SHORT).show();

            }
        }.start();

        timerContinue=true;
    }


    public void resetTimer()
    {
        timeLeft=TOTAL_TIME;
        updateCountText();

    }

    public void updateCountText()
    {
        int second=(int)(timeLeft/1000)%60;
        time.setText("Time: "+String.valueOf(second));
    }

    public void pauseTimer()
    {
        countDownTimer.cancel();
        timerContinue=false;
    }

    public void sendScore()
    {
        String userUID=user.getUid();
        databaseReferenceSecond.child("scores").child(userUID)
                .child("correct").setValue(userCorrect)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Quiz_Page.this, "Scores are saved.", Toast.LENGTH_SHORT).show();
                                
                            }
                        });

        databaseReferenceSecond.child("scores").child(userUID)
                .child("wrong").setValue(userWrong);

    }
}