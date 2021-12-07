package com.example.YouStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class UserFolderActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        int counter = 3333;
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.d("prefix", prefix.getName());

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            Button btn = new Button(UserFolderActivity.this);
                            btn.setId(counter);
                            counter++;
                            final int id_ = btn.getId();
                            btn.setText(prefix.getName());
                            btn.setBackgroundColor(Color.rgb(0, 0, 0));
                            btn.setTextColor(Color.rgb(255, 255, 255));
                            linearLayout.addView(btn, params);
                            Button btn1 = ((Button) findViewById(id_));
                            btn1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    Toast.makeText(view.getContext(),
                                            "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }

                        for (StorageReference item : listResult.getItems()) {
                            Log.d("item", item.getName());

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            Button btn = new Button(UserFolderActivity.this);
                            btn.setId(counter);
                            counter++;
                            final int id_ = btn.getId();
                            btn.setText(item.getName());
                            btn.setBackgroundColor(Color.rgb(0, 0, 0));
                            btn.setTextColor(Color.rgb(255, 255, 255));
                            linearLayout.addView(btn, params);
                            Button btn1 = ((Button) findViewById(id_));
                            btn1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    Toast.makeText(view.getContext(),
                                            "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Button btn = new Button(UserFolderActivity.this);
        btn.setId((int)5000);
        final int id_ = btn.getId();
        btn.setText("add folder");
        btn.setBackgroundColor(Color.rgb(0, 0, 0));
        btn.setTextColor(Color.rgb(255, 255, 255));
        linearLayout.addView(btn, params);
        Button btn1 = ((Button) findViewById(id_));
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(),
                        "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

}