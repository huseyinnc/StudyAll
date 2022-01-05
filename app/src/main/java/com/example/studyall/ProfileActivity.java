package com.example.studyall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String UserID;

    private ImageButton logout;
    private ImageButton notesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = (ImageButton) findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });

        notesButton = (ImageButton) findViewById(R.id.notesButton);
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,NotesActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();

        final TextView endProfilename = (TextView) findViewById(R.id.endProfilename);
        final TextView endProfilesurname = (TextView) findViewById(R.id.endProfilesurname);
        final TextView endProfileage = (TextView) findViewById(R.id.endProfileage);
        final TextView endProfileemail = (TextView) findViewById(R.id.endProfileemail);

        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String nameUP = userProfile.nameUP;
                    String surnameUP = userProfile.surnameUP;
                    String ageUP = userProfile.ageUP;
                    String emailUP = userProfile.emailUP;

                    endProfilename.setText(nameUP);
                    endProfilesurname.setText(surnameUP);
                    endProfileage.setText(ageUP);
                    endProfileemail.setText(emailUP);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();

            }
        });
    }
}