package me.academeg.timetoend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Time {

    protected Date date;
    protected SimpleDateFormat formatDate;
    protected String dateStr;

    Time() {
        date = new Date();
        formatDate = new SimpleDateFormat("HH:mm");
        dateStr = formatDate.format(date);
    }

    public int compareTo(String secDate) {
        return dateStr.compareTo(secDate);
    }

    public long getMinutesBetweenTime(String endDate) {
        long minutes = 0;

        try {
            minutes = formatDate.parse(endDate).getTime()
                    - formatDate.parse(dateStr).getTime();
        }
        catch(ParseException e) {
            e.printStackTrace();
        }

        minutes = minutes/1000/60;
        return minutes;
    }

    public String toString() {
        return dateStr;
    }

    private String addLeadingZeroes(String str) {
        if (str.length() < 5)
            return ("0" + str);
        return str;
    }

}
