package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseManager = new DatabaseManager(this);

        // User clicks Log In Button
        binding.loginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Username and Password
                String username = binding.loginUsername.getText().toString().trim();
                String password = binding.loginPassword.getText().toString().trim();

                // Validate the Username and Password
                if(username.equals("")||password.equals("")) {
                    Toast.makeText(Login.this, "Enter your Username and Password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the Username exist
                Boolean userExists = databaseManager.checkUser(username, password);

                if(userExists == true){
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    String userID = databaseManager.getUserID(username);
                    //Toast.makeText(Login.this, "User ID is " + userID, Toast.LENGTH_SHORT).show();

                    // Open Dashboard.
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login.this, "Incorrect Username or Password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Open Signup
        binding.loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}
