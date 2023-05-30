package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    // Decalre
    DatabaseManager databaseManager;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ArrayList IDList, performancesList, playwrightVenueList, dateTimeAcessibilityList, ticketsList, seats;

    public static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Recieve UserID
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
        }

        // Initialise
        databaseManager = new DatabaseManager(this);

        ImageView imageView = findViewById(R.id.dashboard_logo);

        // Open Bookings
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Bookings.class));
            }
        });

        // Search Bar - Filter List based on User's input
        EditText searchbar = findViewById(R.id.dashboard_searchbar);

        // Set Values & Visibility
        IDList = databaseManager.getAllPerformancesID(searchbar.getText().toString());
        performancesList = databaseManager.getAllPerformancesByName(searchbar.getText().toString());
        playwrightVenueList = databaseManager.getAllPerformancesDetails(searchbar.getText().toString());
        dateTimeAcessibilityList = databaseManager.getAllPerformancesDetailsDateTimeAccessibility(searchbar.getText().toString());
        ticketsList = databaseManager.getAllPerformancesTickets(searchbar.getText().toString());

        recyclerView = findViewById(R.id.dashboard_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this, IDList, performancesList, playwrightVenueList, dateTimeAcessibilityList, ticketsList);
        recyclerView.setAdapter(adapter);

        // Update List of Performances
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                IDList = databaseManager.getAllPerformancesID(searchbar.getText().toString());
                performancesList = databaseManager.getAllPerformancesByName(searchbar.getText().toString());
                playwrightVenueList = databaseManager.getAllPerformancesDetails(searchbar.getText().toString());
                dateTimeAcessibilityList = databaseManager.getAllPerformancesDetailsDateTimeAccessibility(searchbar.getText().toString());
                ticketsList = databaseManager.getAllPerformancesTickets(searchbar.getText().toString());

                adapter.updateList(IDList, performancesList, playwrightVenueList, dateTimeAcessibilityList, ticketsList);
            }
        });
    }



}