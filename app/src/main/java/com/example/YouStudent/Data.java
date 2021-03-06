package com.example.YouStudent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Data {
    public static boolean isShared = false;
    public static String path = "";
    public static String sharedPath = "";
    public static String imagename="";
    public static boolean isrootshared=false;
    public static List<Button>buttons = null;
    public static int buttonIdCounter = 3333;
    public static int lastid;
    public static void createButton(AppCompatActivity activity, LinearLayout linearLayout, String name)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Button btn = new Button(activity);
        btn.setId(Data.buttonIdCounter);
        Data.buttonIdCounter++;
        final int id_ = btn.getId();
        btn.setText(name);
        btn.setAllCaps(false);
        btn.setBackgroundResource(R.drawable.ic_action_folder);
        //btn.setBackgroundColor(Color.rgb(0, 0, 0));
        btn.setHeight(40);
        btn.setWidth(40);
        btn.setTextColor(Color.rgb(255, 255, 255));
        linearLayout.addView(btn, params);
        Button btn1 = ((Button) activity.findViewById(id_));
        btn1.setOnClickListener(new ButtonFunctionalty(btn1.getText().toString(),activity,linearLayout));

//        btn1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Data.buttons = null;
//                Thread t = Thread.currentThread();
//                getSubButtons(btn1.getText().toString(),activity,linearLayout);
//                try {
//                    t.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                linearLayout.removeAllViews();
//                for (Button b:Data.buttons) {
//                    linearLayout.addView(b,params);
//                }
//            }
//        });
    }
    public static void createButtonItem(AppCompatActivity activity, LinearLayout linearLayout, String name)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Button btn = new Button(activity);
        btn.setId(Data.buttonIdCounter);
        Data.buttonIdCounter++;
        final int id_ = btn.getId();
        btn.setBackgroundResource(R.drawable.ic_action_image);
        btn.setText(name);
        btn.setAllCaps(false);
        btn.setHeight(40);
        btn.setWidth(40);
        //btn.setBackgroundColor(Color.rgb(0, 0, 0));
        btn.setTextColor(Color.rgb(255, 255, 255));
        linearLayout.addView(btn, params);
        Button btn1 = ((Button) activity.findViewById(id_));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("item onclick","moving to display image");
                imagename =  ((Button)v).getText().toString();
                switchActivity(activity,ImageDisplayActivity.class);
            }
        });

    }

    public static void switchActivity(AppCompatActivity activity, Class c)
    {
        Intent i = new Intent(activity, c);
        activity.startActivity(i);
    }

    public static String getPath()
    {
        return FirebaseAuth.getInstance().getUid() + "/" + path;
    }
    public static String getSharedPath()
    {

        return "shared" + "/" + sharedPath;
    }
    public static void addToPath(String s)
    {
        path += s+"/";
    }
    public static void addToSharedPath(String s)
    {
        sharedPath += s+"/";
    }
    public static void removeOneFromPath()
    {
        String arr[] = path.split("/");
        String s = "";
        for (int i = 0; i < arr.length-1; i++) {
            s += arr[i]+"/";
        }
        path = s.length()>0? s.substring(0,s.length()-1):s;
    }
    public static void removeOneFromSharedPath()
    {
        String arr[] = sharedPath.split("/");
        String s = "";
        for (int i = 0; i < arr.length-1; i++) {
            s += arr[i]+"/";
        }
        sharedPath = s.length()>0? s.substring(0,s.length()-1):s;
    }
    public static void getSubButtons(String name, AppCompatActivity activity,LinearLayout linearLayout)
    {
        addToPath(name);
        String p = getPath();
        String temp = ("Directory is: "+  Data.path);
        ((TextView)activity.findViewById(R.id.directorydisplay)).setText(temp.toCharArray(),0,temp.length());
        StorageReference s = FirebaseStorage.getInstance().getReference();
        StorageReference ref = s.child(p);
        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                Log.d("onsuccess","entered on sucess");
                ArrayList<Button> buttons = new ArrayList<Button>();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                for (StorageReference prefix :listResult.getPrefixes()) {
                    Button btn = new Button(activity);
                    btn.setId(Data.buttonIdCounter);
                    btn.setAllCaps(false);
                    Data.buttonIdCounter++;
                    btn.setText(prefix.getName());
                    btn.setBackgroundResource(R.drawable.ic_action_folder);
                    btn.setTextColor(Color.rgb(255, 255, 255));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSubButtons(prefix.getName(),activity,linearLayout);
                        }
                    });
                    buttons.add(btn);
                    Log.d("onsuccess","added button");
                }
                for (StorageReference item :listResult.getItems()) {
                    Button btn = new Button(activity);
                    btn.setId(Data.buttonIdCounter);
                    Data.buttonIdCounter++;
                    btn.setBackgroundResource(R.drawable.ic_action_image);
                    btn.setText(item.getName() + ".png");

                    btn.setTextColor(Color.rgb(255, 255, 255));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagename = ((Button)v).getText().toString();
                            switchActivity(activity,ImageDisplayActivity.class);
                        }
                    });
                    buttons.add(btn);
                    Log.d("onsuccess","added button");

                }
                Log.d("onsuccess","finished with shite");
                linearLayout.removeAllViews();
                for (Button b:buttons) {
                    linearLayout.addView(b,params);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Retriving folder data","Failed getting folder data");
                Toast.makeText(activity,"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void getSharedSubButtons(String name, AppCompatActivity activity,LinearLayout linearLayout)
    {
        Data.addToSharedPath(name);
        String p = Data.getSharedPath();
        String temp = ("Directory is: "+  Data.sharedPath);
//        ((TextView)activity.findViewById(R.id.shared_directorydisplay)).setText(temp.toCharArray(),0,temp.length());
        StorageReference s = FirebaseStorage.getInstance().getReference();
        StorageReference ref = s.child(p);
        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                Log.d("onsuccess","entered on sucess");
                ArrayList<Button> buttons = new ArrayList<Button>();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                for (StorageReference prefix :listResult.getPrefixes()) {
                    Button btn = new Button(activity);
                    btn.setId(Data.buttonIdCounter);
                    Data.buttonIdCounter++;
                    btn.setAllCaps(false);
                    btn.setText(prefix.getName());
                    btn.setBackgroundResource(R.drawable.ic_action_folder);
                    btn.setTextColor(Color.rgb(255, 255, 255));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            getSharedSubButtons(prefix.getName(),activity,linearLayout);
                        }
                    });
                    buttons.add(btn);
                    Log.d("onsuccess","added button");
                }
                for (StorageReference item :listResult.getItems()) {
                    Button btn = new Button(activity);
                    btn.setId(Data.buttonIdCounter);
                    Data.buttonIdCounter++;
                    btn.setBackgroundResource(R.drawable.ic_action_image);
                    btn.setText(item.getName() + ".png");
                    btn.setTextColor(Color.rgb(255, 255, 255));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagename = ((Button)v).getText().toString();
                            switchActivity(activity,ImageDisplayActivity.class);
                        }
                    });
                    buttons.add(btn);
                    Log.d("onsuccess","added button");

                }
                Log.d("onsuccess","finished with shite");
                linearLayout.removeAllViews();
                for (Button b:buttons) {
                    linearLayout.addView(b,params);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Retriving folder data","Failed getting folder data");
                Toast.makeText(activity,"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getEmailFromFirestore(String uid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String[] email = new String[1];
        CollectionReference ref = firestore.collection("users");
        ref.whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            email[0] = doc.get("email").toString();
                        }
                    }
                });

        return email[0];
    }
}
