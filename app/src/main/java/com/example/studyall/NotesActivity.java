package com.example.studyall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {
    FloatingActionButton createnotefab;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    DatabaseReference databaseReference;

    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("All Notes");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(firebaseAuth.getCurrentUser().getUid());
        createnotefab = findViewById(R.id.createnotefab);
        createnotefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NotesActivity.this,CreateNotesActivity.class));

            }
        });


        //match the CreateNotesActivity about collection and document
        Query query = firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {

                noteViewHolder.notestitle.setText(firebasemodel.getTitle());
                noteViewHolder.notescontent.setText(firebasemodel.getContent());



            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };




        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);

    }



    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView notestitle;
        private TextView notescontent;
        LinearLayout notes;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notestitle = itemView.findViewById(R.id.notestittle);
            notescontent = itemView.findViewById(R.id.notescontent);
            notes = itemView.findViewById(R.id.notes);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuprofile:
                startActivity(new Intent(NotesActivity.this,ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
        /*FirebaseRecyclerAdapter<firebasemodel, NoteViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerOptions.Builder<firebasemodel>() {
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
                String noteID = getRef(position).getKey();
                databaseReference.child(noteID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String title = snapshot.child("title").toString();
                        String content = snapshot.child("content").toString();

                        holder.notestitle.setText(firebasemodel.getTitle());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        };

         */

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }

    }
}