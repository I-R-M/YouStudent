package com.example.YouStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;

public class SharedFolderActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    public FirebaseAuth auth;
    private StorageReference storageReference;
    public String m_Text = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_folder);
        linearLayout = findViewById(R.id.shared_linear_layout);
        auth = FirebaseAuth.getInstance();
        Data.isShared = true;
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference listRef = storageReference.child("shared/");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.d("prefix", prefix.getName());
                            //TODO Clicking on the found folders crashes.
                            //TODO the idea is that once the other user accepts the invite (still need to figure it out), he then changes
                            //TODO the name in the folder from his UID to his email. (Just because you can't actually get another users email from UID).
                            //TODO this way the folder will be in a nice name once both users accept
                            if(!prefix.getName().contains(auth.getUid()))
                                continue;
                            //Change here is to dispaly the emails in a nice manner.
                                Data.createButton(SharedFolderActivity.this, linearLayout, prefix.getName());

                        }

                        for (StorageReference item : listResult.getItems()) {
                            Log.d("item", item.getName());
                            Data.createButtonItem(SharedFolderActivity.this, linearLayout, item.getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debug shared",e.getMessage());
                        // Uh-oh, an error occurred!
                    }
                });

        ImageButton addPhoto = (ImageButton) findViewById(R.id.sharedAddPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Data.switchActivity(SharedFolderActivity.this, UserActivity.class);
            }
        });
        ImageButton addFolder = (ImageButton) findViewById(R.id.sharedaddFolder);
        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(SharedFolderActivity.this);
                builder.setTitle("folder name");

                // Set up the input
                final EditText input = new EditText(SharedFolderActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if(m_Text != "")
                        {
                            Data.createButton(SharedFolderActivity.this, linearLayout, m_Text);
                            byte [] arr = {};
                            storageReference.child(Data.getSharedPath()+"/"+m_Text+"/temp.txt").putBytes(arr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //storageReference.child(auth.getUid()+"/"+m_Text+"/temp.txt").delete();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
        ImageButton returnbutton = findViewById(R.id.sharedreturnbutton);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Data.getSharedPath().equals("shared") || Data.getSharedPath().equals("shared/")) {
                    Data.isShared = false;
                    Data.switchActivity(SharedFolderActivity.this, UserFolderActivity.class);
                } else {
                    Data.removeOneFromSharedPath();
                    Data.getSharedSubButtons("", SharedFolderActivity.this, linearLayout);
                }
            }
        });
    }



}