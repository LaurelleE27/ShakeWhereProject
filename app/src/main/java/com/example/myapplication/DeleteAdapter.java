package com.example.myapplication;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static java.lang.String.valueOf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.MyViewHolder>{
    // Declare
    String ticketID, performanceID, performanceName, seatID;
    private Context context;

    DatabaseManager databaseManager;

    Button delete;
    private ArrayList bookingList;
    private boolean expand = false;

    public DeleteAdapter(Context context, ArrayList bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public DeleteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookingrow, parent, false);
        databaseManager = new DatabaseManager(context.getApplicationContext());
        return new DeleteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteAdapter.MyViewHolder holder, int position) {
        // Set Values & Visibilities
        holder.expandableLayout.setVisibility(expand? View.VISIBLE : View.GONE);

        ticketID = valueOf(bookingList.get(position));
        seatID = valueOf(bookingList.get(position));

        holder.TV_seatid.setText(seatID);
        holder.seatdetails.setText(databaseManager.getSeatDetails(seatID));

        performanceID = databaseManager.getPerformanceIDFromBookings(ticketID);
        performanceName = databaseManager.getPerformanceFromBookings(seatID);

        holder.performancename.setText(performanceName);
        holder.playwrightvenue.setText(databaseManager.getPerformanceDetailsFromID(performanceID));
        holder.datetimeaccessiblilty.setText(databaseManager.getPerformanceDetails2FromID(performanceID));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Declare
        TextView TV_seatid, seatdetails, performancename, playwrightvenue, datetimeaccessiblilty;

        LinearLayout linearLayout, expandableLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialise
            TV_seatid = itemView.findViewById(R.id.row_id);
            TV_seatid.setVisibility(View.GONE);
            seatdetails = itemView.findViewById(R.id.row_seatdetails);
            performancename = itemView.findViewById(R.id.row_performance);
            playwrightvenue = itemView.findViewById(R.id.row_playwrightvenue);
            datetimeaccessiblilty = itemView.findViewById(R.id.row_datetimeacessiblity);

            linearLayout = itemView.findViewById(R.id.row_linearlayout);
            expandableLayout = itemView.findViewById(R.id.row_expandablecontent);

            delete = itemView.findViewById(R.id.row_deletebutton);

            // Expand Bookings
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // If expand = true, display expanded information
                    // If expand = false, hide expanded information
                    expand = !expand;
                    expandableLayout.setVisibility(expand ? View.VISIBLE : View.GONE);

                    //Close other linear layouts
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete Ticket
                    Toast.makeText(context.getApplicationContext(), "Delete Seat " + TV_seatid.getText().toString(), Toast.LENGTH_SHORT).show();
                    databaseManager.deleteTickets(Dashboard.userID, TV_seatid.getText().toString());

                    // Update List
                    updateList (context, bookingList);
                }
            });
        }
    }

    public void updateList (Context context, ArrayList bookingList){
        this.context = context;
        this.bookingList = databaseManager.getBookingsID(Integer.parseInt(Dashboard.userID));
        notifyDataSetChanged();
    }



}
