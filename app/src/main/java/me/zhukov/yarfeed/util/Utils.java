package me.zhukov.yarfeed.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
