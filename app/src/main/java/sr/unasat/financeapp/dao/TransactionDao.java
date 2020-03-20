package sr.unasat.financeapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sr.unasat.financeapp.entities.Transaction;
import sr.unasat.financeapp.helpers.DateHelper;

public class TransactionDao {
    Context context;
    ComfiDbHelper cmDbHelper;
    SQLiteDatabase db;

    public TransactionDao(Context context) {
        this.context = context;
        cmDbHelper = new ComfiDbHelper(context);
    }
    public Transaction getTransaction(int transactionID){
        Transaction transaction = new Transaction();

        try {
            db = cmDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ComfiContract.TransactionEntry.COLUMN_NAME_ID,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TYPE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TITLE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT,
                    ComfiContract.TransactionEntry.COLUMN_NAME_DATE,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = ComfiContract.TransactionEntry.COLUMN_NAME_ID + " = ?";
            String[] selectionArgs = {Integer.toString(transactionID)};

            Cursor cursor = db.query(
                    ComfiContract.TransactionEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    null,              // The sort order
                    null                   //limit
            );

            if(cursor!=null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    transaction = new Transaction(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getLong(4));
                    System.out.println(transaction.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return transaction;
    }

    public boolean addTransaction(Transaction transaction){
        try{
            // Gets the data repository in write mode
            db = cmDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_TYPE, transaction.getType());
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_TITLE, transaction.getTitle());
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT, transaction.getAmount());
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_DATE, new Date().getTime());

            System.out.println(values);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(ComfiContract.TransactionEntry.TABLE_NAME, null, values);
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

    public boolean editTransaction(Transaction transaction) {

        try{
            // Gets the data repository in write mode
            db = cmDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_TYPE, transaction.getType());
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_TITLE, transaction.getTitle());
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT, transaction.getAmount());
            values.put(ComfiContract.TransactionEntry.COLUMN_NAME_DATE, transaction.getDate());

            // Insert the new row, returning the primary key value of the new row

            return db.update(ComfiContract.TransactionEntry.TABLE_NAME, values, ComfiContract.TransactionEntry.COLUMN_NAME_ID + " =?",new String[]{String.valueOf(transaction.getId())}) > 0;

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return false;
    }

    public boolean deleteTransaction(int transactionID){
        boolean status = false;
        try{
            // Gets the data repository in write mode
            db = cmDbHelper.getWritableDatabase();

            status = db.delete(ComfiContract.TransactionEntry.TABLE_NAME, ComfiContract.TransactionEntry.COLUMN_NAME_ID+"=?", new String[]{Integer.toString(transactionID)}) > 0;

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return status;
    }

    public ArrayList<Transaction> getRecents(long date){
        ArrayList<Transaction> recents = new ArrayList<>();

        try {
            db = cmDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ComfiContract.TransactionEntry.COLUMN_NAME_ID,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TYPE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TITLE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT,
                    ComfiContract.TransactionEntry.COLUMN_NAME_DATE,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " <= ?";
            String[] selectionArgs = {String.valueOf(date)};

            Cursor cursor = db.query(
                    ComfiContract.TransactionEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " DESC",              // The sort order
                    "5"                   //limit
            );

            if(cursor!=null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    Transaction transaction = new Transaction(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getLong(4));
                    System.out.println(transaction.toString());
                    recents.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return recents;
    }

    public double getTotalIncome(long date){
        double totalIncome = 0.00;

        db = cmDbHelper.getReadableDatabase();

        String queryTotalIncome = "SELECT SUM(" + ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT + ") " +
                                    "as totalIncome FROM " + ComfiContract.TransactionEntry.TABLE_NAME +
                                    " WHERE " + ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " <= " + date +
                                    " AND " + ComfiContract.TransactionEntry.COLUMN_NAME_TYPE + " = '" + ComfiContract.TransactionEntry.DEFAULT_INCOME_STRING + "';";

        System.out.println("income query: " + queryTotalIncome);

        try{
            Cursor cursor = db.rawQuery(queryTotalIncome , null);

            if (cursor.moveToFirst()) {
                totalIncome = (double) cursor.getInt(cursor.getColumnIndex("totalIncome"));
                System.out.println("In TransactionDao, total income is " + totalIncome);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return totalIncome;
    }

    public double getTotalExpense(long date){
        double totalExpense = 0.00;

        db = cmDbHelper.getReadableDatabase();

        String queryTotalExpense = "SELECT SUM(" + ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT + ") " +
                "as totalExpense FROM " + ComfiContract.TransactionEntry.TABLE_NAME +
                " WHERE " + ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " <= " + date +
                " AND " + ComfiContract.TransactionEntry.COLUMN_NAME_TYPE + " = '" + ComfiContract.TransactionEntry.DEFAULT_EXPENSE_STRING + "';";

        try {
            Cursor cursor = db.rawQuery(queryTotalExpense, null);

            if(cursor.moveToFirst()){
                totalExpense = (double) cursor.getInt(cursor.getColumnIndex("totalExpense"));
                System.out.println("In TransactionDao, total expense is " + totalExpense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return totalExpense;
    }

    public ArrayList<Transaction> getAllExpense(long date){
        ArrayList<Transaction> allExpenses = new ArrayList<>();

        try {
            db = cmDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ComfiContract.TransactionEntry.COLUMN_NAME_ID,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TYPE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TITLE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT,
                    ComfiContract.TransactionEntry.COLUMN_NAME_DATE,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = ComfiContract.TransactionEntry.COLUMN_NAME_TYPE + " = ?" + " AND " + ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " <= ?";
            String[] selectionArgs = {ComfiContract.TransactionEntry.DEFAULT_EXPENSE_STRING, String.valueOf(date)};

            Cursor cursor = db.query(
                    ComfiContract.TransactionEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " DESC"  // The sort order
            );

            if(cursor!=null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    Transaction transaction = new Transaction(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getLong(4));
//                    System.out.println(transaction.toString());
                    allExpenses.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return allExpenses;
    }

    public ArrayList<Transaction> getAllIncome(long date){
        ArrayList<Transaction> allIncome = new ArrayList<>();

        try {
            db = cmDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ComfiContract.TransactionEntry.COLUMN_NAME_ID,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TYPE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_TITLE,
                    ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT,
                    ComfiContract.TransactionEntry.COLUMN_NAME_DATE,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = ComfiContract.TransactionEntry.COLUMN_NAME_TYPE + " = ?" + " AND " + ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " <= ?";
            String[] selectionArgs = {ComfiContract.TransactionEntry.DEFAULT_INCOME_STRING, String.valueOf(date)};

            Cursor cursor = db.query(
                    ComfiContract.TransactionEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " DESC"  // The sort order
            );

            if(cursor!=null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    Transaction transaction = new Transaction(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getLong(4));
//                    System.out.println(transaction.toString());
                    allIncome.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cmDbHelper.close();
        }

        return allIncome;
    }


}
