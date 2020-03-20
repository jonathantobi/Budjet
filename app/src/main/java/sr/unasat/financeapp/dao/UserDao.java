package sr.unasat.financeapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import sr.unasat.financeapp.entities.Transaction;
import sr.unasat.financeapp.entities.User;

/**
 * Created by Mitchell Jones on 3/12/2018.
 */

public class UserDao {

    private Context context;
    ComfiDbHelper cmDbHelper;
    SQLiteDatabase db;

    public static final String PREFERENCE_LOGGED_IN_KEY = "isLoggedIn";
    public static final String PREFERENCE_USER_ID_KEY = "loggedInUser";
    public static final String PREFERENCE_SELECTED_CURRENCY_KEY = "selectedCurrency";

    public UserDao(Context context) {
        this.context = context;
        cmDbHelper = new ComfiDbHelper(context);
    }

    public boolean registerUser(User user){
        try {
            // Gets the data repository in write mode
            db = cmDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ComfiContract.UserEntry.COLUMN_NAME_USER_NAME, user.getUsername());
            values.put(ComfiContract.UserEntry.COLUMN_NAME_EMAIL, user.getEmail());
            values.put(ComfiContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(ComfiContract.UserEntry.TABLE_NAME, null, values);

            if(newRowId > 0){
                return true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return false;
    }

    public User Authenticate(User user) {

        try {
            db = cmDbHelper.getReadableDatabase();

            Cursor cursor = db.query(ComfiContract.UserEntry.TABLE_NAME,// Selecting Table
                    new String[]{ComfiContract.UserEntry.COLUMN_NAME_ID,
                            ComfiContract.UserEntry.COLUMN_NAME_USER_NAME,
                            ComfiContract.UserEntry.COLUMN_NAME_EMAIL, },//Selecting columns want to query
                    ComfiContract.UserEntry.COLUMN_NAME_EMAIL + "= ? AND " +
                            ComfiContract.UserEntry.COLUMN_NAME_PASSWORD + "= ?",
                    new String[]{user.getEmail(), user.getPassword()},//Where clause
                    null, null, null);

            if (cursor != null && cursor.moveToFirst() && cursor.getCount()>0) {
                //if cursor has value then in user database there is user associated with this given email
                User authenticatedUser = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));

                System.out.println(authenticatedUser.toString());

                System.out.println(authenticatedUser.getId());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putBoolean(PREFERENCE_LOGGED_IN_KEY, true).commit();
                prefs.edit().putInt(PREFERENCE_USER_ID_KEY, authenticatedUser.getId()).commit();

                System.out.println("prefs val: " + prefs.getInt(PREFERENCE_USER_ID_KEY, 0));

                return authenticatedUser;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public boolean isEmailExists(String email) {

        db = cmDbHelper.getReadableDatabase();

        Cursor cursor = db.query(ComfiContract.UserEntry.TABLE_NAME,// Selecting Table
                new String[]{ComfiContract.UserEntry.COLUMN_NAME_ID},//Selecting columns want to query
                ComfiContract.UserEntry.COLUMN_NAME_EMAIL + " = ?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }

    private User getUser(int userID){
        try {
            db = cmDbHelper.getReadableDatabase();

            Cursor cursor = db.query(ComfiContract.UserEntry.TABLE_NAME,// Selecting Table
                    new String[]{ComfiContract.UserEntry.COLUMN_NAME_ID,
                            ComfiContract.UserEntry.COLUMN_NAME_USER_NAME,
                            ComfiContract.UserEntry.COLUMN_NAME_EMAIL, },//Selecting columns want to query
                    ComfiContract.UserEntry.COLUMN_NAME_ID + "= ?",
                    new String[]{String.valueOf(userID)},//Where clause
                    null, null, null);

            if (cursor != null && cursor.moveToFirst() && cursor.getCount()>0) {
                //if cursor has value then in user database there is user associated with this given email
                User user = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));

                System.out.println(user.toString());
                return user;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public User isLoggedIn(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isLoggedIn = prefs.getBoolean(PREFERENCE_LOGGED_IN_KEY, false);

        if(isLoggedIn){
            int userID = prefs.getInt(PREFERENCE_USER_ID_KEY, 0);

            System.out.println(userID);

            if(userID > 0){
                return getUser(userID);
            }
        }

        return null;
    }

    public boolean logUserOut(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(PREFERENCE_LOGGED_IN_KEY, false).commit();
        prefs.edit().putInt(PREFERENCE_USER_ID_KEY, 0).commit();

        if(isLoggedIn() == null){
            return true;
        }

        return false;
    }

    public boolean updateUser(User user){
        try {
            // Gets the data repository in write mode
            db = cmDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ComfiContract.UserEntry.COLUMN_NAME_USER_NAME, user.getUsername());
            values.put(ComfiContract.UserEntry.COLUMN_NAME_EMAIL, user.getEmail());
            values.put(ComfiContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());

            // Insert the new row, returning the primary key value of the new row
            int status = db.update(ComfiContract.UserEntry.TABLE_NAME, values, ComfiContract.UserEntry.COLUMN_NAME_ID + " = ?",
                    new String[] { String.valueOf(user.getId()) });

            if(status > 0){
                return true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return false;
    }


    public String getSelectedCurrency() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREFERENCE_SELECTED_CURRENCY_KEY, "SRD");
    }

    public void updateSelectedCurrency(String s) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(PREFERENCE_SELECTED_CURRENCY_KEY, s).commit();
    }

    public boolean deleteUser(int id) {
        try {
            // Gets the data repository in write mode
            db = cmDbHelper.getWritableDatabase();

            return db.delete(ComfiContract.UserEntry.TABLE_NAME, ComfiContract.UserEntry.COLUMN_NAME_ID + "= ?", new String[] { String.valueOf(id) }) > 0;

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return false;
    }
}
