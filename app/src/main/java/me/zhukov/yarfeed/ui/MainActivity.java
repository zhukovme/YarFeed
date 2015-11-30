package me.zhukov.yarfeed.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import me.zhukov.yarfeed.R;
import me.zhukov.yarfeed.database.NewsTable;
import me.zhukov.yarfeed.loader.NewsLoader;
import me.zhukov.yarfeed.model.NewsItem;
import me.zhukov.yarfeed.util.Utils;

/**
 * @author Michael Zhukov
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRvNews;
    private List<NewsItem> mNewsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRvNews = (RecyclerView) findViewById(R.id.rv_news);

        setSupportActionBar(toolbar);

        mNewsItems = new NewsTable(this).getNewsList();
        mRvNews.setLayoutManager(new LinearLayoutManager(this));
        mRvNews.setAdapter(initAdapter(mNewsItems));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivity.this.getLoaderManager()
                        .restartLoader(R.integer.news_loader_id, Bundle.EMPTY, MainActivity.this);
            }
        });
        getLoaderManager().initLoader(R.integer.news_loader_id, Bundle.EMPTY, this);
    }

    private NewsCardsAdapter initAdapter(List<NewsItem> newsItems) {
        return new NewsCardsAdapter(this, newsItems);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, mSwipeRefreshLayout);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        if (data != null) {
            mNewsItems.clear();
            mNewsItems.addAll(data);
            mRvNews.getAdapter().notifyDataSetChanged();
        }
        if (!Utils.isNetworkAvailable(this)) {
            Snackbar.make(mSwipeRefreshLayout, R.string.no_internet_msg, Snackbar.LENGTH_SHORT)
                    .show();
        }
        mSwipeRefreshLayout.setRefreshing(false);
        getLoaderManager().destroyLoader(R.integer.news_loader_id);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_cache:
                new NewsTable(this).clear();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
