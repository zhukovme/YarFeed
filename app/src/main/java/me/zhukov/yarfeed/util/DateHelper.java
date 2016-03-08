package me.zhukov.yarfeed.util;

import android.support.annotation.Nullable;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Michael Zhukov
 */
public enum  DateHelper {

    INSTANCE;

    private static final String DATE_PATTERN_FROM = "EEE, dd MMM yyyy kk:mm:ss";
    private static final String DATE_PATTERN_TO = "dd MMMM yyyy  kk:mm";

    @Nullable
    public Date dateFromString(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_FROM, Locale.ENGLISH);
        Date parsedDate;
        try {
            parsedDate = formatter.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return parsedDate;
    }

    @Nullable
    public String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormat.format(DATE_PATTERN_TO, date).toString();
    }
}
