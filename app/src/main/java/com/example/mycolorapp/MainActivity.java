package com.example.mycolorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login_button = findViewById(R.id.button_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity(LoginActivity.class);
            }
        });
        Button register_button = findViewById(R.id.button_register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity(RegisterActivity.class);
            }
        });
    }
    private void switchActivity(Class c)
    {
        Intent i = new Intent(this,c);
        startActivity(i);
    }
}