package me.zhukov.yarfeed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.zhukov.yarfeed.model.NewsItem;
import me.zhukov.yarfeed.util.BitmapHelper;

/**
 * @author Michael Zhukov
 */
public enum NewsTable {

    INSTANCE;

    private SQLiteDatabase mDatabase;

    @NonNull
    private ContentValues toContentValues(NewsItem newsItem) {
        ContentValues values = new ContentValues();
        values.put(Columns.TITLE, newsItem.getTitle());
        values.put(Columns.LINK, newsItem.getLink());
        values.put(Columns.DESCRIPTION, newsItem.getDescription());
        values.put(Columns.ENCLOSURE, BitmapHelper.INSTANCE.bitmapToBytes(newsItem.getEnclosure()));
        values.put(Columns.CATEGORY, newsItem.getCategory());
        values.put(Columns.PUB_DATE, newsItem.getPubDate().getTime());
        return values;
    }

    @NonNull
    private NewsItem fromCursor(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(Columns.TITLE));
        String link = cursor.getString(cursor.getColumnIndex(Columns.LINK));
        String description = cursor.getString(cursor.getColumnIndex(Columns.DESCRIPTION));

        byte[] enclosureBytes = cursor.getBlob(cursor.getColumnIndex(Columns.ENCLOSURE));
        Bitmap enclosure = BitmapHelper.INSTANCE.bitmapFromBytes(enclosureBytes);

        String category = cursor.getString(cursor.getColumnIndex(Columns.CATEGORY));
        Date pubDate = new Date(cursor.getLong(cursor.getColumnIndex(Columns.PUB_DATE)));
        return new NewsItem(title, link, description, enclosure, pubDate, category);
    }

    public long save(List<NewsItem> newsItems) {
        long count = 0;
        if (newsItems == null) {
            return count;
        }
        for (NewsItem newsItem : newsItems) {
            count += mDatabase.insert(Requests.TABLE_NAME, null, toContentValues(newsItem));
        }
        return count;
    }

    @NonNull
    public List<NewsItem> getNewsList() {
        Cursor cursor = mDatabase.query(
                Requests.TABLE_NAME, null, null, null, null, null, Columns.PUB_DATE + " DESC");

        List<NewsItem> newsItems = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return newsItems;
        }
        try {
            do {
                newsItems.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            return newsItems;
        } finally {
            cursor.close();
        }
    }

    public void clear() {
        mDatabase.delete(Requests.TABLE_NAME, null, null);
    }

    public void open(Context context) {
        NewsDbHelper dbHelper = new NewsDbHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(mDatabase);
    }

    public void close() {
        mDatabase.close();
    }

    public interface Columns {
        String TITLE = "title";
        String LINK = "link";
        String DESCRIPTION = "description";
        String ENCLOSURE = "enclosure";
        String PUB_DATE = "pub_date";
        String CATEGORY = "category";
    }

    public interface Requests {

        String TABLE_NAME = NewsTable.class.getSimpleName();

        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.TITLE + " TEXT, " +
                Columns.LINK + " TEXT, " +
                Columns.DESCRIPTION + " TEXT, " +
                Columns.ENCLOSURE + " BLOB, " +
                Columns.PUB_DATE + " TEXT, " +
                Columns.CATEGORY + " TEXT, " +
                "UNIQUE(" + Columns.LINK + ")" +
                "ON CONFLICT REPLACE" + ");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
