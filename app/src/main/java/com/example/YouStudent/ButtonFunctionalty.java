package com.example.YouStudent;

import static com.example.YouStudent.Data.getSharedSubButtons;
import static com.example.YouStudent.Data.getSubButtons;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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
        getSharedSubButtons(buttonname,activity,linearLayout);
        }
        else {
        getSubButtons(buttonname,activity,linearLayout);
        }
    }
}
