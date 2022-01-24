package com.example.memoriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import UI.MemorieRecyclerAdapter;
import model.Memorie;
import util.MemorieApi;

public class MemoriesListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private List<Memorie> memorieList;
    private RecyclerView recyclerView;
    private MemorieRecyclerAdapter memorieRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Journal");
    private TextView emptyJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorie_list);

        firebaseAuth = firebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        memorieList = new ArrayList<>();
        emptyJournal = (TextView) findViewById(R.id.list_no_thoughts);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                // Take user to PostJournalActivity
                if (user != null && firebaseAuth != null){
                    startActivity(new Intent(MemoriesListActivity.this,
                            PostMemoriesActivity.class));
                    //finish();
                }
                break;
            case R.id.action_signout:
                // SignOut User
                if (user != null && firebaseAuth != null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(MemoriesListActivity.this,
                            MainActivity.class));
                    //finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", MemorieApi.getInstance()
                .getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot journals : queryDocumentSnapshots){
                                Memorie memorie = journals.toObject(Memorie.class); //Map journals objects to Journal model
                                memorieList.add(memorie);
                            }

                            //Invoke RecyclerView
                            memorieRecyclerAdapter = new MemorieRecyclerAdapter(MemoriesListActivity.this,
                                    memorieList);
                            recyclerView.setAdapter(memorieRecyclerAdapter);
                            memorieRecyclerAdapter.notifyDataSetChanged(); //Update itself when changes occur
                        }
                        else{
                            emptyJournal.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}