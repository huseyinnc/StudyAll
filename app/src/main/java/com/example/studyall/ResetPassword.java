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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText EditemailReset;
    private Button EditresetPassword;
    private ProgressBar EditprogressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditemailReset = (EditText) findViewById(R.id.emailReset);
        EditresetPassword = (Button) findViewById(R.id.resetPassword);
        EditprogressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        EditresetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String emailReset = EditemailReset.getText().toString().trim();

        if(emailReset.isEmpty()){
            EditemailReset.setError("Email is required!");
            EditemailReset.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailReset).matches()){
            EditemailReset.setError("Please provide a valid email!");
            EditemailReset.requestFocus();
            return;
        }

       EditprogressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(emailReset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ResetPassword.this,MainActivity.class));
                }else{
                    Toast.makeText(ResetPassword.this, "Try again! Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}