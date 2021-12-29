package com.example.YouStudent;

import static com.example.YouStudent.Data.getSharedSubButtons;
import static com.example.YouStudent.Data.getSubButtons;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ButtonFunctionalty implements View.OnClickListener {
    public AppCompatActivity activity;
    public LinearLayout linearLayout;
    public String buttonname;
    public  LinearLayout.LayoutParams params;
    public ButtonFunctionalty( String buttonname,AppCompatActivity activity, LinearLayout linearLayout)
    {
        this.params =new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.buttonname = buttonname;
        this.activity = activity;
        this.linearLayout = linearLayout;
    }
    @Override
    public void onClick(View v) {
        if(Data.isShared){
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference ref = firestore.collection("users");
            String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String arr [] = buttonname.split("_");
            TextView txt = ((TextView)activity.findViewById(R.id.shared_directorydisplay));// .setText(temp.toCharArray(),0,temp.length());
            txt.setText(txt.getText().toString()+"/"+buttonname);
            ref.whereEqualTo("email", mail.equals(arr[0])? arr[1]:arr[0])
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                String uid = doc.get("uid").toString();
                                ArrayList<String> uids = new ArrayList<>();
                                uids.add(uid);
                                uids.add(FirebaseAuth.getInstance().getUid());
                                Collections.sort(uids);
                                String folderName = uids.get(0) + "_" +  uids.get(1);
                                getSharedSubButtons(folderName,activity,linearLayout);
                            }
                        }
                    });
        }
        else {
        getSubButtons(buttonname,activity,linearLayout);
        }
    }
}
