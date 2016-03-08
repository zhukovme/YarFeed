package me.zhukov.yarfeed.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Michael Zhukov
 */
public enum BitmapHelper {

    INSTANCE;

    @Nullable
    public Bitmap getBitmapFromUrl(String url) {
        try {
            return BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    @Nullable
    public Bitmap bitmapFromBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
