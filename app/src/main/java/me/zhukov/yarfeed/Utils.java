package me.zhukov.yarfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

/**
 * @author Michael Zhukov
 */
public class Utils {

    public static Bitmap fetchImage(String url) throws Exception {
        return BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
    }
}
