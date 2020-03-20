package sr.unasat.financeapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import sr.unasat.financeapp.entities.Goal;
import sr.unasat.financeapp.entities.Transaction;
import sr.unasat.financeapp.helpers.DateHelper;

/**
 * Created by Benito on 12-Mar-18.
 */

public class GoalDao {
    private Context context;
    ComfiDbHelper cmDbHelper;
    SQLiteDatabase db;

    public GoalDao(Context context) {
        this.context = context;
        cmDbHelper = new ComfiDbHelper(context);
    }

    public Goal getGoal(long date){
        Goal goal = null;
        String stringDate = DateHelper.milisecondsToDate(date);

        System.out.println("stringDate:" + stringDate);

        try {
            db = cmDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ComfiContract.GoalEntry.COLUMN_NAME_ID,
                    ComfiContract.GoalEntry.COLUMN_NAME_AMOUNT,
                    ComfiContract.GoalEntry.COLUMN_NAME_DATE,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = "strftime('%m', " + ComfiContract.GoalEntry.COLUMN_NAME_DATE + ") = ? " +
                                "AND strftime('%Y', " + ComfiContract.GoalEntry.COLUMN_NAME_DATE + ") = ? ";
            String[] selectionArgs = {"03", "2018"};

            Cursor cursor = db.query(
                    ComfiContract.GoalEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    null,              // The sort order
                    null
            );

            System.out.println(cursor);

            if(cursor!=null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    goal = new Goal(
                            cursor.getInt(0),
                            cursor.getDouble(1),
                            cursor.getLong(2));
                    System.out.println(goal.toString());
                    return goal;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return goal;
    }

    public boolean addGoal(Goal goal){
        try{
            // Gets the data repository in write mode
            db = cmDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ComfiContract.GoalEntry.COLUMN_NAME_AMOUNT, goal.getAmount());
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_DATE, new Date().getTime());

            System.out.println(values);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(ComfiContract.GoalEntry.TABLE_NAME, null, values);

            if (newRowId > 0) {
                return true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return false;
    }

}
