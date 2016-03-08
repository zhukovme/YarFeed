package me.zhukov.yarfeed.util;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Michael Zhukov
 */
public enum UrlHelper {

    INSTANCE;

    @Nullable
    public URL urlFromString(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri urlToUri(URL url) {
        return Uri.parse(url.toString());
    }
}
