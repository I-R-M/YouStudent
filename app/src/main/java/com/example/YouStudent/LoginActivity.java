package com.example.YouStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText passwordarea;
    EditText emailarea;
    Button login;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth = FirebaseAuth.getInstance();
        passwordarea = (EditText) findViewById(R.id.editTextTextPassword);
        emailarea = (EditText) findViewById(R.id.editTextTextPersonName);
        login = (Button) findViewById(R.id.button);
        login.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String passwordstring = passwordarea.getText().toString();
                        String emailstring = emailarea.getText().toString();

                        mauth.signInWithEmailAndPassword(emailstring, passwordstring)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("status", "signInWithEmail:success");
                                            FirebaseUser user = mauth.getCurrentUser();
                                            // switchActivity(UserActivity.class);
                                            switchActivity(UserFolderActivity.class);
                                            // updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("status", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            // updateUI(null);
                                        }
                                    }
                                });
                    }
                });

    }
    private void switchActivity(Class c)
    {
        Intent i = new Intent(this,c);
        startActivity(i);
    }

}
