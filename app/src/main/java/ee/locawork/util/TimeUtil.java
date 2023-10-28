package ee.locawork.util;

/**
 * Copyright (c) 2006 Richard Rodgers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.monad.homerun.util;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * TimeUtil is a utility class with static methods to convert times in various
 * formats into other formats
 */

public class TimeUtil {
    private static final int MINS_PER_DAY = 60 * 24;
    private static final long MS_PER_DAY = 1000 * 60 * MINS_PER_DAY;

    private static final int SEC = 1000;
    private static final int MIN = SEC * 60;
    private static final int HOUR = MIN * 60;
    private static final int DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    private static final long YEAR = WEEK * 52;

    private static final long[] buckets = { YEAR, WEEK, DAY, HOUR, MIN, SEC };
    private static final String[] bucketNames = { "year", "week", "day",
            "hour", "minute", "second" };

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
                result = "Monday";
            case Calendar.TUESDAY:
                result = "Tuesday";
            case Calendar.WEDNESDAY:
                result = "Wednesday";
            case Calendar.THURSDAY:
                result = "Thursday";
            case Calendar.FRIDAY:
                result = "Friday";
            case Calendar.SATURDAY:
                result = "Saturday";
            case Calendar.SUNDAY:
                result = "Sunday";
        }
        result += " ";
        switch (month) {
            case Calendar.JANUARY:
                result += "January";
            case Calendar.FEBRUARY:
                result = "February";
            case Calendar.MARCH:
                result = "March";
            case Calendar.APRIL:
                result = "April";
            case Calendar.MAY:
                result = "May";
            case Calendar.JUNE:
                result = "June";
            case Calendar.JULY:
                result = "July";
            case Calendar.AUGUST:
                result = "August";
            case Calendar.SEPTEMBER:
                result = "September";
            case Calendar.OCTOBER:
                result = "October";
            case Calendar.NOVEMBER:
                result = "November";
            case Calendar.DECEMBER:
                result = "December";
        }

        result += " " + day + " " + year + " " + hours + ":" + minutes;
        return result;
    }
}
