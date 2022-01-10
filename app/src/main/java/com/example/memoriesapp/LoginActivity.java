package com.example.memoriesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.MemorieApi;

public class LoginActivity extends AppCompatActivity {

    private Button createAcctButton;
    private Button loginButton;

    private AutoCompleteTextView emailAddress;
    private EditText password;
    ProgressBar progressBar;

    //Firebase instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = firebaseAuth.getInstance();

        createAcctButton = (Button) findViewById(R.id.create_acct_button_login);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        emailAddress = (AutoCompleteTextView) findViewById(R.id.email) ;
        password = (EditText) findViewById(R.id.password);

        createAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        CreateAccountActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = emailAddress.getText().toString().trim();
                String loginPassword = password.getText().toString().trim();

                loginEmailPasswordUser(loginEmail, loginPassword);
            }
        });
    }

    private void loginEmailPasswordUser(String loginEmail, String loginPassword) {

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)){
            firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String currentUserId = user.getUid();

                            collectionReference.whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error != null){

                                            }

                                            assert  value != null;
                                            if (!value.isEmpty()){

                                                progressBar.setVisibility(View.INVISIBLE);

                                                for (QueryDocumentSnapshot snapshot : value) {
                                                    MemorieApi memorieApi = MemorieApi.getInstance();
                                                    memorieApi.setUsername(snapshot.getString("username"));
                                                    memorieApi.setUserId(snapshot.getString("userId"));

                                                    //Go to JournalListActivity
                                                    startActivity(new Intent(LoginActivity.this,
                                                            MemoriesListActivity.class));
                                                    finish();
                                                }
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Empty fields not allowed",
                    Toast.LENGTH_LONG).show();
        }
    }
}