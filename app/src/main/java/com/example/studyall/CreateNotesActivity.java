package com.example.studyall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNotesActivity extends AppCompatActivity {

    EditText mcreatetitlenote,mcreatecontentnote;
    FloatingActionButton msavenotefab;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        mcreatetitlenote = findViewById(R.id.createtitlenote);
        mcreatecontentnote = findViewById(R.id.createcontentnote);
        msavenotefab = findViewById(R.id.savenotefab);

        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        msavenotefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreatetitlenote.getText().toString();
                String content = mcreatecontentnote.getText().toString();
                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Both field are require!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //match the notesactivitiy about collection and document
                    DocumentReference documentReference = firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String, Object> notes = new HashMap<>();
                    notes.put("title",title);
                    notes.put("content",content);

                    documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Note created succesfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNotesActivity.this,NotesActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create note!", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(CreateNotesActivity.this,NotesActivity.class));
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}