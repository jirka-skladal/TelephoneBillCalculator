package com.phonecompany.billing;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarTool {

    private CalendarTool() {
        // restrict instantiation
    }

    public static Calendar min(Calendar calendar1, Calendar calendar2) {
        if (calendar1.getTimeInMillis() < calendar2.getTimeInMillis()) {
            return calendar1;
        }
        else {
            return calendar2;
        }
    }

    public static Calendar max(Calendar calendar1, Calendar calendar2) {
        if (calendar1.getTimeInMillis() > calendar2.getTimeInMillis()) {
            return calendar1;
        }
        else {
            return calendar2;
        }
    }

    public static long minutesBetween(Calendar startTime, Calendar endTime) {
        double start = startTime.getTimeInMillis();
        double end = endTime.getTimeInMillis();
        double minutes = (end - start) / (1000*60); // TODO asi by to mela byt konstanta
        long ceilMinutes = (long) Math.ceil(minutes);
        return Math.max(ceilMinutes, 0); //TimeUnit.MILLISECONDS.toMinutes(vyslL);
    }

    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }
}
