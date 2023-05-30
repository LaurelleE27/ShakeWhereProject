package com.example.myapplication;

import static java.lang.String.valueOf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;

    DatabaseManager databaseManager;
    private ArrayList IDList, performanceList, playwrightVenueList, dateTimeAccessiblityList, ticketsList;
    private boolean expand = false;

    int ticketsAvaliable, ticketsRequested;
    public CustomAdapter(Context context, ArrayList IDList, ArrayList performanceList, ArrayList playwrightVenueList, ArrayList dateTimeAccessiblityList, ArrayList ticketsList) {
        this.context = context;
        this.IDList = IDList;
        this.performanceList = performanceList;
        this.playwrightVenueList = playwrightVenueList;
        this.dateTimeAccessiblityList = dateTimeAccessiblityList;
        this.ticketsList = ticketsList;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        databaseManager = new DatabaseManager(context.getApplicationContext());
        return new CustomAdapter.MyViewHolder(view);
    }

    // Set Values & Visbilities
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.performanceid.setText(valueOf(IDList.get(position)));
        holder.performanceid.setVisibility(View.GONE);
        holder.performancename.setText(valueOf(performanceList.get(position)));
        holder.playwrightvenue.setText(valueOf(playwrightVenueList.get(position)));
        holder.datetimeaccessiblilty.setText(valueOf(dateTimeAccessiblityList.get(position)));
        holder.totaltickets.setText("/ " + valueOf(ticketsList.get(position)));
        holder.expandableLayout.setVisibility(expand? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return performanceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Declare
        TextView performanceid, performancename, playwrightvenue, datetimeaccessiblilty;

        EditText numberoftickets, totaltickets;
        Button book;
        LinearLayout linearLayout, expandableLayout;

        Spinner seatTypes;

        String TicketID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialise
            performanceid = itemView.findViewById(R.id.row_id);
            performancename = itemView.findViewById(R.id.row_performance);
            playwrightvenue = itemView.findViewById(R.id.row_playwrightvenue);
            datetimeaccessiblilty = itemView.findViewById(R.id.row_datetimeacessiblity);

            linearLayout = itemView.findViewById(R.id.row_linearlayout);
            expandableLayout = itemView.findViewById(R.id.row_expandablecontent);

            numberoftickets = itemView.findViewById(R.id.row_ticketstobook);
            totaltickets = itemView.findViewById(R.id.row_totaltickets);

            book = itemView.findViewById(R.id.row_bookbutton);

            // Initialise Spinner
            seatTypes = itemView.findViewById(R.id.row_seats);
            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context, R.array.seattypes, android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            seatTypes.setAdapter(arrayAdapter);

            // Expand Performances
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // If expand = true, display expanded information
                    // If expand = false, hide expanded information
                    expand = !expand;
                    expandableLayout.setVisibility(expand ? View.VISIBLE : View.GONE);
                    seatTypes.setSelection(0);
                    numberoftickets.setText("0");

                    //Close other linear layouts
                }
            });

            // Book Ticket
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ticketsAvaliable = databaseManager.getSeatAvaliablity(performanceid.getText().toString(), seatTypes.getSelectedItem().toString());
                    ticketsRequested = Integer.valueOf(numberoftickets.getText().toString());
                    Boolean avaliable = (0 <  ticketsRequested && ticketsRequested <= ticketsAvaliable);

                    //Toast.makeText(context.getApplicationContext(), "You want " + String.valueOf(ticketsRequested) + " There are "+ String.valueOf(ticketsAvaliable), Toast.LENGTH_SHORT).show();

                    //If seat and tickets are avaliable, confirm booking.
                    if (!avaliable) {
                        Toast.makeText(context.getApplicationContext(), "These tickets are not avaliable.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle(performancename.getText().toString());
                    builder.setMessage("You are purchasing " + numberoftickets.getText().toString() + " " + seatTypes.getSelectedItem().toString() +  " ticket(s) for " + performancename.getText().toString() + " on " + datetimeaccessiblilty.getText().toString().substring(0,8) + " at " + datetimeaccessiblilty.getText().toString().substring(11, 16) + ".\n\nAbout " + performancename.getText().toString() + "\n" + playwrightvenue.getText().toString() + "\n\nAccessibility Options\n" + datetimeaccessiblilty.getText().toString().substring(17));
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    for (int i = 1; i <= ticketsRequested; i++) {
                                        // Get Ticket ID
                                        TicketID = databaseManager.getTicketID(performanceid.getText().toString(), seatTypes.getSelectedItem().toString());

                                        // Book Ticket
                                        databaseManager.bookTicket(TicketID, Dashboard.userID);
                                        Toast.makeText(context.getApplicationContext(), "Booked! Ticket ID: " + TicketID, Toast.LENGTH_SHORT).show();

                                        ticketsAvaliable = databaseManager.getSeatAvaliablity(performanceid.getText().toString(), seatTypes.getSelectedItem().toString());
                                        Toast.makeText(context.getApplicationContext(), " There are " + String.valueOf(ticketsAvaliable) + " tickets left.", Toast.LENGTH_SHORT).show();

                                    }
                                    expandableLayout.setVisibility(View.GONE);
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context.getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    updateList (IDList, performanceList, playwrightVenueList, dateTimeAccessiblityList, ticketsList);
                }
            });
        }
    }

    // Update list when searchbar is used
    public void updateList (ArrayList IDList, ArrayList performanceList, ArrayList playwrightVenueList, ArrayList dateTimeAccessiblityList, ArrayList ticketsList){
        this.IDList = IDList;
        this.performanceList = performanceList;
        this.playwrightVenueList = playwrightVenueList;
        this.dateTimeAccessiblityList = dateTimeAccessiblityList;
        this.ticketsList = ticketsList;
        notifyDataSetChanged();
    }
}
