package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Homepage extends AppCompatActivity {

    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        databaseManager = new DatabaseManager(this);

        ImageView imageView = findViewById(R.id.homepage_logo);
        Button signupButton = (Button)findViewById(R.id.homepage_signup);
        Button loginButton = (Button)findViewById(R.id.homepage_login);

        //Toast.makeText(getApplicationContext(), "Tap the logo to begin.", Toast.LENGTH_SHORT).show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Welcome to ShakeWhere!", Toast.LENGTH_SHORT).show();
                databaseManager.dropAll();
                databaseManager.createTables();
                databaseManager.populateTables();
                imageView.setOnClickListener(null);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, Signup.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, Login.class));
            }
        });
    }
}