package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivitySignupBinding;

public class Signup extends AppCompatActivity {

        ActivitySignupBinding binding;
        DatabaseManager databaseManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            binding = ActivitySignupBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            databaseManager = new DatabaseManager(this);

            // User clicks Sign Up Button
            binding.signupSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Get Username and Password.
                    String firstname = binding.signupFirstname.getText().toString().trim();
                    String lastname = binding.signupLastname.getText().toString().trim();
                    String username = binding.signupUsername.getText().toString().trim();
                    String password = binding.signupPassword.getText().toString().trim();

                    // Validate the Username and Password.
                    if(firstname.equals("")||lastname.equals("")||username.equals("")||password.equals("")) {
                        if(firstname.equals("")) {
                            Toast.makeText(Signup.this, "Enter your Firstname.", Toast.LENGTH_SHORT).show();
                        } else if (lastname.equals("")) {
                            Toast.makeText(Signup.this, "Enter your Lastname.", Toast.LENGTH_SHORT).show();
                        } else if (username.equals("")) {
                            Toast.makeText(Signup.this, "Enter your Username.", Toast.LENGTH_SHORT).show();
                        } else if (password.equals("")) {
                            Toast.makeText(Signup.this, "Enter your Password.", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    }

                    // Check if the Username exists.
                    Boolean userExists = databaseManager.findUser(username);
                    //Toast.makeText(Signup.this, "UserExists: " + String.valueOf(userExists), Toast.LENGTH_SHORT).show();

                    if(userExists){
                        // If the Username exists, output an error.
                        Toast.makeText(Signup.this, "This Username is taken.", Toast.LENGTH_SHORT).show();
                    }else{
                        // If the Username doesn't exists, create a User.
                        Boolean userCreated = databaseManager.createUser(firstname, lastname, username, password);
                        //Toast.makeText(Signup.this, "userCreated: " + String.valueOf(userCreated), Toast.LENGTH_SHORT).show();

                        if(userCreated){
                            // If the User is created, output a success message.
                            Toast.makeText(Signup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();

                            String userID = databaseManager.getUserID(username);
                            //Toast.makeText(Signup.this, "User ID is " + userID, Toast.LENGTH_SHORT).show();

                            // Open Dashboard.
                            Intent intent = new Intent(Signup.this, Dashboard.class);
                            intent.putExtra("userID", userID);
                            startActivity(intent);
                        } else {
                            // If the User is not created, output a failure message.
                            Toast.makeText(Signup.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            // Open Login.
            binding.signupAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Signup.this, Login.class);
                    startActivity(intent);
                }
            });
        }
}
