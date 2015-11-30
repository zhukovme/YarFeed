package me.zhukov.yarfeed.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.List;

import me.zhukov.yarfeed.database.NewsTable;
import me.zhukov.yarfeed.model.NewsItem;

/**
 * @author Michael Zhukov
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    private Context mContext;
    private NewsTable mNewsTable;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public NewsLoader(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        super(context);
        mContext = context;
        mNewsTable = new NewsTable(context);
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
            NewsXmlParser parser = new NewsXmlParser();
            NewsTable newsTable = new NewsTable(mContext);
            newsTable.save(parser.parse());
            return mNewsTable.getNewsList();
        } catch (Exception e) {
            e.printStackTrace();
            return mNewsTable.getNewsList();
        }
    }
}
