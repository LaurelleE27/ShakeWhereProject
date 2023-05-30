package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Bookings extends AppCompatActivity {
    DatabaseManager databaseManager;
    RecyclerView recyclerView;
    DeleteAdapter adapter;
    ArrayList bookingsList, IDList, performancesList, playwrightVenueList, dateTimeAcessibilityList, ticketsList, seats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        // Initalise
        ImageView imageView = findViewById(R.id.bookings_logo);

        EditText searchbar = findViewById(R.id.dashboard_searchbar);
        TextView tickets = findViewById(R.id.textView);
        tickets.setText("");

        databaseManager = new DatabaseManager(this);

        // Initialise ArrayLists
        IDList = databaseManager.getAllPerformancesID(searchbar.getText().toString());
        performancesList = databaseManager.getAllPerformancesByName(searchbar.getText().toString());
        playwrightVenueList = databaseManager.getAllPerformancesDetails(searchbar.getText().toString());
        dateTimeAcessibilityList = databaseManager.getAllPerformancesDetailsDateTimeAccessibility(searchbar.getText().toString());
        ticketsList = databaseManager.getAllPerformancesTickets(searchbar.getText().toString());
        bookingsList = databaseManager.getBookingsID(Integer.parseInt(Dashboard.userID));

        // Populate RecylerView with Tickets
        recyclerView = findViewById(R.id.bookings_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeleteAdapter(this, bookingsList);
        recyclerView.setAdapter(adapter);

        // Open Dashboard
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bookings.this, Dashboard.class));
            }
        });
    }
}