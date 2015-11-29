package me.zhukov.yarfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Michael Zhukov
 */
public class Utils {

    public static Bitmap fetchImage(String url) throws Exception {
        return BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
    }

    public static String dateFormatter(String date) {
        SimpleDateFormat formatter =
                new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss", Locale.ENGLISH);
        Date parsedDate;
        try {
            parsedDate = formatter.parse(date);
        } catch (ParseException e) {
            return date;
        }
        return new SimpleDateFormat("dd.M.yyyy  kk:mm", Locale.getDefault()).format(parsedDate);
    }
}
