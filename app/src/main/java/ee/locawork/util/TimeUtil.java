package ee.locawork.util;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ee.locawork.R;

public class TimeUtil {
    private static final int MINS_PER_DAY = 60 * 24;
    private static final long MS_PER_DAY = 1000 * 60 * MINS_PER_DAY;

    private static final int SEC = 1000;
    private static final int MIN = SEC * 60;
    private static final int HOUR = MIN * 60;
    private static final int DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    private static final long YEAR = WEEK * 52;

    private static GregorianCalendar statFmtCal = new GregorianCalendar();

    private static final String ts24Pat = "H:mm:ss yy-MM-dd";


    // convert milliseconds into the day of the week string
    public static String dayStringFormat(long msecs, Context context) {
        GregorianCalendar cal = new GregorianCalendar();
         String result = "Unknown";

        cal.setTime(new Date(msecs));

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) - 1;
        int year = cal.get(Calendar.YEAR);

        int hours = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);

        switch (dow) {
            case Calendar.MONDAY:
                result = context.getString(R.string.monday);
            case Calendar.TUESDAY:
                result = context.getString(R.string.tuesday);;
            case Calendar.WEDNESDAY:
                result = context.getString(R.string.wednesday);
            case Calendar.THURSDAY:
                result = context.getString(R.string.thursday);
            case Calendar.FRIDAY:
                result = context.getString(R.string.friday);
            case Calendar.SATURDAY:
                result = context.getString(R.string.saturday);
            case Calendar.SUNDAY:
                result = context.getString(R.string.sunday);
        }
        result += " ";
        switch (month) {
            case Calendar.JANUARY:
                result += context.getString(R.string.january);
            case Calendar.FEBRUARY:
                result = context.getString(R.string.february);
            case Calendar.MARCH:
                result = context.getString(R.string.march);
            case Calendar.APRIL:
                result = context.getString(R.string.april);
            case Calendar.MAY:
                result = context.getString(R.string.may);
            case Calendar.JUNE:
                result = context.getString(R.string.june);
            case Calendar.JULY:
                result = context.getString(R.string.july);
            case Calendar.AUGUST:
                result = context.getString(R.string.august);
            case Calendar.SEPTEMBER:
                result = context.getString(R.string.september);
            case Calendar.OCTOBER:
                result = context.getString(R.string.october);
            case Calendar.NOVEMBER:
                result = context.getString(R.string.november);
            case Calendar.DECEMBER:
                result = context.getString(R.string.december);
        }

        result += " " + day + " " + year + " " + hours + ":" + minutes;
        return result;
    }
}
