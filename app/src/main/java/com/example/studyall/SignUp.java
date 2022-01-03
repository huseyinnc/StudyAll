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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private TextView bannerUP;
    private Button signupUP;
    private EditText nameUP,surnameUP,ageUP,emailUP,passwordUP;
    private ProgressBar progressBarUP;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        bannerUP = (TextView) findViewById(R.id.bannerUP);
        bannerUP.setOnClickListener(this);

        signupUP = (Button) findViewById(R.id.signupUP);
        signupUP.setOnClickListener(this);

        nameUP = (EditText) findViewById(R.id.nameUP);
        surnameUP = (EditText) findViewById(R.id.surnameUP);
        ageUP = (EditText) findViewById(R.id.ageUP);
        emailUP = (EditText) findViewById(R.id.emailUP);
        passwordUP = (EditText) findViewById(R.id.passwordUP);

        progressBarUP = (ProgressBar) findViewById(R.id.progressBarUP);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bannerUP:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.signupUP:
                signupUP();
                break;
        }

    }

    private void signupUP() {
        String name = nameUP.getText().toString().trim();
        String surname = surnameUP.getText().toString().trim();
        String age = ageUP.getText().toString().trim();
        String email = emailUP.getText().toString().trim();
        String password = passwordUP.getText().toString().trim();



        if(name.isEmpty()){
            nameUP.setError("Name is required!");
            nameUP.requestFocus();
            return;
        }

        if(surname.isEmpty()){
            surnameUP.setError("Surname is required!");
            surnameUP.requestFocus();
            return;
        }

        if(age.isEmpty()){
            ageUP.setError("Age is required!");
            ageUP.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailUP.setError("Surname is required!");
            emailUP.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordUP.setError("Email is required!");
            passwordUP.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUP.setError("Please provide valid email!");
            emailUP.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordUP.setError("Min password length should be 6 characters!" );
            passwordUP.requestFocus();
            return;
        }

        progressBarUP.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(name,surname,age,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBarUP.setVisibility(View.GONE);

                                        //redirect to login layout
                                    }else{
                                        Toast.makeText(SignUp.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressBarUP.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(SignUp.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBarUP.setVisibility(View.GONE);
                        }
                    }
                });

    }
}