package com.example.studyall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signup;
    private TextView forgotPassword;
    private Button signin;
    private EditText email,password;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);

        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                startActivity(new Intent(this,SignUp.class));
                break;

            case R.id.signin:
                login();
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this,ResetPassword.class));
                break;

        }

    }

    private void login() {
        String login_email = email.getText().toString().trim();
        String login_password = password.getText().toString().trim();

        if (login_email.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(login_email).matches()){
            email.setError("Please enter a valid email!");
            email.requestFocus();
            return;
        }
        if (login_password.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if (login_password.length() < 6){
            password.setError("Min password length is 6 characters!");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(login_email,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){

                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));

                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email and verify your account!", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your email and password!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}