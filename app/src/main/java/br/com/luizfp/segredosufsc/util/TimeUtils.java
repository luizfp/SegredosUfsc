package br.com.luizfp.segredosufsc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luiz on 2/23/16.
 */
public class TimeUtils {

    public static String toUserFriendlyTimestamp(Date date) {
        return dateFormat(date) + " às " + extractHour(date);
    }

    private static String extractHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String hour = String.format("%d", calendar.get(Calendar.HOUR_OF_DAY));
        int m = calendar.get(Calendar.MINUTE);
        String minute = "";
        if (m < 10) {
             minute = String.format("0%d", calendar.get(Calendar.MINUTE));
        } else {
            minute = String.format("%d", calendar.get(Calendar.MINUTE));
        }
        return hour+"h"+minute;
    }

    private static String dateFormat(Date date){
        if (date == null) {
            return "";
        }
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return s.format(date);
    }
}
