package com.example.YouStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        TextView textView = findViewById(R.id.showUser);
        Button createButton = findViewById(R.id.createShareUserButton);
        Button searchButton = findViewById(R.id.searchButton);

        EditText searchView = (EditText) findViewById(R.id.searchBar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel((notificationChannel));
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                CollectionReference ref = firestore.collection("users");
                ref.whereEqualTo("email", searchView.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    if(task.getResult().size() == 0)
                                    {
                                        textView.setText("no such user with this email was found!");
                                        createButton.setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                        String email = doc.get("email").toString();
                                        if(email.equals(auth.getCurrentUser().getEmail()))
                                        {
                                            textView.setText("Can't make shared folder with yourself");
                                            createButton.setVisibility(View.INVISIBLE);
                                        }
                                        else{
                                            textView.setText(email);
                                            createButton.setVisibility(View.VISIBLE);
                                            createButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String otheruid = doc.get("uid").toString();
                                                    ArrayList<String> uids = new ArrayList<>();
                                                    uids.add(otheruid);
                                                    uids.add(auth.getUid());
                                                    Collections.sort(uids);
                                                    String folderName = uids.get(0) + "_" +  uids.get(1);
                                                    byte[] arr = {};
                                                    FirebaseStorage.getInstance().getReference().child("shared/" + folderName + "/temp.txt").putBytes(arr)
                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            Toast.makeText(SearchActivity.this, "Folder created successfully.",
                                                                    Toast.LENGTH_LONG).show();
                                                            //push notification

                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(SearchActivity.this,"notification");
                                                            builder.setSmallIcon(R.drawable.ic_action_notification);
                                                            builder.setContentTitle("Added new shared folder");
                                                            builder.setContentText("new folder with " +textView.getText().toString());
                                                            builder.setAutoCancel(true);
                                                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(SearchActivity.this);
                                                            managerCompat.notify(Data.buttonIdCounter,builder.build());
                                                            Data.buttonIdCounter++;

                                                            Data.switchActivity(SearchActivity.this, SharedFolderActivity.class);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SearchActivity.this, "couldn't create folder. please try again",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
            }
        });
    }
}