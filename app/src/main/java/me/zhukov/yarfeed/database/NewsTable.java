package me.zhukov.yarfeed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.zhukov.yarfeed.model.NewsItem;

/**
 * @author Michael Zhukov
 */
public class NewsTable {

    private NewsDbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public NewsTable(Context context) {
        mDbHelper = new NewsDbHelper(context);
    }

    private ContentValues toContentValues(NewsItem newsItem) {
        ContentValues values = new ContentValues();
        values.put(Columns.TITLE, newsItem.getTitle());
        values.put(Columns.LINK, newsItem.getLink());
        values.put(Columns.DESCRIPTION, newsItem.getDescription());

        if (newsItem.getEnclosure() != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            newsItem.getEnclosure().compress(Bitmap.CompressFormat.PNG, 100, bos);
            values.put(Columns.ENCLOSURE, bos.toByteArray());
        }

        values.put(Columns.PUB_DATE, newsItem.getPubDate());
        return values;
    }

    private NewsItem fromCursor(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(Columns.TITLE));
        String link = cursor.getString(cursor.getColumnIndex(Columns.LINK));
        String description = cursor.getString(cursor.getColumnIndex(Columns.DESCRIPTION));

        byte[] enclosureBytes = cursor.getBlob(cursor.getColumnIndex(Columns.ENCLOSURE));
        Bitmap enclosure = null;
        if (enclosureBytes != null) {
            enclosure = BitmapFactory.decodeByteArray(enclosureBytes, 0, enclosureBytes.length);
        }

        String pubDate = cursor.getString(cursor.getColumnIndex(Columns.PUB_DATE));
        return new NewsItem(title, link, description, enclosure, pubDate);
    }

    public void save(NewsItem newsItem) {
        mDatabase = mDbHelper.getWritableDatabase();
        mDatabase.insert(Requests.TABLE_NAME, null, toContentValues(newsItem));
        mDatabase.close();
        mDbHelper.close();
    }

    public void save(List<NewsItem> newsItems) {
        mDatabase = mDbHelper.getWritableDatabase();
        for (int i = 0; i < newsItems.size(); i++) {
            mDatabase.insert(Requests.TABLE_NAME, null, toContentValues(newsItems.get(i)));
        }
        mDatabase.close();
        mDbHelper.close();
    }

    public List<NewsItem> getNewsList() {
        mDatabase = mDbHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(Requests.TABLE_NAME, null, null, null, null, null, null);

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
            mDatabase.close();
            mDbHelper.close();
        }
    }

    public void clear() {
        mDatabase = mDbHelper.getWritableDatabase();
        mDatabase.delete(Requests.TABLE_NAME, null, null);
        mDatabase.rawQuery(Requests.DROP_REQUEST, null);
        mDatabase.close();
        mDbHelper.close();
    }

    public interface Columns {
        String TITLE = "title";
        String LINK = "link";
        String DESCRIPTION = "description";
        String ENCLOSURE = "enclosure";
        String PUB_DATE = "pub_date";
    }

    public interface Requests {

        String TABLE_NAME = NewsTable.class.getSimpleName();

        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.TITLE + " TEXT, " +
                Columns.LINK + " TEXT, " +
                Columns.DESCRIPTION + " TEXT, " +
                Columns.ENCLOSURE + " BLOB, " +
                Columns.PUB_DATE + " TEXT, " +
                "UNIQUE(" + Columns.TITLE + ")" +
                "ON CONFLICT REPLACE" + ");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
