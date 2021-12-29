package com.example.YouStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button registerArea;
    EditText passwordArea;
    EditText passwordAreaRe;
    EditText email;
    FirebaseAuth mauth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerArea = (Button)findViewById(R.id.button2);
        passwordArea = (EditText) findViewById(R.id.editTextTextPersonName3);
        passwordAreaRe = (EditText) findViewById(R.id.editTextTextPersonName4);
        email = (EditText) findViewById(R.id.editTextTextPersonName2);

        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        registerArea.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String emailstring = email.getText().toString();
                        String passwordString = passwordArea.getText().toString();
                        if(passwordString.equals(passwordAreaRe.getText().toString())){
                            Log.i("Password Validity:","Password Correct!");
                            Log.v("Password",passwordString);
                            mauth.createUserWithEmailAndPassword(emailstring, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("status", "createUserWithEmail:success");
                                        FirebaseUser user = mauth.getCurrentUser();

                                        Map<String, Object> data1 = new HashMap<>();
                                        data1.put("uid", user.getUid());
                                        data1.put("email", user.getEmail());
                                        db.collection("users").add(data1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("status", "im bobi botten");

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("error", e.getMessage());
                                            }
                                        });

                                        // to the main activity
                                        Data.switchActivity(RegisterActivity.this, UserFolderActivity.class);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("status", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "something failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.v("Email",email.getText().toString());

                    }
                }
        );

    }
}