package com.example.YouStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserFolderActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    FirebaseAuth auth;
    private StorageReference storageReference;
    public String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.isShared = false;
        setContentView(R.layout.activity_user_folder);
        linearLayout = findViewById(R.id.linear_layout);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //get all files from storageReference
        StorageReference listRef = storageReference.child(auth.getUid() + "/");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {

                            Log.d("prefix", prefix.getName());

                            Data.createButton(UserFolderActivity.this, linearLayout, prefix.getName());

                        }

                        for (StorageReference item : listResult.getItems()) {
                            Log.d("item", item.getName());

                            Data.createButtonItem(UserFolderActivity.this, linearLayout, item.getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

        ImageButton addPhoto = (ImageButton) findViewById(R.id.addPicture);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                 Data.switchActivity(UserFolderActivity.this, UserActivity.class);
            }
        });
        ImageButton addFolder = (ImageButton) findViewById(R.id.addFolder);
        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(UserFolderActivity.this);
                builder.setTitle("folder name");

                // Set up the input
                final EditText input = new EditText(UserFolderActivity.this);
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
                            Data.createButton(UserFolderActivity.this, linearLayout, m_Text);
                            byte [] arr = {};
                            storageReference.child(auth.getUid()+"/"+m_Text+"/temp.txt").putBytes(arr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        ImageButton sharedbutton = findViewById(R.id.sharedFolderButton);
        sharedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Data.switchActivity(UserFolderActivity.this,SharedFolderActivity.class);
            }
        });

        ImageButton returnbutton = findViewById(R.id.returnbutton);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.removeOneFromPath();
                Data.getSubButtons("",UserFolderActivity.this,linearLayout);
            }
        });
    }
}