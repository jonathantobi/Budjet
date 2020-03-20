package sr.unasat.financeapp.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.NumberFormat;

import sr.unasat.financeapp.dao.UserDao;

public class CurrencyHelper {

    public static String returnStringCurrency(double amount, Context context){
        //SRD
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = prefs.getString(UserDao.PREFERENCE_SELECTED_CURRENCY_KEY, "SRD");

        return currency + " " + amount;
    }
}
