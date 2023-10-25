package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {
    EditText mail;
    Button button;
    ProgressBar progressBar;
    FirebaseAuth auth=FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mail=findViewById(R.id.editTextTextForgotEmail);
        button=findViewById(R.id.buttonSendForgotEmail);
        progressBar=findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userMail=mail.getText().toString();
                resetpassword(userMail);


            }
        });
    }

    public void resetpassword(String userEmail)
    {
        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    button.setClickable(false);
                    Toast.makeText(Forgot_Password.this, "An email to reset your password has been sent.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Forgot_Password.this, "An error occurred.", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}