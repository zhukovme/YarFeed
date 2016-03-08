package me.zhukov.yarfeed.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import java.net.URL;
import java.util.List;

import me.zhukov.yarfeed.database.NewsTable;
import me.zhukov.yarfeed.model.NewsItem;
import me.zhukov.yarfeed.sax.NewsXmlReader;

/**
 * @author Michael Zhukov
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    public static final String XML_URL = "http://76.ru/text/rss.xml";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public NewsLoader(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        super(context);
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        try {
            URL url = new URL(XML_URL);
            List<NewsItem> newsItems = NewsXmlReader.INSTANCE.readXML(url);
            NewsTable.INSTANCE.save(newsItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NewsTable.INSTANCE.getNewsList();
    }
}
