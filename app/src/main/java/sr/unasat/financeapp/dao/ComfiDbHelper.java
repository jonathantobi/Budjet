package sr.unasat.financeapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sr.unasat.financeapp.entities.Transaction;
import sr.unasat.financeapp.entities.User;

public class ComfiDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_USER_ENTRIES = " CREATE TABLE " + ComfiContract.UserEntry.TABLE_NAME
            + " ( "
            + ComfiContract.UserEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, "
            + ComfiContract.UserEntry.COLUMN_NAME_USER_NAME + " TEXT, "
            + ComfiContract.UserEntry.COLUMN_NAME_EMAIL + " TEXT, "
            + ComfiContract.UserEntry.COLUMN_NAME_PASSWORD + " TEXT "
            + " ) ";
    private static final String SQL_DELETE_USER_ENTRIES = " DROP TABLE IF EXISTS " + ComfiContract.UserEntry.TABLE_NAME;


    private static final String SQL_CREATE_TRANSACTION_ENTRIES = " CREATE TABLE " + ComfiContract.TransactionEntry.TABLE_NAME
            + " ( "
            + ComfiContract.TransactionEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ComfiContract.TransactionEntry.COLUMN_NAME_TYPE + " TEXT, "
            + ComfiContract.TransactionEntry.COLUMN_NAME_TITLE + " TEXT, "
            + ComfiContract.TransactionEntry.COLUMN_NAME_AMOUNT + " REAL, "
            + ComfiContract.TransactionEntry.COLUMN_NAME_DATE + " INT "
            + " ) ";
    private static final String SQL_DELETE_TRANSACTION_ENTRIES = " DROP TABLE IF EXISTS " + ComfiContract.TransactionEntry.TABLE_NAME;


    private static final String SQL_CREATE_GOAL_ENTRIES = " CREATE TABLE " + ComfiContract.GoalEntry.TABLE_NAME
            + " ( "
            + ComfiContract.GoalEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ComfiContract.GoalEntry.COLUMN_NAME_AMOUNT + " REAL, "
            + ComfiContract.GoalEntry.COLUMN_NAME_DATE + " INT "
            + " ) ";
    private static final String SQL_DELETE_GOAL_ENTRIES = " DROP TABLE IF EXISTS " + ComfiContract.GoalEntry.TABLE_NAME;

    //DATABASE NAME
    public static final String DATABASE_NAME = "comfi_db";
    //DATABASE VERSION
    public static final int DATABASE_VERSION = 2;


    public ComfiDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table when oncreate gets called
        db.execSQL(SQL_CREATE_USER_ENTRIES);
        db.execSQL(SQL_CREATE_TRANSACTION_ENTRIES);
        db.execSQL(SQL_CREATE_GOAL_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop table to create new one if database version updated
        db.execSQL(SQL_DELETE_USER_ENTRIES);
        db.execSQL(SQL_DELETE_TRANSACTION_ENTRIES);
        db.execSQL(SQL_DELETE_GOAL_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}