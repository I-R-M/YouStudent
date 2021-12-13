package com.example.YouStudent;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Data {
    public static String path = "/pictures/";
    public static int buttonIdCounter = 3333;
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
        btn.setBackgroundColor(Color.rgb(0, 0, 0));
        btn.setTextColor(Color.rgb(255, 255, 255));
        linearLayout.addView(btn, params);
        Button btn1 = ((Button) activity.findViewById(id_));
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(),
                        "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public static void switchActivity(AppCompatActivity activity, Class c)
    {
        Intent i = new Intent(activity, c);
        activity.startActivity(i);
    }
}
