package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String databaseName = "ShakeWhereDatabase.db";
    public static final int databaseVersion = 1;

    public DatabaseManager(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create tables if they don't exist
        createTables();

        //populateTables();
        //setDefaultLabel(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("drop Table if exists users");
    }

    public void setDefaultLabel(SQLiteDatabase database) {
        // create default label
        ContentValues contentValues = new ContentValues();
        contentValues.put("Firstname", "Admin");
        contentValues.put("Lastname", "Admin");
        contentValues.put("Username", "Admin");
        contentValues.put("Password", "Password");

        database.insert("Users", null, contentValues);

    }

    // TABLES
    // Create Tables
    public void createTables() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS Users(ID INTEGER PRIMARY KEY AUTOINCREMENT, Firstname TEXT, Lastname TEXT, Username TEXT UNIQUE, Password TEXT)");
        database.execSQL("create Table IF NOT EXISTS Performances(PerformanceID INTEGER PRIMARY KEY AUTOINCREMENT, Performance TEXT NOT NULL, Playwright TEXT NOT NULL, Venue TEXT NOT NULL, Street TEXT NOT NULL, City TEXT NOT NULL, Postcode TEXT NOT NULL, Date TEXT NOT NULL, Time TEXT NOT NULL, TwentySteps INTEGER NOT NULL, WheelchairAccess INTEGER NOT NULL, FlashingLights INTEGER NOT NULL, NeedConsent INTEGER NOT NULL, Consent TEXT, Tickets INTEGER NOT NULL)");
        database.execSQL("create Table IF NOT EXISTS Tickets(TicketID INTEGER PRIMARY KEY AUTOINCREMENT, PerformanceID INTEGER NOT NULL, SeatID INTEGER NOT NULL, Seat TEXT NOT NULL, Price INTEGER NOT NULL, UserID INTEGER)");
    }

    // Populate Tables
    public void populateTables() {
        createUser("Admin", "Admin", "Admin", "password");

        createPerformance("The Merchant of Venice", "William Shakespeare", "Merchant Adventurer's Hall", "Fossgate", "York", "YO1 9XD", "23/04/23", "20:00", 0, 1, 0, 0, null, 17);
        createPerformance("Hamlet", "William Shakespeare", "Clifford's Tower", "Tower St", "York", "YO1 9SA", "19/05/23", "18:00", 1, 0, 1, 1, "Fake Blood Splatter", 20);
        createPerformance("A Midsummer Night's Dream", "William Shakespeare", "Dean's Park", "2 Minster Yard", "York", "YO1 7JJ", "03/06/23", "19:00", 0, 1, 1, 0, null, 40);
        createPerformance("Oedipus", "Sophocles", "St Mary's Abbey", "Museum Gardens", "York", "YO30 7DR", "28/07/23", "20:00", 0, 1, 0, 0, null, 17);
        createPerformance("The Tempest", "William Shakespeare", "Milleneum Bridge", "38 Hospital Fields Rd", "York", "YO10 4EF", "19/08/23", "14:00", 0, 1, 0, 0, null, 20);
        createPerformance("Antigone", "Sophocles", "Crypt", "Deangate", "York", "YO1 7HH", "20/09/23", "21:00", 1, 0, 0, 0, null, 23);

        createTickets(1, "Seated", 17, 8);
        createTickets(2, "Seated", 8, 8);
        createTickets(2, "Standing", 12, 7);
        createTickets(3, "On Stage", 11, 4);
        createTickets(3, "Grass", 29, 4);
        createTickets(4, "Seated", 5, 9);
        createTickets(4, "Standing", 12, 7);
        createTickets(5, "Boat A", 6, 9);
        createTickets(5, "Boat B", 4, 9);
        createTickets(5, "Riverbank", 10, 7);
        createTickets(6, "Inner Circle", 5, 16);
        createTickets(6, "Outer Circle", 8, 13);
        createTickets(6, "Standing", 10, 10);

    }

    // Drop Tables
    public void dropAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS Users");
        database.execSQL("DROP TABLE IF EXISTS Performances");
        database.execSQL("DROP TABLE IF EXISTS Tickets");
    }

    // Delete Data from Tables
    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM Users");
        database.execSQL("DELETE FROM Tickets");
        database.execSQL("DELETE FROM Performances");
    }

    // USERS
    // Create User
    public Boolean createUser(String firstname, String lastname, String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Firstname", firstname);
        contentValues.put("Lastname", lastname);
        contentValues.put("Username", username);
        contentValues.put("Password", password);

        long result = database.insert("Users", null, contentValues);
        return result != -1;
    }

    // Search for a Username to see if it already exists
    public boolean findUser(String username) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Users where Username = ?", new String[]{username});

        Boolean userExists = cursor.getCount() > 0;
        cursor.close();

        return userExists;

    }

    // Check Username and Password
    public boolean checkUser(String username, String password) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Users where Username = ? and Password = ?", new String[]{username, password});

        Boolean userExists = cursor.getCount() > 0;

        cursor.close();

        return userExists;
    }

    // Get User's ID
    public String getUserID(String username) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Users where Username = ?", new String[]{username});

        String userID;

        if (cursor.moveToFirst()) {
            userID = cursor.getString(0);
        } else {
            userID = null;
        }

        cursor.close();

        return userID;
    }

    // PERFORMANCES
    // Create Performance
    public boolean createPerformance(String Performance, String Playwright, String Venue, String Street, String City, String Postcode, String Date, String Time, Integer TwentySteps, Integer WheelchairAccess, Integer FlashingLights, Integer NeedConsent, String Consent, Integer Tickets) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Performance", Performance);
        contentValues.put("Playwright", Playwright);
        contentValues.put("Venue", Venue);
        contentValues.put("Street", Street);
        contentValues.put("City", City);
        contentValues.put("Postcode", Postcode);
        contentValues.put("Date", Date);
        contentValues.put("Time", Time);
        contentValues.put("TwentySteps", TwentySteps);
        contentValues.put("WheelchairAccess", WheelchairAccess);
        contentValues.put("FlashingLights", FlashingLights);
        contentValues.put("NeedConsent", NeedConsent);
        contentValues.put("Consent", Consent);
        contentValues.put("Tickets", Tickets);

        long result = database.insert("Performances", null, contentValues);
        database.close();

        return result != -1;
    }

    // Create an ArrayList of Performance IDs
    public ArrayList getAllPerformancesID(String performancename) {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Performances where Performance LIKE ?", new String[]{"%" + performancename + "%"});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0));
                //Log.d("NumberofTickets", "ID " + cursor.getString(0));
            }
        }
        cursor.close();
        return result;
    }

    // Create an ArrayList of Performance Names based off the Performance Name
    public ArrayList getAllPerformancesByName(String performancename) {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Performances where Performance LIKE ?", new String[]{"%" + performancename + "%"});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(1));

                //Log.d("SearchForPerfomances", "Found " + cursor.getCount() + " performances similar to " + performancename + " - " + cursor.getString(1));
            }
        }
        cursor.close();
        return result;
    }

    // Create an ArrayList of Performance Details based off the Performance Name
    public ArrayList getAllPerformancesDetails(String performancename) {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Performances where Performance LIKE ?", new String[]{"%" + performancename + "%"});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result.add("By " + cursor.getString(2) + "\n" +
                        cursor.getString(3) + ",\n" + cursor.getString(4) + ",\n" + cursor.getString(5) + ",\n" + cursor.getString(6));
            }
        }
        cursor.close();
        return result;
    }

    public ArrayList getAllPerformancesDetailsDateTimeAccessibility(String performancename) {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Performances where Performance LIKE ?", new String[]{"%" + performancename + "%"});

        String steps, wheelchair, lights, consent;

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                if (cursor.getString(9).equals("1")) {
                    steps = "Y";
                } else {
                    steps = "N";
                }

                if (cursor.getString(10).equals("1")) {
                    wheelchair = "Y";
                } else {
                    wheelchair = "N";
                }

                if (cursor.getString(11).equals("1")) {
                    lights = "Y";
                } else {
                    lights = "N";
                }

                if (cursor.getString(12).equals("1")) {
                    consent = "Y";
                } else {
                    consent = "N";
                }
                result.add(cursor.getString(7) + " @ " + cursor.getString(8) + "\n" +
                        "Must Climb 20 Steps: " + steps + "\n" +
                        "Wheelchair Access: " + wheelchair + "\n" +
                        "Flashing Lights: " + lights + "\n" +
                        "Consent Form: " + consent);
            }
        }
        cursor.close();
        return result;
    }

    public ArrayList getAllPerformancesTickets(String performancename) {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Performances where Performance LIKE ?", new String[]{"%" + performancename + "%"});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(14));
            }
        }
        return result;
    }

    public ArrayList getAllPerformancesAvaliableTickets() {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();

        for (int performanceID = 1; performanceID <= 6; performanceID++) {
            Cursor cursor = database.rawQuery("Select * from Tickets where PerformanceID = ? and UserID IS NULL", new String[]{String.valueOf(performanceID)});

            //Log.d("NumberofTickets", "There are " + String.valueOf(cursor.getCount()) + " tickets for performance " + performanceID);
            result.add(cursor.getCount());
        }

        return result;
    }

    // TICKETS
    // Create Ticket
    public void createTickets(Integer PerformanceID, String Seat, Integer NumberOfTickets, Integer Price) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("PerformanceID", PerformanceID);
        contentValues.put("Seat", Seat);
        contentValues.put("Price", Price);

        for (int i = 1; i <= NumberOfTickets; i++) {
            contentValues.put("SeatID", i);

            long result = database.insert("Tickets", null, contentValues);
        }
        database.close();
    }

    public boolean checkSeatAvaliablity(String PerformanceID, String SeatType) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Tickets where PerformanceID = ? and Seat = ? and UserID IS NULL", new String[]{String.valueOf(PerformanceID), SeatType});

        Boolean seatsAvaliable = cursor.getCount() > 0;

        cursor.close();

        return seatsAvaliable;
    }

    public int getSeatAvaliablity(String PerformanceID, String SeatType) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Tickets where PerformanceID = ? and Seat = ? and UserID IS NULL", new String[]{String.valueOf(PerformanceID), SeatType});

        int numberofTickets = cursor.getCount();

        //Log.d("NumberofTickets", "Seat type " + SeatType + " Tickets " + String.valueOf(cursor.getCount()));

        cursor.close();

        return numberofTickets;
    }

    public String getTicketID(String PerformanceID, String SeatType) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Tickets where PerformanceID = ? and Seat = ? and UserID IS NULL", new String[]{String.valueOf(PerformanceID), SeatType});

        String seatID;

        if (cursor.moveToFirst()) {
            seatID = cursor.getString(0);
        } else {
            seatID = null;
        }

        cursor.close();

        return seatID;
    }

    public void bookTicket(String TicketID, String UserID) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", UserID);

        database.update("Tickets", contentValues, "TicketID = ?", new String[]{TicketID});
        database.close();
    }

    public ArrayList getBookings(Integer UserID) {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("Select * from Tickets where UserID = ?", new String[]{String.valueOf(UserID)});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(3) + " ticket - ID" + cursor.getString(0));
            }
        }
        return result;
    }

    public ArrayList getBookingsID(Integer UserID) {
        ArrayList result = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("Select * from Tickets where UserID = ?", new String[]{String.valueOf(UserID)});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0));
            }
        }
        return result;
    }

    public void deleteTickets (String UserID, String TicketID) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.putNull("UserID");

        database.update("Tickets", contentValues, "TicketID = ?", new String[]{TicketID});
        database.close();
    }

    // Complex
    // Get performance name from booking
    public String getPerformanceIDFromBookings(String TicketID) {
        SQLiteDatabase database = this.getReadableDatabase();

        String PerformanceID, PerformanceName;

        Cursor cursor = database.rawQuery("Select * from Tickets where TicketID = ?", new String[]{TicketID});

        if (cursor.moveToFirst()) {
            PerformanceID = cursor.getString(1);
        } else {
            return null;
        }
        cursor.close();
        return PerformanceID;
    }

    public String getPerformanceFromBookings(String TicketID) {
        SQLiteDatabase database = this.getReadableDatabase();

        String PerformanceID, PerformanceName;

        Cursor cursor = database.rawQuery("Select * from Tickets where TicketID = ?", new String[]{TicketID});

        if (cursor.moveToFirst()) {
            PerformanceID = cursor.getString(1);
        } else {
            return null;
        }
        cursor.close();

        cursor = database.rawQuery("Select Performance from Performances where PerformanceID = ?", new String[]{PerformanceID});

        if (cursor.moveToFirst()) {
            PerformanceName = cursor.getString(0);
        } else {
            return null;
        }

        return PerformanceName;
    }

    public String getPerformanceDetailsFromID (String PerformanceID) {
        String result = null;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Performances where PerformanceID = ?", new String[]{PerformanceID});

        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result = ("By " + cursor.getString(2) + "\n" +
                        cursor.getString(3) + ",\n" + cursor.getString(4) + ",\n" + cursor.getString(5) + ",\n" + cursor.getString(6));
            }
        }
        cursor.close();
        return result;
    }

    public String getPerformanceDetails2FromID(String PerformanceID) {
        String result = null;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from Performances where PerformanceID = ?", new String[]{PerformanceID});

        String steps, wheelchair, lights, consent;

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                if (cursor.getString(9).equals("1")) {
                    steps = "Y";
                } else {
                    steps = "N";
                }

                if (cursor.getString(10).equals("1")) {
                    wheelchair = "Y";
                } else {
                    wheelchair = "N";
                }

                if (cursor.getString(11).equals("1")) {
                    lights = "Y";
                } else {
                    lights = "N";
                }

                if (cursor.getString(12).equals("1")) {
                    consent = "Y";
                } else {
                    consent = "N";
                }
                result = (cursor.getString(7) + " @ " + cursor.getString(8) + "\n" +
                        "Must Climb 20 Steps: " + steps + "\n" +
                        "Wheelchair Access: " + wheelchair + "\n" +
                        "Flashing Lights: " + lights + "\n" +
                        "Consent Form: " + consent);
            }
        }
        cursor.close();
        return result;
    }

    public String getSeatID (String seatID) {
        SQLiteDatabase database = this.getReadableDatabase();

        String S_seatID;

        Cursor cursor = database.rawQuery("Select * from Tickets where seatID = ?", new String[]{seatID});

        if (cursor.moveToFirst()) {
            S_seatID = cursor.getString(2);
        } else {
            return null;
        }
        cursor.close();

        return S_seatID;
    }

    public String getSeatDetails(String ticketID) {
        SQLiteDatabase database = this.getReadableDatabase();

        String result;

        Cursor cursor = database.rawQuery("Select * from Tickets where ticketID = ?", new String[]{ticketID});

        if (cursor.moveToFirst()) {
            result = "Seat Type: " + cursor.getString(3) + "\nSeat ID: " + cursor.getString(0);

        } else {
            return null;
        }
        cursor.close();

        return result;
    }
}