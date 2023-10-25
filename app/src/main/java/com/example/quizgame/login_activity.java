package com.example.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login_activity extends AppCompatActivity implements View.OnClickListener {
    EditText email,password;
    Button signin;
    SignInButton signingoogle;
    TextView signUp, forgotPassword;
    ProgressBar progressBar;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Intent> activityResultLauncher;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Game Login");
        setContentView(R.layout.activity_login);
        //register
        registerActivityForGoogleSignIn();




        email=findViewById(R.id.editTextTextLoginEmail);
        password=findViewById(R.id.editTextTextLoginPassword);

        signin=findViewById(R.id.buttonLoginSingIn);
        signingoogle=findViewById(R.id.buttonloginGoogleSignIn);

        signUp=findViewById(R.id.textViewLoginSignUp);
        forgotPassword=findViewById(R.id.textViewLoginforgotPassword);

        progressBar=findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.GONE);



        signin.setOnClickListener(this);
        signingoogle.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.buttonLoginSingIn)
        {
            String userEmail=email.getText().toString();
            String userPass=password.getText().toString();

            //Hide keyboard after clicking the button
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            signInwithFirebase(userEmail,userPass);
        }

        if(view.getId()==R.id.buttonloginGoogleSignIn)
        {
            signInWithGoogle();

        }

        if(view.getId()==R.id.textViewLoginSignUp)
        {
            Intent intent=new Intent(login_activity.this,Sign_Up_Page.class);
            startActivity(intent);


        }

        if(view.getId()==R.id.textViewLoginforgotPassword)
        {
            Intent i=new Intent(login_activity.this,Forgot_Password.class);
            startActivity(i);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser=auth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Intent i=new Intent(login_activity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void signInwithFirebase(String userEmail, String userPassword)
    {
        progressBar.setVisibility(View.VISIBLE);
        signin.setClickable(false);

        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent i=new Intent(login_activity.this,MainActivity.class);
                            startActivity(i);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(login_activity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            finish();

                        } else
                        {
                            Toast.makeText(login_activity.this, "Error:"+task.getException()
                                    .getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            signin.setClickable(true);

                        }
                    }
                });
    }


    public void signInWithGoogle()
    {
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("272185826568-jms9atf7kfjbfgacj0efab9u5cgkmbgu.apps.googleusercontent.com")
                .requestEmail().build();
        googleSignInClient=GoogleSignIn.getClient(this,gso);

        signinMethod();

    }

    public void signinMethod()
    {
        //user will choose gmail-options
        Intent signIntent=googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signIntent);

    }

    public void registerActivityForGoogleSignIn()
    {
        activityResultLauncher=registerForActivityResult
                (new ActivityResultContracts.StartActivityForResult()
                        , new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult o) {
                                int resultCode=o.getResultCode();
                                Intent data=o.getData();

                                if(resultCode==RESULT_OK && data!=null)
                                {
                                    Task<GoogleSignInAccount> task=GoogleSignIn
                                            .getSignedInAccountFromIntent(data);

                                    firebaseSignInWithGoogle(task);
                                }
                            }
                        });


    }

    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account=task.getResult(ApiException.class);
            Toast.makeText(this, "Successfully signed In.", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(login_activity.this,MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);

        } catch (ApiException e) {
            Toast.makeText(this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    private void firebaseGoogleAccount(GoogleSignInAccount account) {

        AuthCredential authCredential= GoogleAuthProvider.getCredential
                (account.getIdToken(),null);

        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {


                        }
                        else {

                        }
                    }
                });
    }


}