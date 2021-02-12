package team18.pharmacyapp.helpers;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeHelpers {

    public final static Calendar calendar = GregorianCalendar.getInstance();

    public static LocalTime getTimeWithoutDate(Date date) {
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String hoursString = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesString = minutes < 10 ? "0" + minutes : String.valueOf(minutes);

        return LocalTime.parse(hoursString + ":" + minutesString + ":00");
    }

    public static Date getDateWithoutTime(Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static boolean checkIfTimesIntersect(Date date1Start, Date date1End, Date date2Start, Date date2End) {
        /*
        Explanation:
            1)    1 __________           2) 1   _____________   3)  1 _____________    4) 1    _____
                  2    ___________          2 ____________          2    _______          2 ____________
         */

        if (date1Start.equals(date2Start)) return true; // 0 **start at same time
        // Partial intersection
        if (date1Start.before(date2Start) && date1End.before(date2End) && date2Start.before(date1End)) return true; // 1
        if (date2Start.before(date1Start) && date2End.before(date1End) && date1Start.before(date2End)) return true; // 2

        // Complete intersection
        if (date1Start.before(date2Start) && date1End.after(date2End)) return true; // 3
        return date2Start.before(date1Start) && date2End.after(date1End); // 4
    }
}
