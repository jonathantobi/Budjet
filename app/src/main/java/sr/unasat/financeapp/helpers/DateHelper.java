package sr.unasat.financeapp.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public String monthIntToString(int month){

        switch (month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "--";
        }

    }

    public static long dateToMiliseconds(String givenDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        Date date = null;
        String inputString = " 23:59:59.999";
        try {
            date = sdf.parse(givenDate + inputString);
            long miliseconds = date.getTime();
            System.out.println("dateFormat: " + miliseconds);
            return miliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String milisecondsToDate(long miliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliseconds);
        return sdf.format(calendar.getTime());
    }
}
